package com.lln.agriculture.common.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/12 22:54
 */

public class UsernameOrPasswordNotCompleteException extends AuthenticationException {

    public UsernameOrPasswordNotCompleteException(String msg) {
        super(msg);
    }
}
