package com.lln.agriculture.manage.service.agriculture.impl;

import com.lln.agriculture.common.agriculture.AgricultureDetail;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.manage.mapper.agriculture.AgricultureDetailImgMapper;
import com.lln.agriculture.manage.mapper.agriculture.AgricultureDetailMapper;
import com.lln.agriculture.manage.service.agriculture.AgricultureDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/12/13 16:05
 */
@Service
public class AgricultureDetailServiceImpl implements AgricultureDetailService {
    private final AgricultureDetailMapper agricultureDetailMapper;
    private final AgricultureDetailImgMapper agricultureDetailImgMapper;

    public AgricultureDetailServiceImpl(AgricultureDetailMapper agricultureDetailMapper, AgricultureDetailImgMapper agricultureDetailImgMapper) {
        this.agricultureDetailMapper = agricultureDetailMapper;
        this.agricultureDetailImgMapper = agricultureDetailImgMapper;
    }


    @Override
    public Message selectDetail(int detailId) {
        return Message.builder().data(agricultureDetailMapper.selectDetailById(detailId)).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateDetail(AgricultureDetail agricultureDetail) {
        //无法修改分类数据
        agricultureDetail.setClassifyId(null);
        agricultureDetailMapper.updateByPrimaryKeySelective(agricultureDetail);
        agricultureDetailImgMapper.deleteAllImgByDetailId(agricultureDetail.getId());
        agricultureDetail.getDetailImgs().forEach(agricultureDetailImg -> {
            agricultureDetailImg.setDetailId(agricultureDetail.getId());
            agricultureDetailImgMapper.insert(agricultureDetailImg);
        });
        return Message.builder().message("修改成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteDetail(int detailId) {
        agricultureDetailImgMapper.deleteAllImgByDetailId(detailId);
        agricultureDetailMapper.deleteByPrimaryKey(detailId);
        return Message.builder().message("删除成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message insertDetail(AgricultureDetail agricultureDetail) {
        agricultureDetailMapper.insert(agricultureDetail);
        Integer id = agricultureDetail.getId();
        agricultureDetail.getDetailImgs().forEach(agricultureDetailImg -> {
            agricultureDetailImg.setDetailId(id);
            agricultureDetailImgMapper.insert(agricultureDetailImg);
        });
        return Message.builder().message("插入成功").build();
    }
}
