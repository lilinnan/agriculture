package com.lln.agriculture.manage.service.agriculture.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.agriculture.AgricultureEntity;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.util.PageUtils;
import com.lln.agriculture.manage.mapper.agriculture.AgricultureClassifyMapper;
import com.lln.agriculture.manage.mapper.agriculture.AgricultureDetailImgMapper;
import com.lln.agriculture.manage.mapper.agriculture.AgricultureDetailMapper;
import com.lln.agriculture.manage.mapper.agriculture.AgricultureEntityMapper;
import com.lln.agriculture.manage.service.agriculture.AgricultureEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/12 17:05
 */
@Service
public class AgricultureEntityServiceImpl implements AgricultureEntityService {

    private final AgricultureEntityMapper agricultureEntityMapper;
    private final AgricultureClassifyMapper agricultureClassifyMapper;
    private final AgricultureDetailMapper agricultureDetailMapper;
    private final AgricultureDetailImgMapper agricultureDetailImgMapper;

    public AgricultureEntityServiceImpl(AgricultureEntityMapper agricultureEntityMapper, AgricultureClassifyMapper agricultureClassifyMapper, AgricultureDetailMapper agricultureDetailMapper, AgricultureDetailImgMapper agricultureDetailImgMapper) {
        this.agricultureEntityMapper = agricultureEntityMapper;
        this.agricultureClassifyMapper = agricultureClassifyMapper;
        this.agricultureDetailMapper = agricultureDetailMapper;
        this.agricultureDetailImgMapper = agricultureDetailImgMapper;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateEntity(AgricultureEntity agricultureEntity) {
        agricultureEntityMapper.updateByPrimaryKeySelective(agricultureEntity);
        return Message.builder().message("更新成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteEntity(int id) {
        agricultureDetailImgMapper.deleteAllImgByEntityId(id);
        agricultureDetailMapper.deleteByEntityId(id);
        agricultureClassifyMapper.deleteByEntityId(id);
        agricultureEntityMapper.deleteByPrimaryKey(id);
        return Message.builder().message("删除成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message insertEntity(AgricultureEntity agricultureEntity) {

        agricultureEntityMapper.insert(agricultureEntity);
        return Message.builder().message("插入成功").build();
    }
}
