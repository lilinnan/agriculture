package com.lln.agriculture.user.mapper.order;

import com.lln.agriculture.common.domain.order.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface OrderMapper {
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
    int insert(Order record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(Order record);


    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    Order selectByOrderNumAndUsername(@Param("orderNum") String orderNum, @Param("username") String username);

    /**
     * 查询用户订单
     *
     * @param username 用户名
     * @param status   订单状态
     * @return list
     */
    List<Order> selectOrdersByUsername(String username, Integer status);


    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(Order record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(Order record);



    int updateOrderByOrderNumAndCheckStatus(@Param("order") Order order, @Param("status") Collection<Integer> status);
}