package com.lln.agriculture.manage;


import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2021/1/11 13:39
 */

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AgricultureManageApiApplication.class);
    }
}
