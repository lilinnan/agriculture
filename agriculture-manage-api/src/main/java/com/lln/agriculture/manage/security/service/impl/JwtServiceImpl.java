package com.lln.agriculture.manage.security.service.impl;

import com.lln.agriculture.manage.security.config.bean.JwtConfig;
import com.lln.agriculture.manage.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/12 13:20
 */
@Service
public class JwtServiceImpl implements JwtService {

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtServiceImpl(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public String signNewJwt(String subject, String claimName, Object claimValue) {
        long signatureExpirationTime = System.currentTimeMillis() + jwtConfig.getSignatureValidTime();
        return Jwts.builder()
                .setSubject(subject)
                .claim(claimName, claimValue)
                .setIssuedAt(new Date())
                .setExpiration(new Date(signatureExpirationTime))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getKey().getBytes()))
                .compact();
    }

    @Override
    public Jws<Claims> parseClaimsJws(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(jwtConfig.getKey().getBytes()).build();
        return parser.parseClaimsJws(token);
    }
}
