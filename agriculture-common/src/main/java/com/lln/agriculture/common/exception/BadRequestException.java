package com.lln.agriculture.common.exception;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/17 19:40
 */

public class BadRequestException extends AbstractMessageException {
    public BadRequestException(String message) {
        super(message);
    }
}
