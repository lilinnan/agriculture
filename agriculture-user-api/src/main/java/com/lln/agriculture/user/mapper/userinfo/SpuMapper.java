package com.lln.agriculture.user.mapper.userinfo;

import com.lln.agriculture.common.domain.item.Sku;
import com.lln.agriculture.common.domain.item.Spu;
import com.lln.agriculture.user.domain.item.SimpleSpu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SpuMapper {

    List<SimpleSpu> selectLatestSimpleSpu(int num);

    Spu selectSpuById(int spuId);

    SimpleSpu selectSimpleSpuBySkuId(int skuId);

    Sku selectSkuBySkuId(int skuId);

    List<SimpleSpu> searchSimpleSpu(String key);
}