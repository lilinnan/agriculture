package com.lln.agriculture.common.exception;

/**
 * 服务器异常
 *
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/9 20:03
 */

public class InternalServerErrorException extends AbstractMessageException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
