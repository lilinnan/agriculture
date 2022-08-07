package com.lln.agriculture.user.mapper.order;

import com.lln.agriculture.common.domain.order.OrderSku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderSkuMapper {
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
    int insert(OrderSku record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(OrderSku record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    OrderSku selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(OrderSku record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(OrderSku record);

    int deleteByOrderId(Integer id);
}