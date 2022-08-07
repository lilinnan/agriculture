package com.lln.agriculture.user.mapper.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface AgricultureEntityMapper {

    int insert(AgricultureEntity record);

    int insertSelective(AgricultureEntity record);

    int updateByPrimaryKeySelective(AgricultureEntity record);

    List<AgricultureEntity> selectAllEntityPage(@Param("plant") boolean plant, @Param("key") String key);

    int deleteByPrimaryKey(int id);
}