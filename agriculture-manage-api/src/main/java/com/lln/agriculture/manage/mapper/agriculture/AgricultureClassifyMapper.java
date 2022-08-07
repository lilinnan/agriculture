package com.lln.agriculture.manage.mapper.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureClassify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AgricultureClassifyMapper {

    int insert(AgricultureClassify record);

    int insertSelective(AgricultureClassify record);

    int updateByPrimaryKeySelective(AgricultureClassify record);

    List<AgricultureClassify> selectAllClassifyByEntityId(int entityId);

    int deleteByPrimaryKey(int id);

    int deleteByEntityId(int entityId);

}