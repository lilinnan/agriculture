package com.lln.agriculture.manage.service.authority.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.manage.mapper.authority.AdminAuthorityMapper;
import com.lln.agriculture.manage.service.authority.AdminAuthorityService;
import com.lln.agriculture.common.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/21 18:53
 */

@Service
public class AdminAuthorityServiceImpl implements AdminAuthorityService {

    private final AdminAuthorityMapper adminAuthorityMapper;

    @Autowired
    public AdminAuthorityServiceImpl(AdminAuthorityMapper adminAuthorityMapper) {
        this.adminAuthorityMapper = adminAuthorityMapper;
    }


    @Override
    public Message selectAllAuthority(PageRequest pageRequest) {
        PageUtils.startPage(pageRequest);
        return Message
                .builder()
                .status(200)
                .message("查询成功")
                .data(PageInfo.of(adminAuthorityMapper.selectAllPage(pageRequest.getKey())))
                .build();
    }

    @Override
    public Message selectAllAuthoritySimple(PageRequest pageRequest) {
        return selectAllAuthority(pageRequest);
    }

}
