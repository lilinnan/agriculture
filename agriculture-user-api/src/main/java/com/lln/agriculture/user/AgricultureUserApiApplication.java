package com.lln.agriculture.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author lilinnan
 */
@SpringBootApplication
@EnableConfigurationProperties
public class AgricultureUserApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgricultureUserApiApplication.class, args);
    }

}
