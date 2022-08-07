package com.lln.agriculture.common.exception;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/16 17:21
 */

public class EmailExpiredException extends AbstractMessageException {
    public EmailExpiredException(String message) {
        super(message);
    }
}
