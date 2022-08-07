package com.lln.agriculture.manage.mapper.item;

import com.lln.agriculture.common.domain.item.SpuInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpuInfoMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(int spuId, SpuInfo record);

    int deleteInfoBySpuId(int spuId);

    int updateBySpuId(int id, SpuInfo spuInfo);
}