package com.lln.agriculture.manage.mapper.order;

import com.lln.agriculture.common.domain.order.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/1 13:57
 */

@Mapper
public interface OrderMapper {

    List<Order> selectAllOrderPage(@Param("key") String key, @Param("status") Integer status);


    int updateOrderByOrderNumAndCheckStatus(@Param("order") Order order, @Param("status") Collection<Integer> status);


    boolean orderExist(String orderNum);


    Order selectByOrderNum(String orderNum);
}