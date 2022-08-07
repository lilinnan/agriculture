package com.lln.agriculture.manage.domain.admininfo;

import com.lln.agriculture.manage.domain.authority.AdminAuthority;
import com.lln.agriculture.manage.domain.authority.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 后台管理用户表
 *
 * @author lilinnan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser implements UserDetails {
    /**
     * id
     */
    private Integer id;

    /**
     * 账号
     */

    @NotNull(message = "用户名不能为空")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否未过期
     */
    @Builder.Default
    private boolean accountNonExpired = true;

    /**
     * 是否未锁定
     */
    @NotNull(message = "是否未锁定不能为空")
    @Builder.Default
    private boolean accountNonLocked = true;

    /**
     * 是否凭证未过期
     */
    @Builder.Default
    private boolean credentialsNonExpired = true;

    /**
     * 是否已启用
     */
    @NotNull(message = "是否启用不能为空")
    @Builder.Default
    private boolean enabled = true;

    /**
     * 描述
     */
    @NotNull(message = "用户描述不能为空")
    @NotBlank(message = "用户描述不能为空")
    private String description;


    private String avatar;

    private Set<AdminRole> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> set = new HashSet<>();
        if (roles == null) {
            return set;
        }
        for (AdminRole adminRole : roles) {
            set.add(new SimpleGrantedAuthority("ROLE_" + adminRole.getRole()));
            Set<AdminAuthority> authorities = adminRole.getAuthorities();
            if (authorities == null) {
                continue;
            }
            for (AdminAuthority adminAuthority : authorities) {
                set.add(new SimpleGrantedAuthority(adminAuthority.getAuthority()));
            }
        }
        return set;
    }

}
