package com.application.config;/*
package com.application.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import sun.nio.ch.IOUtil;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

*/
/**
 * 在过滤器中打印输入输出日志
 * @Author
 * @Date 2019-08-06
 *//*

@Component
public class LoggingFilterConfig extends OncePerRequestFilter {

    ObjectMapper objectMapper=new ObjectMapper();
    Logger log= LoggerFactory.getLogger(LoggingFilterConfig.class);

    */
/**
     * 支持打印的MediaType
     *//*

    private static final Set<MediaType> supportMediaTypes=new HashSet<>();
    {
        supportMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportMediaTypes.add(MediaType.APPLICATION_JSON);
        supportMediaTypes.add(MediaType.TEXT_HTML);
        supportMediaTypes.add(MediaType.TEXT_PLAIN);
        supportMediaTypes.add(MediaType.APPLICATION_ATOM_XML);

    }

    final  List<String> resourceRequestsURI = Arrays.asList(
            "/css/", "/js/", "/scss/", "/fonts/",
            ".css", ".js", ".scss", ".eot",
            ".svg", ".ttf", ".woff", ".otf", ".ico", ".png");

    final List<String> swaggerRequestURI = Arrays.asList(
            "/v2/api-docs", "/swagger-resources","swagger-ui");


    ThreadLocal<ContentCachingResponseWrapper> contentCachingResponseWrapperThreadLocal=new ThreadLocal();

    @Override
    protected void doFilterInternal( HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse,
                                     FilterChain filterChain)  {

        try {

            doLog(httpServletRequest,httpServletResponse,filterChain);
        }
        catch (Exception e){
            log.error("do log filter has a error:{} ", e.getCause());
            try {
                contentCachingResponseWrapperThreadLocal.get().copyBodyToResponse();
            } catch (IOException e1) {
                log.error("do log filter has a error:{} ", e1.getCause());
            }
        }
    }

    void doLog(final HttpServletRequest httpServletRequest,
               final HttpServletResponse httpServletResponse,
               final FilterChain filterChain) throws Exception {

        ContentCachingRequestWrapper requestWrapper=new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper responseWrapper=new ContentCachingResponseWrapper(httpServletResponse);
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        }
        catch (Exception e){
            responseWrapper.copyBodyToResponse();
        }
        try {
           contentCachingResponseWrapperThreadLocal.set(responseWrapper);


            String requestUrl = requestWrapper.getRequestURL().toString();
            String requestId = UUID.randomUUID().toString();
            HttpMethod httpMethod = HttpMethod.valueOf(requestWrapper.getMethod());

            log.info(" http method is:{}",  httpMethod.name());
            log.info("requestUrl  is:{}",  requestUrl);

            Enumeration<String> parameterEnum = requestWrapper.getParameterNames();
            if (!ObjectUtils.isEmpty(parameterEnum)) {
                log.info("request param   is:{}",  getRequestParamJSON(requestWrapper));

            }
            log.info("request header  is:{}",  getRequestHeader(requestWrapper));


            if (HttpMethod.valueOf(httpServletRequest.getMethod()) != HttpMethod.GET) {
                if (supportMediaTypes.contains(MediaType.valueOf(httpServletRequest.getContentType()))) {
                    log.info("request body  is:\n{}",  new String(requestWrapper.getContentAsByteArray()));
                }
            }
            HttpStatus responseStatus = HttpStatus.valueOf(responseWrapper.getStatusCode());
            log.info("response status is:{}",  responseStatus.value());
            byte[] responseBytes = new byte[1024 * 24];
            IOUtils.readFully(responseWrapper.getContentInputStream(), responseBytes,0,responseWrapper.getContentSize());
            String responseBody =new String(responseBytes);


            if (httpServletRequest.isAsyncSupported()) {
                log.info(" response payLoad is:\n{}",  responseBody);
            } else if (!isAsyncStarted(httpServletRequest) && httpServletRequest.getDispatcherType() != DispatcherType.ASYNC) {
                log.info("response body is:{}",  objectMapper.readTree(responseBody));
            } else {
                log.info(" cause by request is async:response body is:{}",  "empty");

            }

        }
        catch (Exception e){
            log.error("do log filter has a error:{} ", e.getCause());

            responseWrapper.copyBodyToResponse();

        }
        finally {
            responseWrapper.copyBodyToResponse();

        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)  {

        String requestUrl=request.getRequestURL().toString();
        return  isResourceUrl(requestUrl)||isSwaggerResource(requestUrl);
    }

    */
/**
     * 请求参数列表
     * @param requestWrapper
     * @return
     *//*

    private JSONObject getRequestParamJSON(ContentCachingRequestWrapper requestWrapper) throws JSONException {
        Enumeration<String> parameterEnum=requestWrapper.getParameterNames();
        JSONObject paramJsonObject=new JSONObject();
        if(!ObjectUtils.isEmpty(parameterEnum)) {
            while (parameterEnum.hasMoreElements()) {
                String name= parameterEnum.nextElement();
                String []value=requestWrapper.getParameterValues(name);
                paramJsonObject.put(name,value);

            }
        }
        return  paramJsonObject;
    }

    */
/**
     *
     * @param requestWrapper
     * @return 请求头参数列表
     *//*

    private JSONObject getRequestHeader(ContentCachingRequestWrapper requestWrapper) throws JSONException {

        Enumeration headerNames = requestWrapper.getHeaderNames();
        JSONObject headerJsonObject=new JSONObject();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            headerJsonObject.put(headerName,requestWrapper.getHeader(headerName));
        }
        return  headerJsonObject;
    }

    private boolean isResourceUrl(String url) {
        return resourceRequestsURI.stream().anyMatch(e->url.contains(e));
    }

    private boolean isSwaggerResource(String url) {
        return swaggerRequestURI.stream().anyMatch(e->url.contains(e));

    }
}

*/
