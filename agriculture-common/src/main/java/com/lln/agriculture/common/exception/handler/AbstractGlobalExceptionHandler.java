package com.lln.agriculture.common.exception.handler;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.exception.InternalServerErrorException;
import com.lln.agriculture.common.exception.UsernameAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import java.sql.SQLSyntaxErrorException;
import java.util.Objects;


/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/1 19:45
 */
public abstract class AbstractGlobalExceptionHandler {

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public Message usernameAlreadyExistExceptionHandler() {
        return Message.builder().status(500).message("用户名已存在").build();
    }


    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Message httpRequestMethodNotSupportedException() {
        return Message.builder().status(HttpStatus.METHOD_NOT_ALLOWED.value()).message("方法不允许").build();
    }


    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MultipartException.class)
    public Message multipartException(MultipartException exception) {
        return Message.builder().status(500).message("文件错误").build();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Message bindException(BindException bindException) {
        String message = "未知错误";
        try {
            message = Objects.requireNonNull(bindException.getFieldError()).getDefaultMessage();
        } catch (Exception ignored) {
        }
        return Message.builder().status(400).message(message).build();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Message methodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        String message = "未知错误";
        try {
            message = Objects.requireNonNull(methodArgumentNotValidException.getBindingResult().getFieldError()).getDefaultMessage();
        } catch (Exception ignored) {
        }
        return Message.builder().status(400).message(message).build();
    }


    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public Message sqlSyntaxErrorException(SQLSyntaxErrorException sqlSyntaxErrorException) {
        sqlSyntaxErrorException.printStackTrace();
        return Message.builder().status(500).message("服务器开小差啦").build();
    }


    /**
     * 服务器内部错误 500
     *
     * @param internalServerErrorException 异常
     * @return 信息
     */
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public Message internalServerErrorException(InternalServerErrorException internalServerErrorException) {
        return Message
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(internalServerErrorException.getMessage())
                .build();
    }


    /**
     * 错误的请求
     *
     * @param badRequestException 错误的请求异常
     * @return 信息
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Message badRequestException(BadRequestException badRequestException) {
        return Message
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(badRequestException.getMessage())
                .build();
    }
}
