package com.lln.agriculture.user.config;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/16 14:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties("system")
public class SystemConfig {

    /**
     * 系统名称
     */
    private String name;

    /**
     * checkEmailUrl，即要去哪里验证此信息，使用{token}表示要提交的token 例如 https://shop.lilinnan.com/#/check-email?{token}
     */
    private String checkEmailUrl;

    private String findPasswordUrl;

    /**
     * 用户默认头像
     */
    private String defaultAvatar;


    /**
     * 验证邮件有效时长单位：秒
     */
    private String emailValidTime;


    public Long getEmailValidTime() {
        return ((Long) AviatorEvaluator.execute(emailValidTime)) * 1000;
    }
}
