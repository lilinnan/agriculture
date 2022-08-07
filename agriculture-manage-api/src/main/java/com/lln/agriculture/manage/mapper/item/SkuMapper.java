package com.lln.agriculture.manage.mapper.item;

import com.lln.agriculture.common.domain.item.Sku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkuMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(int spuId, Sku record);

    int deleteSkuBySpuId(int spuId);

    int updateSkuBySpecificationValueAndSpuId(int id, Sku sku);
}