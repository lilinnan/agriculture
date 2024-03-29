package com.lln.agriculture.user.mapper.order;

import com.lln.agriculture.common.domain.order.PayMethod;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayMethodMapper {
    /**
     * delete by primary key
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(PayMethod record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(PayMethod record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    PayMethod selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(PayMethod record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(PayMethod record);
}