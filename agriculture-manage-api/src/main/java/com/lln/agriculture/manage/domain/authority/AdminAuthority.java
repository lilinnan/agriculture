package com.lln.agriculture.manage.domain.authority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台管理权限表
 *
 * @author lilinnan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAuthority {
    /**
     * id
     */
    private Integer id;

    /**
     * 权限名称
     */
    private String authority;

    private String description;

    public AdminAuthority(Integer id) {
        this.id = id;
    }
}