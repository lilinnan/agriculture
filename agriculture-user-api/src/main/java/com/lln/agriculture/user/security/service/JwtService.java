package com.lln.agriculture.user.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/12 13:05
 */

public interface JwtService {

    /**
     * 生成签名
     * 仅可设置一个自定义参数
     *
     * @param subject    主题
     * @param claimName  claim名称
     * @param claimValue claim值
     * @return token
     */
    String signNewJwt(String subject, String claimName, Object claimValue);


    String signNewJwt(String subject, String claimName, Object claimValue, long time);


    /**
     * 解析签名
     *
     * @param token 被解析的token
     * @return 解析后的结果
     */
    Jws<Claims> parseClaimsJws(String token) throws Exception;
}
