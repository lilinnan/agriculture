package com.lln.agriculture.user.security.handler;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.util.CommonUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/15 11:06
 */
@Component
public class SimpleAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        System.out.println(132456654);
        CommonUtils.print(response, Message.builder().status(403).message("您没有权限来执行此操作").build());
    }
}
