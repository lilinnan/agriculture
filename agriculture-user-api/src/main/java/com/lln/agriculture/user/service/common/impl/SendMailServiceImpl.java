package com.lln.agriculture.user.service.common.impl;

import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.user.service.common.SendMailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/16 14:50
 */
@Service
public class SendMailServiceImpl implements SendMailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public SendMailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMail(String title, String templateName, String email, Context context) {
        try {
            send(title, templateName, email, context);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("邮件发送失败");
        }
    }

    private void send(String title, String templateName, String email, Context context) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setFrom("sntx_feedback@163.com");
        mimeMessageHelper.setTo(email);
        String html = templateEngine.process("email/" + templateName, context);
        mimeMessageHelper.setText(html, true);
        javaMailSender.send(mimeMessage);
    }
}
