package com.lln.agriculture.user.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/6 16:47
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "aliyun")
public class AliYunConfig {

    private String endPoint;
    private String accessKeyId;
    private String accessKeySecret;
}
