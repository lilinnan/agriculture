package com.lln.agriculture.manage.domain.authority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 后台管理角色表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRole {
    /**
     * id
     */
    private Integer id;

    /**
     * 角色名称
     */

    @NotBlank(message = "角色名称不能为空")
    @NotNull(message = "角色名称不能为空")
    private String role;

    @NotBlank(message = "角色描述不能为空")
    @NotNull(message = "角色描述不能为空")
    private String description;


    private Set<AdminAuthority> authorities;


    public AdminRole(Integer id) {
        this.id = id;
    }
}