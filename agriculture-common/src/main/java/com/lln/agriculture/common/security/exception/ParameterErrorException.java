package com.lln.agriculture.common.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/9 16:12
 */

public class ParameterErrorException extends AuthenticationException {
    public ParameterErrorException(String msg) {
        super(msg);
    }
}
