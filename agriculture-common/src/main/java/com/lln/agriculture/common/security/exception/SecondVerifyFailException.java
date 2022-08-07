package com.lln.agriculture.common.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/2 19:04
 */

public class SecondVerifyFailException extends AuthenticationException {

    public SecondVerifyFailException(String msg) {
        super(msg);
    }
}
