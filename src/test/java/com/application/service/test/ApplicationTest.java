package com.application.service.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.*")
public class ApplicationTest {


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ApplicationTest.class);
        application.run(args);
    }

}
