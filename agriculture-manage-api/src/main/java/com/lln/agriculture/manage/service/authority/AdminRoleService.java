package com.lln.agriculture.manage.service.authority;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.manage.domain.authority.AdminRole;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/24 19:25
 */

public interface AdminRoleService {


    /**
     * 查询简易信息
     *
     * @return 信息
     */
    Message selectAllRoleSimple(PageRequest pageRequest);

    Message selectAllRole(PageRequest pageRequest);

    Message insertRole(AdminRole adminRole);

    Message updateRole(AdminRole adminRole);

    Message deleteRole(int id);
}
