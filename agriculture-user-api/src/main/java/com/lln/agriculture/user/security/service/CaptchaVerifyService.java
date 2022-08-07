package com.lln.agriculture.user.security.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/4 9:09
 */

public interface CaptchaVerifyService {

    /**
     * 验证码二次校验
     *
     * @param request 接收到的请求
     * @param token   携带的token
     * @return 是否通过验证
     */
    boolean verify(HttpServletRequest request, String token);
}
