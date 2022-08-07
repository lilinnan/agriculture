package com.lln.agriculture.user.mapper.item;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SkuMapper {

    int updateSkuStock(@Param("skuId") int skuId, @Param("changeNum") int changeNum);

}