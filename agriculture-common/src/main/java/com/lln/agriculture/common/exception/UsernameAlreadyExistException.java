package com.lln.agriculture.common.exception;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/1 19:48
 */

public class UsernameAlreadyExistException extends AbstractMessageException {
    public UsernameAlreadyExistException(String message) {
        super(message);
    }
}
