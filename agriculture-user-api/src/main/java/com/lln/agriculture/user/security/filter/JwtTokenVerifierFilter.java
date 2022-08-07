package com.lln.agriculture.user.security.filter;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.util.CommonUtils;
import com.lln.agriculture.user.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/2 19:28
 */

@Component
public class JwtTokenVerifierFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Autowired
    public JwtTokenVerifierFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || token.trim().length() == 0) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            //解析jwt
            Jws<Claims> claimsJws = jwtService.parseClaimsJws(token);
            //获取到body
            Claims body = claimsJws.getBody();

            String subject = body.getSubject();
            if (!"simple".equals(subject)) {
                throw new Exception();
            }
            //获取到用户名
            String username = body.get("username", String.class);
            //创建令牌
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username, "", Collections.emptyList());
            //放到SpringSecurity上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            CommonUtils.print(response, Message.builder().status(401).message("身份信息已过期，请重新登录").data("tokenInvalid").build());
            return;
        }
        filterChain.doFilter(request, response);
    }

}
