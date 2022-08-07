package com.lln.agriculture.manage.service.item.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.item.Category;
import com.lln.agriculture.common.domain.item.Sku;
import com.lln.agriculture.common.domain.item.Spu;
import com.lln.agriculture.common.domain.item.SpuImg;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.util.PageUtils;
import com.lln.agriculture.manage.mapper.item.SkuMapper;
import com.lln.agriculture.manage.mapper.item.SpuImgMapper;
import com.lln.agriculture.manage.mapper.item.SpuInfoMapper;
import com.lln.agriculture.manage.mapper.item.SpuMapper;
import com.lln.agriculture.manage.service.item.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/12 9:19
 */
@Service
public class SpuServiceImpl implements SpuService {

    private final SpuMapper spuMapper;
    private final SpuImgMapper spuImgMapper;
    private final SpuInfoMapper spuInfoMapper;
    private final SkuMapper skuMapper;

    @Autowired
    public SpuServiceImpl(SpuMapper spuMapper, SpuImgMapper spuImgMapper, SpuInfoMapper spuInfoMapper, SkuMapper skuMapper) {
        this.spuMapper = spuMapper;
        this.spuImgMapper = spuImgMapper;
        this.spuInfoMapper = spuInfoMapper;
        this.skuMapper = skuMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message insertSpu(Spu spu) {
        spu.setDelete(false);
        //插入商品基本信息
        spuMapper.insertSpu(spu);
        //插入其他信息
        insertOtherData(spu, true);
        return Message.builder()
                .message("添加成功")
                .build();
    }

    @Override
    public Message selectAllSpu(PageRequest pageRequest) {
        return null;
    }

    @Override
    public Message selectAllSpuSimple(PageRequest pageRequest) {
        PageUtils.startPage(pageRequest);
        return Message.builder()
                .message("查询成功")
                .data(PageInfo.of(spuMapper.selectAllSpuPage(pageRequest.getKey())))
                .build();
    }

    @Override
    public Message selectOneSpu(int spuId) {
        return Message.builder()
                .message("查询成功")
                .data(spuMapper.selectSpuById(spuId))
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateSaleable(Spu spu) {
        spuMapper.updateSpuSaleable(spu);
        return Message.builder().build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteSpu(int id) {
        return Message.builder()
                .message("删除成功")
                .data(spuMapper.deleteSpuById(id))
                .build();
    }

    @Override
    public Message realDeleteSpu(int id) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateSpu(Spu spu) {
        Integer spuId = spu.getId();
        if (spuId == null) {
            throw new BadRequestException("错误的请求，未找到商品Id");
        }
        //更新基础信息
        spuMapper.updateSpuBasicInfo(spu);
        //删除之前的信息
        //删除分类信息
        spuMapper.deleteSpuCategoryBySpuId(spuId);
        //删除图片信息
        spuImgMapper.deleteImgBySpuId(spuId);
        //商品信息更新即可
        //删除sku，此种操作方式废弃
        //这里更新sku信息即可
        updateSku(spu);
        //插入其他信息
        insertOtherData(spu, false);
        return Message.builder()
                .message("修改成功")
                .build();
    }

    private void updateSku(Spu spu) {
        //获取sku
        List<Sku> skus = spu.getSkus();
        //逐个更新即可
        skus.forEach(sku -> skuMapper.updateSkuBySpecificationValueAndSpuId(spu.getId(), sku));
    }

    private void insertOtherData(Spu spu, boolean needInsertSkus) {
        //获取分类信息
        List<Category> categories = spu.getCategories();
        //遍历category并将商品信息与
        categories.forEach(category -> spuMapper.insertSpuCategory(spu.getId(), category.getId()));
        //获取图片
        List<SpuImg> spuImgs = spu.getSpuImgs();
        //遍历插入到图片表中
        spuImgs.forEach(spuImg -> spuImgMapper.insert(spu.getId(), spuImg));


        if (!needInsertSkus) {
            //不更新sku插入信息就变成了更新信息
            spuInfoMapper.updateBySpuId(spu.getId(), spu.getSpuInfo());
            return;
        }
        //插入信息
        spuInfoMapper.insert(spu.getId(), spu.getSpuInfo());
        //获取sku
        List<Sku> skus = spu.getSkus();
        //插入库存
        skus.forEach(sku -> skuMapper.insert(spu.getId(), sku));
    }
}
