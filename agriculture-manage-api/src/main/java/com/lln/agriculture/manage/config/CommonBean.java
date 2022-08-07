package com.lln.agriculture.manage.config;

import com.google.gson.Gson;
import com.lln.agriculture.common.util.CommonUtils;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/4 9:05
 */

@Configuration
public class CommonBean {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Bean
    public Gson gson() {
        return CommonUtils.getGson();
    }
}
