package com.lln.agriculture.user.service.userinfo.impl;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.userinfo.User;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.user.config.SystemConfig;
import com.lln.agriculture.user.mapper.userinfo.UserMapper;
import com.lln.agriculture.user.security.service.CaptchaVerifyService;
import com.lln.agriculture.user.security.service.JwtService;
import com.lln.agriculture.user.service.common.SendMailService;
import com.lln.agriculture.user.service.userinfo.RegisterService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/16 14:18
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    private final UserMapper userMapper;
    private final SendMailService sendMailService;
    private final SystemConfig systemConfig;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final CaptchaVerifyService captchaVerifyService;


    @Autowired
    public RegisterServiceImpl(UserMapper userMapper, SendMailService sendMailService, SystemConfig systemConfig, JwtService jwtService, PasswordEncoder passwordEncoder, CaptchaVerifyService captchaVerifyService) {
        this.userMapper = userMapper;
        this.sendMailService = sendMailService;
        this.systemConfig = systemConfig;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.captchaVerifyService = captchaVerifyService;
    }

    @Override
    public Message register(RegisterBean registerBean, HttpServletRequest request) {
        if (!captchaVerifyService.verify(request, registerBean.getToken())) {
            throw new BadRequestException("二次验证未通过");
        }
        if (!registerBean.getPassword().equals(registerBean.getRePassword())) {
            throw new BadRequestException("两次密码输入不一致");
        }
        if (userMapper.isExistByUsername(registerBean.getUsername())) {
            throw new BadRequestException("用户名已存在");
        }
        if (userMapper.isExistByEmail(registerBean.getEmail())) {
            throw new BadRequestException("该邮箱已注册");
        }
        sendMail(registerBean);
        userMapper.insert(User.builder()
                .username(registerBean.getUsername())
                .password(passwordEncoder.encode(registerBean.getPassword()))
                .email(registerBean.getEmail())
                .enabled(false)
                .avatar(systemConfig.getDefaultAvatar())
                .accountNonLocked(true)
                .build());
        return Message.builder()
                .message("注册成功，请到您的邮箱中查看验证邮件")
                .build();
    }

    @Override
    public Message mailCheck(String jwt) {
        try {
            Jws<Claims> claimsJws = jwtService.parseClaimsJws(jwt);
            if (!"mail-verify".equals(claimsJws.getBody().getSubject())) {
                throw new BadRequestException("请检查您的验证链接！");
            }
            String username = (String) claimsJws.getBody().get("username");
            userMapper.enableUser(username);
            return Message.builder()
                    .message("邮箱验证成功")
                    .build();
        } catch (Exception e) {
            if (e.getClass() == SignatureException.class) {
                throw new BadRequestException("请检查您的验证链接！");
            }
            if (e.getClass() == ExpiredJwtException.class) {
                throw new BadRequestException("验证邮件已失效，请重新发送！");
            }
            throw new BadRequestException("邮件验证失败，请重发邮件");
        }
    }

    @Override
    public Message resendMail(String email, HttpServletRequest request, String token) {
        if (!captchaVerifyService.verify(request, token)) {
            throw new BadRequestException("二次验证失败");
        }
        if (StringUtils.isEmpty(email)) {
            throw new BadRequestException("请输入邮件");
        }
        if (!userMapper.isExistByEmail(email)) {
            throw new BadRequestException("该邮箱未注册");
        }
        if (userMapper.isMailValidated(email)) {
            throw new BadRequestException("该邮箱已验证");
        }
        sendMail(userMapper.selectUsernameByEmail(email), email);
        return Message.builder()
                .message("邮件已重发")
                .build();
    }

    private void sendMail(RegisterBean registerBean) {
        sendMail(registerBean.getUsername(), registerBean.getEmail());
    }

    private void sendMail(String username, String email) {
        Context context = new Context();
        context.setVariable("title", systemConfig.getName() + "注册验证");
        context.setVariable("username", username);
        context.setVariable("systemName", systemConfig.getName());
        context.setVariable("url", String.format(systemConfig.getCheckEmailUrl(),
                jwtService.signNewJwt("mail-verify", "username", username, systemConfig.getEmailValidTime())));
        sendMailService.sendMail(systemConfig.getName() + "注册验证", "check-email.html", email, context);
    }

}
