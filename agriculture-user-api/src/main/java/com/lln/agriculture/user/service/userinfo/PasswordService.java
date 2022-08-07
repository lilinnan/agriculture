package com.lln.agriculture.user.service.userinfo;

import com.lln.agriculture.common.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/16 21:43
 */

public interface PasswordService {

    Message sendFindPasswordMail(String email, HttpServletRequest request, String token);

    Message changeFindPassword(ChangeFindPassword changeFindPassword);

    Message changePassword(ChangePassword changePassword);


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ChangeFindPassword {
        @NotNull(message = "密码不能为空")
        @NotBlank(message = "密码不能为空")
        @Pattern(regexp = "[\\w]{6,16}", message = "新密码必须由数字、字母或下划线组成，长度在6~16位之间")
        private String password;
        @NotNull(message = "重复密码不能为空")
        @NotBlank(message = "重复密码不能为空")
        private String rePassword;
        @NotNull(message = "请进行二次验证")
        @NotBlank(message = "请进行二次验证")
        private String token;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class ChangePassword {

        @NotNull(message = "原密码不能为空")
        @NotBlank(message = "原密码不能为空")
        private String oldPassword;

        @NotNull(message = "新密码不能为空")
        @NotBlank(message = "新密码不能为空")
        @Pattern(regexp = "[\\w]{6,16}", message = "新密码必须由数字、字母或下划线组成，长度在6~16位之间")
        private String newPassword;

        @NotNull(message = "重复密码不能为空")
        @NotBlank(message = "重复密码不能为空")
        @Pattern(regexp = "[\\w]{6,16}", message = "重复密码必须由数字、字母或下划线组成，长度在6~16位之间")
        private String reNewPassword;


    }
}
