package com.lln.agriculture.user.service.agriculture.impl;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.user.mapper.agriculture.AgricultureDetailMapper;
import com.lln.agriculture.user.service.agriculture.AgricultureDetailService;
import org.springframework.stereotype.Service;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/12/13 16:05
 */
@Service
public class AgricultureDetailServiceImpl implements AgricultureDetailService {
    private final AgricultureDetailMapper agricultureDetailMapper;

    public AgricultureDetailServiceImpl(AgricultureDetailMapper agricultureDetailMapper) {
        this.agricultureDetailMapper = agricultureDetailMapper;
    }


    @Override
    public Message selectDetail(int detailId) {
        return Message.builder().data(agricultureDetailMapper.selectDetailById(detailId)).build();
    }
}
