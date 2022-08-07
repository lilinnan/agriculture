package com.lln.agriculture.manage.service.authority.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.util.CommonUtils;
import com.lln.agriculture.common.util.PageUtils;
import com.lln.agriculture.manage.domain.admininfo.AdminUser;
import com.lln.agriculture.manage.domain.authority.AdminRole;
import com.lln.agriculture.manage.mapper.admininfo.AdminUserMapper;
import com.lln.agriculture.manage.service.authority.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/27 18:06
 */


@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserServiceImpl(AdminUserMapper adminUserMapper, PasswordEncoder passwordEncoder) {
        this.adminUserMapper = adminUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Message getAllUser(PageRequest pageRequest) {
        PageUtils.startPage(pageRequest);
        return Message.builder()
                .status(200)
                .message("查询成功")
                .data(PageInfo.of(adminUserMapper.selectAllPage(pageRequest.getKey())))
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message insertUser(AdminUser adminUser) {
        if (adminUserMapper.isExist(adminUser.getUsername())) {
            throw new BadRequestException("用户名存在");
        }
        if (StringUtils.isEmpty(adminUser.getPassword())) {
            throw new BadRequestException("请输入新用户的密码");
        }
        adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        adminUserMapper.insert(adminUser);
        int userId = adminUser.getId();
        Set<AdminRole> roles = adminUser.getRoles();
        if (roles != null) {
            roles.forEach(adminRole -> adminUserMapper.insertAdminUserRole(userId, adminRole.getId()));
        }
        return Message.builder().message("添加成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateUser(AdminUser adminUser) {
        Integer userId = adminUser.getId();
        if (userId == null) {
            throw new BadRequestException("无用户ID，无法执行修改操作！");
        }
        String username = adminUserMapper.selectUsernameById(userId);


        //修改角色
        List<Integer> originalRolesList = adminUserMapper.selectOriginalRoles(userId);
        if (adminUser.getRoles() == null) {
            adminUser.setRoles(Collections.emptySet());
        }
        List<Integer> newRolesList = adminUser.getRoles().stream().map(AdminRole::getId).collect(Collectors.toList());


        if ("admin".equals(username)) {
            String currentUsername = CommonUtils.getCurrentUsername();
            //在操作管理员
            //非管理员账户提示这一句
            if (!"admin".equals(currentUsername)) {
                throw new BadRequestException("请勿操作admin账户");
            }
            boolean flag = false;
            if (originalRolesList.size() != newRolesList.size()) {
                flag = true;
            }
            if (!flag) {
                for (int id : originalRolesList) {
                    if (!newRolesList.contains(id)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                throw new BadRequestException("请勿操作你的角色信息");
            }

            if (!adminUser.isAccountNonLocked() || !adminUser.isEnabled()) {
                throw new BadRequestException("请勿操作你的状态信息");
            }
            String des = adminUserMapper.selectDescriptionByUsername("admin");
            if (!des.equals(adminUser.getDescription())) {
                throw new BadRequestException("请勿修改您的用户描述");
            }
        }


        if (!StringUtils.isEmpty(adminUser.getPassword())) {
            //存在密码就进行加密
            adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        }

        //修改用户信息
        adminUserMapper.updateByIdSelective(adminUser);


        originalRolesList.forEach(roleId -> {
            if (!newRolesList.contains(roleId)) {
                adminUserMapper.deleteAdminUserRole(userId, roleId);
            }
        });

        newRolesList.forEach(roleId -> {
            if (!originalRolesList.contains(roleId)) {
                adminUserMapper.insertAdminUserRole(userId, roleId);
            }
        });
        return Message.builder().message("修改成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteUser(int id) {
        String currentUsername = CommonUtils.getCurrentUsername();
        String deleteUsername = adminUserMapper.selectUsernameById(id);
        if (currentUsername.equals(deleteUsername)) {
            throw new BadRequestException("你不能删除自己！");
        }
        if ("admin".equals(deleteUsername)) {
            throw new BadRequestException("admin账户无法删除");
        }
        try {
            adminUserMapper.deleteAllAdminUserRole(id);
            adminUserMapper.deleteUser(id);
        } catch (Exception e) {
            throw new BadRequestException("删除失败");
        }
        return Message.builder().message("删除成功").build();
    }
}
