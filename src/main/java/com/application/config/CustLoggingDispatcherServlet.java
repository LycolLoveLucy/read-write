package com.application.config;

import com.baomidou.mybatisplus.core.injector.methods.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustLoggingDispatcherServlet extends DispatcherServlet {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("logging");

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) throws Exception {
        return super.processHandlerException(request, response, handler, ex);
    }

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(doNotWrapperDisptacher(request)){
            super.doDispatch(request,response);
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("uri", requestWrapper.getRequestURI());
        rootNode.put("clientIp", requestWrapper.getRemoteAddr());
        rootNode.set("requestHeaders", mapper.valueToTree(getRequestHeaders(requestWrapper)));
        String method = requestWrapper.getMethod();
        rootNode.put("method", method);
        try {
            super.doDispatch(requestWrapper, responseWrapper);
        } finally {
            if(HttpMethod.GET.name().equals(method)) {
                rootNode.set("request", mapper.valueToTree(requestWrapper.getParameterMap()));
            }
            else {
                JsonNode newNode = mapper.readTree(requestWrapper.getContentAsByteArray());
                rootNode.set("request", newNode);
            }

            if(null!=response.getContentType()&&response.getContentType().equals(MediaType.APPLICATION_JSON.getType())
                    &&response.getContentType().equals( MediaType.APPLICATION_JSON_UTF8.getType())) {
                JsonNode newNode = mapper.readTree(responseWrapper.getContentInputStream());
                rootNode.set("response", newNode);
            }
            else{
                JsonNode newNode = JsonNodeFactory.instance.textNode(new String(responseWrapper.getContentAsByteArray()));
                rootNode.set("response", newNode);

            }

            responseWrapper.copyBodyToResponse();

            rootNode.set("responseHeaders", mapper.valueToTree(getResponsetHeaders(responseWrapper)));
            logger.info(rootNode.toString());
        }


    }

    private Map<String, Object> getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;

    }

    private Map<String, Object> getResponsetHeaders(ContentCachingResponseWrapper response) {
        Map<String, Object> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }

    private  boolean doNotWrapperDisptacher(HttpServletRequest request){
        return  isResourceUrl(request.getRequestURL().toString())||isSwaggerResource(request.getRequestURL().toString());
    }

    private boolean isResourceUrl(String url) {
        return resourceRequestsURI.stream().anyMatch(e->url.contains(e));
    }

    private boolean isSwaggerResource(String url) {
        return swaggerRequestURI.stream().anyMatch(e->url.contains(e));

    }

    static  final   List<String> resourceRequestsURI;

    static {
        resourceRequestsURI = Stream.of(
                "/css/",
                "/js/",
                "/scss/",
                "/fonts/",
                "/error",
                ".css",
                ".js",
                ".scss",
                ".eot",
                ".svg",
                ".ttf",
                ".woff",
                ".otf",
                ".ico",
                ".png"
        ).collect(Collectors.toList());
    }

    final List<String> swaggerRequestURI = Arrays.asList(
            "/v2/api-docs", "/swagger-resources","swagger-ui");
}