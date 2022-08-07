package com.lln.agriculture.manage.mapper.item;

import com.lln.agriculture.common.domain.item.Spu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SpuMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insertSpu(Spu record);

    int insertSpuCategory(int spuId, int categoryId);

    int deleteSpuCategoryBySpuId(int spuId);

    List<Spu> selectAllSpuPage(String key);

    Spu selectSpuById(int spuId);

    int deleteSpuById(int spuId);

    int updateSpuBasicInfo(Spu spu);

    List<Spu> selectAllDeletedSpu(String key);

    int restoreDeletedSpu(int spuId);

    int realDeleteSpu(int spuId);

    int updateSpuSaleable(Spu spu);
}