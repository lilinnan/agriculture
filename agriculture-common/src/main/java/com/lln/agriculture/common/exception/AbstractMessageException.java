package com.lln.agriculture.common.exception;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/9 20:04
 */

public abstract class AbstractMessageException extends RuntimeException {
    private final String message;

    public AbstractMessageException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
