package com.lln.agriculture.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author lilinnan
 */
@SpringBootApplication
@EnableConfigurationProperties
public class AgricultureManageApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgricultureManageApiApplication.class, args);
    }

}
