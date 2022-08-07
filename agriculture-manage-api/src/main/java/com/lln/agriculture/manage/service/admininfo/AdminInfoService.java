package com.lln.agriculture.manage.service.admininfo;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.manage.domain.admininfo.ChangePassword;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/17 19:17
 */

public interface AdminInfoService {


    /**
     * 修改密码
     *
     * @param changePassword 修改密码对象
     * @return 一条信息
     */
    Message changePassword(ChangePassword changePassword);


    Message getAvatar();


    Message getAvatar(String username);
}
