package com.lln.agriculture.user.service.common;

import org.thymeleaf.context.Context;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/16 14:47
 */

public interface SendMailService {
    /**
     * 发送邮件
     *
     * @param title        标题
     * @param templateName 模板名称
     * @param mail         邮箱地址
     * @param context      上下文
     */
    void sendMail(String title, String templateName, String mail, Context context);
}
