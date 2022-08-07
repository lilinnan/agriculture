package com.lln.agriculture.user;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/23 22:13
 */

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AgricultureUserApiApplication.class);
    }
}
