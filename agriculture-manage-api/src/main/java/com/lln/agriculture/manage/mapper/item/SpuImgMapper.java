package com.lln.agriculture.manage.mapper.item;

import com.lln.agriculture.common.domain.item.SpuImg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpuImgMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(int spuId, SpuImg record);

    int deleteImgBySpuId(int spuId);
}