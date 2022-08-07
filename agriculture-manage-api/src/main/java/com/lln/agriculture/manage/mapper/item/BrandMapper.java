package com.lln.agriculture.manage.mapper.item;

import com.lln.agriculture.common.domain.item.Brand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrandMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(Brand record);


    List<Brand> selectAllPage(String key);


    int deleteById(int id);


    int updateByIdSelective(Brand brand);

}