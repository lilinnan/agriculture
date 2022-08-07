package com.lln.agriculture.user.service.agriculture.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.util.PageUtils;
import com.lln.agriculture.user.mapper.agriculture.AgricultureEntityMapper;
import com.lln.agriculture.user.service.agriculture.AgricultureEntityService;
import org.springframework.stereotype.Service;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/12 17:05
 */
@Service
public class AgricultureEntityServiceImpl implements AgricultureEntityService {

    private final AgricultureEntityMapper agricultureEntityMapper;

    public AgricultureEntityServiceImpl(AgricultureEntityMapper agricultureEntityMapper) {
        this.agricultureEntityMapper = agricultureEntityMapper;
    }

    @Override
    public Message selectAllEntity(PageRequest pageRequest, boolean plant) {
        PageUtils.startPage(pageRequest);
        return Message
                .builder()
                .message("查询成功")
                .data(PageInfo.of(agricultureEntityMapper.selectAllEntityPage(plant, pageRequest.getKey())))
                .build();
    }
}
