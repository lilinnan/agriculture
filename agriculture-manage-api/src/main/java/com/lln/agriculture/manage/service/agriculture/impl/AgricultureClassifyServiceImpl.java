package com.lln.agriculture.manage.service.agriculture.impl;

import com.lln.agriculture.common.agriculture.AgricultureClassify;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.manage.mapper.agriculture.AgricultureClassifyMapper;
import com.lln.agriculture.manage.mapper.agriculture.AgricultureDetailImgMapper;
import com.lln.agriculture.manage.mapper.agriculture.AgricultureDetailMapper;
import com.lln.agriculture.manage.service.agriculture.AgricultureClassifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/16 10:22
 */

@Service
public class AgricultureClassifyServiceImpl implements AgricultureClassifyService {

    private final AgricultureClassifyMapper agricultureClassifyMapper;
    private final AgricultureDetailMapper agricultureDetailMapper;
    private final AgricultureDetailImgMapper agricultureDetailImgMapper;

    public AgricultureClassifyServiceImpl(AgricultureClassifyMapper agricultureClassifyMapper, AgricultureDetailMapper agricultureDetailMapper, AgricultureDetailImgMapper agricultureDetailImgMapper) {
        this.agricultureClassifyMapper = agricultureClassifyMapper;
        this.agricultureDetailMapper = agricultureDetailMapper;
        this.agricultureDetailImgMapper = agricultureDetailImgMapper;
    }

    @Override
    public Message selectAllClassifyByEntityId(int entityId) {
        return Message.builder()
                .message("查询成功")
                .data(agricultureClassifyMapper.selectAllClassifyByEntityId(entityId))
                .build();
    }

    @Override
    public Message updateClassify(AgricultureClassify agricultureClassify) {
        agricultureClassifyMapper.updateByPrimaryKeySelective(agricultureClassify);
        return Message.builder().message("更新成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteClassify(int id) {
        agricultureDetailImgMapper.deleteAllImgByClassifyId(id);
        agricultureDetailMapper.deleteByClassifyId(id);
        agricultureClassifyMapper.deleteByPrimaryKey(id);
        return Message.builder().message("删除成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message insertClassify(AgricultureClassify agricultureClassify) {
        agricultureClassifyMapper.insert(agricultureClassify);
        return Message.builder().message("插入成功").build();
    }

}
