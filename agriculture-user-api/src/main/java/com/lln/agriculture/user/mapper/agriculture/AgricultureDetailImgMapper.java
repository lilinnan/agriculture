package com.lln.agriculture.user.mapper.agriculture;

import com.lln.agriculture.common.agriculture.AgricultureDetailImg;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AgricultureDetailImgMapper {
    int insert(AgricultureDetailImg record);

    int insertSelective(AgricultureDetailImg record);

    int updateByPrimaryKeySelective(AgricultureDetailImg record);

    int deleteAllImgByDetailId(Integer id);

    int deleteAllImgByClassifyId(int id);

    int deleteAllImgByEntityId(int entityId);
}