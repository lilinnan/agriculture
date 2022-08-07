package com.lln.agriculture.manage.security.config.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/3 20:16
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties("vaptcha")
public class VaptchaConfig {
    private String vid;
    private String key;
}
