package com.lln.agriculture.user.service.userinfo.impl;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.exception.InternalServerErrorException;
import com.lln.agriculture.common.util.CommonUtils;
import com.lln.agriculture.user.config.SystemConfig;
import com.lln.agriculture.user.mapper.userinfo.UserMapper;
import com.lln.agriculture.user.security.service.CaptchaVerifyService;
import com.lln.agriculture.user.security.service.JwtService;
import com.lln.agriculture.user.service.common.SendMailService;
import com.lln.agriculture.user.service.userinfo.PasswordService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/16 21:45
 */

@Service
public class PasswordServiceImpl implements PasswordService {

    private final UserMapper userMapper;
    private final CaptchaVerifyService captchaVerifyService;
    private final SystemConfig systemConfig;
    private final JwtService jwtService;
    private final SendMailService sendMailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordServiceImpl(UserMapper userMapper, CaptchaVerifyService captchaVerifyService, SystemConfig systemConfig, JwtService jwtService, SendMailService sendMailService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.captchaVerifyService = captchaVerifyService;
        this.systemConfig = systemConfig;
        this.jwtService = jwtService;
        this.sendMailService = sendMailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Message sendFindPasswordMail(String email, HttpServletRequest request, String token) {
        if (!captchaVerifyService.verify(request, token)) {
            throw new BadRequestException("二次验证失败");
        }
        String username = userMapper.selectUsernameByEmail(email);
        if (username == null) {
            throw new BadRequestException("该用户不存在");
        }
        sendMail(username, email);
        return Message.builder()
                .message("密码重置邮件已发送！")
                .build();
    }

    @Override
    public Message changeFindPassword(ChangeFindPassword changeFindPassword) {
        if (!changeFindPassword.getPassword().equals(changeFindPassword.getRePassword())) {
            throw new BadRequestException("两次密码不一致");
        }
        try {
            Jws<Claims> claimsJws = jwtService.parseClaimsJws(changeFindPassword.getToken());
            if (!"find-password".equals(claimsJws.getBody().getSubject())) {
                throw new BadRequestException("请检查您的密码重置链接！");
            }
            String username = (String) claimsJws.getBody().get("username");
            userMapper.changePassword(username, passwordEncoder.encode(changeFindPassword.getPassword()));
            return Message.builder()
                    .message("密码修改成功")
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
    public Message changePassword(ChangePassword changePassword) {
        if (!changePassword.getNewPassword().equals(changePassword.getReNewPassword())) {
            throw new BadRequestException("两次密码不一致");
        }
        String username = CommonUtils.getCurrentUsername();
        String realOldPassword = userMapper.selectUserPassword(username);
        //原密码一定时能查到的，不可能查不到，不需要校验
        if (!passwordEncoder.matches(changePassword.getOldPassword(), realOldPassword)) {
            throw new BadRequestException("原密码错误");
        }
        if (passwordEncoder.matches(changePassword.getNewPassword(), realOldPassword)) {
            throw new BadRequestException("新旧密码不能相同");
        }

        int i = userMapper.changePassword(username, passwordEncoder.encode(changePassword.getNewPassword()));
        if (i == 1) {
            return Message.builder().status(200).message("密码修改成功，请重新登录").build();
        }
        throw new InternalServerErrorException("密码修改失败");
    }


    private void sendMail(String username, String email) {
        Context context = new Context();
        context.setVariable("title", systemConfig.getName() + "密码找回");
        context.setVariable("username", username);
        context.setVariable("systemName", systemConfig.getName());
        context.setVariable("url", String.format(systemConfig.getFindPasswordUrl(),
                jwtService.signNewJwt("find-password", "username", username, systemConfig.getEmailValidTime())));
        sendMailService.sendMail(systemConfig.getName() + "密码找回", "find-password.html", email, context);
    }

}
