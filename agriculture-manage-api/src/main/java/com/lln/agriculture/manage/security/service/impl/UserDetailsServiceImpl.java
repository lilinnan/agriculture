package com.lln.agriculture.manage.security.service.impl;

import com.lln.agriculture.manage.domain.admininfo.AdminUser;
import com.lln.agriculture.manage.mapper.admininfo.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final AdminUserMapper adminUserMapper;

    @Autowired
    public UserDetailsServiceImpl(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser user = adminUserMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " is not exist");
        }
        return user;
    }
}
