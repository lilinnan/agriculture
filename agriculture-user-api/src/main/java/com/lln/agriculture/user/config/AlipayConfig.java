package com.lln.agriculture.user.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/21 20:21
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {
    private String protocol;
    private String gateway;
    private String signType;
    private String appId;
    private String merchantPrivateKey;
    private String merchantCertPath;
    private String alipayCertPath;
    private String alipayRootCertPath;

    private String successUrl;
    private String exitUrl;

    private String notifyUrl;

}
