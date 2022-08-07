package com.lln.agriculture.manage.service.userinfo;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.userinfo.User;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/27 17:10
 */

public interface UserService {
    Message selectAllUserPage(PageRequest pageRequest);

    Message lockUser(User user);
}
