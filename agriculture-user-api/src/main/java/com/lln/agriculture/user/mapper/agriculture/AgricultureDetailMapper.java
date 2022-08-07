package com.lln.agriculture.user.mapper.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureDetail;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AgricultureDetailMapper {

    int insert(AgricultureDetail record);


    int insertSelective(AgricultureDetail record);


    int updateByPrimaryKeySelective(AgricultureDetail record);


    AgricultureDetail selectDetailById(int detailId);

    int deleteByPrimaryKey(int detailId);

    int deleteByClassifyId(int classifyId);

    int deleteByEntityId(int entityId);

}