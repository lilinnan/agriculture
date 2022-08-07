package com.lln.agriculture.manage.service.authority.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.util.PageUtils;
import com.lln.agriculture.manage.domain.authority.AdminAuthority;
import com.lln.agriculture.manage.domain.authority.AdminRole;
import com.lln.agriculture.manage.mapper.authority.AdminRoleMapper;
import com.lln.agriculture.manage.service.authority.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/24 19:26
 */

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    private final AdminRoleMapper adminRoleMapper;

    @Autowired
    public AdminRoleServiceImpl(AdminRoleMapper adminRoleMapper) {
        this.adminRoleMapper = adminRoleMapper;
    }


    @Override
    public Message selectAllRoleSimple(PageRequest pageRequest) {
        PageUtils.startPage(pageRequest);
        return Message.builder()
                .status(200)
                .message("查询成功")
                .data(PageInfo.of(adminRoleMapper.selectAllSimple(pageRequest.getKey())))
                .build();
    }

    @Override
    public Message selectAllRole(PageRequest pageRequest) {
        PageUtils.startPage(pageRequest);
        return Message
                .builder()
                .status(200)
                .message("查询成功")
                .data(PageInfo.of(adminRoleMapper.selectAllPage(pageRequest.getKey())))
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message insertRole(AdminRole adminRole) {
        if (adminRoleMapper.isExist(adminRole.getRole())) {
            throw new BadRequestException("角色已存在");
        }
        //插入角色
        adminRoleMapper.insert(adminRole);
        //接着插入关系
        int roleId = adminRole.getId();
        Set<AdminAuthority> authorities = adminRole.getAuthorities();
        if (authorities != null) {
            authorities.forEach(adminAuthority -> adminRoleMapper.insertAdminRoleAuthority(roleId, adminAuthority.getId()));
        }
        return Message.builder().message("添加成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateRole(AdminRole adminRole) {
        Integer roleId = adminRole.getId();
        if (roleId == null) {
            throw new BadRequestException("无角色ID，无法执行修改操作！");
        }
        if ("ADMIN".equals(adminRoleMapper.selectRoleNameByRoleId(roleId))) {
            throw new BadRequestException("无法修改超级管理员的角色信息");
        }
        //获取新角色名称的id，查询之前是否存在
        Integer oldRoleId = adminRoleMapper.selectIdByRoleName(adminRole.getRole());
        if (oldRoleId != null && !oldRoleId.equals(roleId)) {
            //违反约束条件
            throw new BadRequestException("该角色已存在，无法修改为此角色");
        }

        //修改角色信息
        adminRoleMapper.updateByIdSelective(adminRole);
        //修改权限
        List<Integer> originalAuthoritiesList = adminRoleMapper.selectOriginalAuthorities(roleId);
        if (adminRole.getAuthorities() == null) {
            adminRole.setAuthorities(Collections.emptySet());
        }
        List<Integer> newAuthoritiesList = adminRole.getAuthorities().stream().map(AdminAuthority::getId).collect(Collectors.toList());
        originalAuthoritiesList.forEach(authorityId -> {
            if (!newAuthoritiesList.contains(authorityId)) {
                adminRoleMapper.deleteAdminRoleAuthority(roleId, authorityId);
            }
        });

        newAuthoritiesList.forEach(authorityId -> {
            if (!originalAuthoritiesList.contains(authorityId)) {
                adminRoleMapper.insertAdminRoleAuthority(roleId, authorityId);
            }
        });
        return Message.builder().message("修改成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteRole(int id) {
        try {
            adminRoleMapper.deleteAllAdminRoleAuthority(id);
            adminRoleMapper.deleteRole(id);
        } catch (Exception e) {
            throw new BadRequestException("删除失败（因为存在为此角色的用户）");
        }
        return Message.builder().message("删除成功").build();
    }
}
