package com.lln.agriculture.manage.service.authority;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.manage.domain.admininfo.AdminUser;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/27 17:18
 */

public interface AdminUserService {


    Message getAllUser(PageRequest pageRequest);

    Message insertUser(AdminUser adminUser);

    Message updateUser(AdminUser adminUser);

    Message deleteUser(int id);

}
