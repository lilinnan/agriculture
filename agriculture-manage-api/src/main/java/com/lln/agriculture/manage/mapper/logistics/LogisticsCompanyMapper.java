package com.lln.agriculture.manage.mapper.logistics;

import com.lln.agriculture.common.domain.order.LogisticsCompany;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogisticsCompanyMapper {
    /**
     * delete by primary key
     *
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(LogisticsCompany record);


    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(LogisticsCompany record);


    List<LogisticsCompany> selectAllPage(String key);


    boolean logisticsCompanyExist(int id);
}