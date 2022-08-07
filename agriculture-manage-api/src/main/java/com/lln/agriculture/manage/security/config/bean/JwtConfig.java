package com.lln.agriculture.manage.security.config.bean;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/4 9:24
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties("jwt")
public class JwtConfig {
    /**
     * 用来签名的key
     */
    private String key;

    /**
     * 签名有效时长，单位：秒
     */
    private String signatureValidTime;


    public Long getSignatureValidTime() {
        return ((Long) AviatorEvaluator.execute(signatureValidTime)) * 1000;
    }

}
