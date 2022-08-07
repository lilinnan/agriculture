package com.lln.agriculture.manage.service.item.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.manage.mapper.item.SkuMapper;
import com.lln.agriculture.manage.mapper.item.SpuImgMapper;
import com.lln.agriculture.manage.mapper.item.SpuInfoMapper;
import com.lln.agriculture.manage.mapper.item.SpuMapper;
import com.lln.agriculture.manage.service.item.SpuRecycleBinService;
import com.lln.agriculture.common.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/15 9:46
 */

@Service
public class SpuRecycleBinServiceImpl implements SpuRecycleBinService {

    private final SpuMapper spuMapper;
    private final SpuImgMapper spuImgMapper;
    private final SpuInfoMapper spuInfoMapper;
    private final SkuMapper skuMapper;

    @Autowired
    public SpuRecycleBinServiceImpl(SpuMapper spuMapper, SpuImgMapper spuImgMapper, SpuInfoMapper spuInfoMapper, SkuMapper skuMapper) {
        this.spuMapper = spuMapper;
        this.spuImgMapper = spuImgMapper;
        this.spuInfoMapper = spuInfoMapper;
        this.skuMapper = skuMapper;
    }


    @Override
    public Message selectAllDeletedSpu(PageRequest pageRequest) {
        PageUtils.startPage(pageRequest);
        return Message.builder()
                .message("查询成功")
                .data(PageInfo.of(spuMapper.selectAllDeletedSpu(pageRequest.getKey())))
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message restoreDeletedSpu(int id) {
        spuMapper.restoreDeletedSpu(id);
        return Message
                .builder()
                .message("恢复成功")
                .build();
    }

    @Override
    public Message realDeleteSpu(int id) {
        //删除分类信息
        spuMapper.deleteSpuCategoryBySpuId(id);
        //删除图片信息
        spuImgMapper.deleteImgBySpuId(id);
        //删除基本信息
        spuInfoMapper.deleteInfoBySpuId(id);
        //删除sku
        skuMapper.deleteSkuBySpuId(id);
        //删除商品本身
        spuMapper.realDeleteSpu(id);
        return Message
                .builder()
                .message("删除成功")
                .build();
    }


}
