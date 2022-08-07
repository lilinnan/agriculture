package com.lln.agriculture.manage.mapper.userinfo;

import com.lln.agriculture.common.domain.userinfo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/27 17:01
 */

@Mapper
public interface UserMapper {

    List<User> selectAllUserPage(String key);

    int lockUser(User user);
}
