package com.lln.agriculture.user.service.agriculture.impl;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.user.mapper.agriculture.AgricultureClassifyMapper;
import com.lln.agriculture.user.service.agriculture.AgricultureClassifyService;
import org.springframework.stereotype.Service;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/16 10:22
 */

@Service
public class AgricultureClassifyServiceImpl implements AgricultureClassifyService {

    private final AgricultureClassifyMapper agricultureClassifyMapper;

    public AgricultureClassifyServiceImpl(AgricultureClassifyMapper agricultureClassifyMapper) {
        this.agricultureClassifyMapper = agricultureClassifyMapper;
    }

    @Override
    public Message selectAllClassifyByEntityId(int entityId) {
        return Message.builder()
                .message("查询成功")
                .data(agricultureClassifyMapper.selectAllClassifyByEntityId(entityId))
                .build();
    }

}
