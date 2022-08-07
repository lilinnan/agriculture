package com.lln.agriculture.manage.domain.admininfo;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/17 18:46
 */

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {

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
