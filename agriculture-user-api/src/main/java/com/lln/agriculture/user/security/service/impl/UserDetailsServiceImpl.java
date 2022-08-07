package com.lln.agriculture.user.security.service.impl;

import com.lln.agriculture.common.domain.userinfo.User;
import com.lln.agriculture.user.mapper.userinfo.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/2 16:23
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " is not exist");
        }
        return user;
    }
}
