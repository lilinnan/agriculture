package com.lln.agriculture.manage.service.logistics.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.order.LogisticsCompany;
import com.lln.agriculture.common.util.PageUtils;
import com.lln.agriculture.manage.mapper.logistics.LogisticsCompanyMapper;
import com.lln.agriculture.manage.service.logistics.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/28 11:09
 */

@Service
public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsCompanyMapper logisticsCompanyMapper;

    @Autowired
    public LogisticsServiceImpl(LogisticsCompanyMapper logisticsCompanyMapper) {
        this.logisticsCompanyMapper = logisticsCompanyMapper;
    }

    @Override
    public Message selectAllLogistics(PageRequest pageRequest) {
        PageUtils.startPage(pageRequest);
        return Message.builder()
                .message("查询成功")
                .data(PageInfo.of(logisticsCompanyMapper.selectAllPage(pageRequest.getKey())))
                .build();
    }

    @Override
    public Message selectAllLogisticsSimple(PageRequest pageRequest) {
        return selectAllLogistics(pageRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateLogistics(LogisticsCompany logisticsCompany) {
        logisticsCompanyMapper.updateByPrimaryKeySelective(logisticsCompany);
        return Message.builder().message("更新成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteLogistics(int id) {
        logisticsCompanyMapper.deleteByPrimaryKey(id);
        return Message.builder().message("删除成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message insertLogistics(LogisticsCompany logisticsCompany) {
        logisticsCompanyMapper.insert(logisticsCompany);
        return Message.builder().message("插入成功").build();
    }
}
