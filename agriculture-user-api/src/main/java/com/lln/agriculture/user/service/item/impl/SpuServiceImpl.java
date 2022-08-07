package com.lln.agriculture.user.service.item.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.item.Sku;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.util.PageUtils;
import com.lln.agriculture.user.domain.item.SimpleSpu;
import com.lln.agriculture.user.mapper.userinfo.SpuMapper;
import com.lln.agriculture.user.service.item.SpuService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/17 13:28
 */

@Service
public class SpuServiceImpl implements SpuService {

    private final SpuMapper spuMapper;


    public SpuServiceImpl(SpuMapper spuMapper) {
        this.spuMapper = spuMapper;
    }

    @Override
    public Message latestSimpleSpu(int num) {
        return Message.builder()
                .message("查询成功")
                .data(spuMapper.selectLatestSimpleSpu(num))
                .build();
    }

    @Override
    public Message selectBuyInfoBySkuId(int skuId) {
        Map<String, Object> map = new HashMap<>(2);
        SimpleSpu simpleSpu = spuMapper.selectSimpleSpuBySkuId(skuId);
        if (simpleSpu == null) {
            throw new BadRequestException("不存在的商品");
        }
        Sku sku = spuMapper.selectSkuBySkuId(skuId);
        map.put("spu", simpleSpu);
        map.put("sku", sku);
        return Message.builder()
                .message("查询成功")
                .data(map)
                .build();
    }

    @Override
    public Message selectSpuById(int spuId) {
        return Message.builder()
                .data(spuMapper.selectSpuById(spuId))
                .build();
    }

    @Override
    public Message search(PageRequest pageRequest) {
        PageUtils.startPage(pageRequest);
        return Message.builder()
                .message("查询成功")
                .data(PageInfo.of(spuMapper.searchSimpleSpu(pageRequest.getKey())))
                .build();
    }
}
