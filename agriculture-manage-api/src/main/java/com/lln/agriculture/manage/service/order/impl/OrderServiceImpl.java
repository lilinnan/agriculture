package com.lln.agriculture.manage.service.order.impl;

import com.github.pagehelper.PageInfo;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.common.domain.order.LogisticsCompany;
import com.lln.agriculture.common.domain.order.Order;
import com.lln.agriculture.common.domain.order.Status;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.util.PageUtils;
import com.lln.agriculture.manage.mapper.logistics.LogisticsCompanyMapper;
import com.lln.agriculture.manage.mapper.order.OrderMapper;
import com.lln.agriculture.manage.service.order.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/1 14:06
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final LogisticsCompanyMapper logisticsCompanyMapper;

    public OrderServiceImpl(OrderMapper orderMapper, LogisticsCompanyMapper logisticsCompanyMapper) {
        this.orderMapper = orderMapper;
        this.logisticsCompanyMapper = logisticsCompanyMapper;
    }

    @Override
    public Message selectAllOrder(PageRequest pageRequest, int status) {
        PageUtils.startPage(pageRequest);
        return Message.builder()
                .data(PageInfo.of(orderMapper.selectAllOrderPage(pageRequest.getKey(), status == -1 ? null : status)))
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message delivery(DeliveryData deliveryData) {
        if (!logisticsCompanyMapper.logisticsCompanyExist(deliveryData.getLogisticsCompanyId())) {
            throw new BadRequestException("不存在的物流公司");
        }
        Order order = orderMapper.selectByOrderNum(deliveryData.getOrderNum());
        if (order == null) {
            throw new BadRequestException("不存在的订单");
        }
        if (order.getStatus().getId() < 2 || order.getStatus().getId() > 3) {
            throw new BadRequestException("错误的订单状态");
        }
        Order newOrder = Order.builder()
                .logisticsCompany(LogisticsCompany.builder().id(deliveryData.getLogisticsCompanyId()).build())
                .trackNumber(deliveryData.getTrackNumber())
                .deliveryTime(new Date())
                .orderNum(deliveryData.getOrderNum())
                .status(Status.builder().id(3).build())
                .build();
        orderMapper.updateOrderByOrderNumAndCheckStatus(newOrder, Arrays.asList(2, 3));
        return Message.builder().message("发货成功").build();
    }
}
