package com.lln.agriculture.manage.security.filter;

import com.google.gson.Gson;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.security.exception.ParameterErrorException;
import com.lln.agriculture.common.security.exception.SecondVerifyFailException;
import com.lln.agriculture.common.security.exception.UsernameOrPasswordNotCompleteException;
import com.lln.agriculture.manage.security.service.CaptchaVerifyService;
import com.lln.agriculture.manage.security.service.JwtService;
import com.lln.agriculture.common.util.CommonUtils;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/2 16:31
 */

@Component
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CaptchaVerifyService captchaVerifyService;
    private final JwtService jwtService;
    private final Gson gson;

    @Autowired
    public JwtUsernamePasswordAuthenticationFilter(CaptchaVerifyService captchaVerifyService, JwtService jwtService, Gson gson) {
        this.captchaVerifyService = captchaVerifyService;
        this.jwtService = jwtService;
        this.gson = gson;
        setFilterProcessesUrl("/v1/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User user;
        String token;
        try {
            //读取传入的用户名和密码
            user = gson.fromJson(new InputStreamReader(request.getInputStream()), User.class);
            //获取验证码带来的token
            token = user.getToken();
        } catch (Exception e) {
            throw new ParameterErrorException("参数错误");
        }
        //进行二次验证
        boolean verify = captchaVerifyService.verify(request, token);
        if (!verify) {
            throw new SecondVerifyFailException("二次验证失败");
        }

        //从父类复制来的代码，进行了部分修改
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String username = user.getUsername();
        String password = user.getPassword();

        if (username == null || username.trim().length() == 0) {
            throw new UsernameOrPasswordNotCompleteException("未输入用户名");
        }

        if (password == null || password.length() == 0) {
            throw new UsernameOrPasswordNotCompleteException("未输入密码");
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authResult);

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        List<String> list = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        //获取jwt签名
        String token = jwtService.signNewJwt(authResult.getName(),
                "authorities",
                list);

        //返回到前端
        CommonUtils.print(response, Message.builder().status(200).message("登录成功").data(token).build());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException {

        if (failed instanceof BadCredentialsException) {
            CommonUtils.print(response, Message.builder().status(401).message("用户名或密码错误").build());
            return;
        }
        CommonUtils.print(response, Message.builder().status(401).message(failed.getMessage()).build());
    }


    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String username;
        private String password;
        private String token;
    }

}

