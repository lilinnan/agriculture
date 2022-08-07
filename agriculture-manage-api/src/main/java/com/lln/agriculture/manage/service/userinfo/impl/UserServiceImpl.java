package com.lln.agriculture.manage.service.userinfo.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.userinfo.User;
import com.lln.agriculture.common.util.PageUtils;
import com.lln.agriculture.manage.mapper.userinfo.UserMapper;
import com.lln.agriculture.manage.service.userinfo.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/27 17:11
 */

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Message selectAllUserPage(PageRequest pageRequest) {
        PageUtils.startPage(pageRequest);
        return Message.builder()
                .message("查询成功")
                .data(PageInfo.of(userMapper.selectAllUserPage(pageRequest.getKey())))
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message lockUser(User user) {
        userMapper.lockUser(user);
        return Message.builder().build();
    }
}
