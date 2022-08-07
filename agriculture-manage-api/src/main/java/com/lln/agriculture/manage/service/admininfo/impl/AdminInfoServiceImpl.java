package com.lln.agriculture.manage.service.admininfo.impl;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.manage.domain.admininfo.ChangePassword;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.exception.InternalServerErrorException;
import com.lln.agriculture.manage.mapper.admininfo.AdminUserMapper;
import com.lln.agriculture.manage.service.admininfo.AdminInfoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/17 19:17
 */

@Service
public class AdminInfoServiceImpl implements AdminInfoService {
    private final AdminUserMapper adminUserMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminInfoServiceImpl(AdminUserMapper adminUserMapper, PasswordEncoder passwordEncoder) {
        this.adminUserMapper = adminUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Message changePassword(ChangePassword changePassword) {
        if (!changePassword.getNewPassword().equals(changePassword.getReNewPassword())) {
            throw new BadRequestException("两次密码不一致");
        }
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String realOldPassword = adminUserMapper.selectAdminUserPassword(username);
        //原密码一定时能查到的，不可能查不到，不需要校验

        if (!passwordEncoder.matches(changePassword.getOldPassword(), realOldPassword)) {
            throw new BadRequestException("原密码错误");
        }
        if (passwordEncoder.matches(changePassword.getNewPassword(), realOldPassword)) {
            throw new BadRequestException("新旧密码不能相同");
        }

        int i = adminUserMapper.changePassword(username, passwordEncoder.encode(changePassword.getNewPassword()));
        if (i == 1) {
            return Message.builder().status(200).message("密码修改成功，请重新登录").build();
        }
        throw new InternalServerErrorException("密码修改失败");

    }

    @Override
    public Message getAvatar() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getAvatar(username);
    }

    @Override
    public Message getAvatar(String username) {
        Map<String, String> data = new HashMap<>(2);
        String avatar = adminUserMapper.selectAvatar(username);
        data.put("username", username);
        data.put("avatar", avatar);
        return Message.builder()
                .message("查询成功")
                .data(data)
                .build();
    }
}
