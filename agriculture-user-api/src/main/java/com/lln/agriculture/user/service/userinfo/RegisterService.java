package com.lln.agriculture.user.service.userinfo;

import com.lln.agriculture.common.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/16 14:17
 */

public interface RegisterService {

    /**
     * 注册用户
     *
     * @param registerBean bean
     * @return 注册信息
     */
    Message register(RegisterBean registerBean, HttpServletRequest request);

    Message mailCheck(String jwt);

    Message resendMail(String email, HttpServletRequest request, String token);


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RegisterBean {
        @NotNull(message = "用户名不能为空")
        @NotBlank(message = "用户名不能为空")
        @Pattern(regexp = "[\\w]{5,16}", message = "用户名必须由数字、字母或下划线组成，长度在5~16位之间")
        private String username;
        @NotNull(message = "密码不能为空")
        @NotBlank(message = "密码不能为空")
        @Pattern(regexp = "[\\w]{6,16}", message = "密码必须由数字、字母或下划线组成，长度在6~16位之间")
        private String password;
        @NotNull(message = "重复密码不能为空")
        @NotBlank(message = "重复密码不能为空")
        private String rePassword;
        @NotNull(message = "邮件不能为空")
        @NotBlank(message = "邮件不能为空")
        @Pattern(regexp = "(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))", message = "请输入正确的邮箱地址")
        private String email;
        @NotNull(message = "请进行二次验证")
        @NotBlank(message = "请进行二次验证")
        private String token;
    }
}
