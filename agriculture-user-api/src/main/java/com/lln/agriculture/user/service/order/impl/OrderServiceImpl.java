package com.lln.agriculture.user.service.order.impl;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.item.Sku;
import com.lln.agriculture.common.domain.order.*;
import com.lln.agriculture.common.domain.userinfo.Address;
import com.lln.agriculture.common.domain.userinfo.User;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.exception.InternalServerErrorException;
import com.lln.agriculture.common.util.CommonUtils;
import com.lln.agriculture.common.util.OrderUtils;
import com.lln.agriculture.user.domain.item.SimpleSpu;
import com.lln.agriculture.user.mapper.item.SkuMapper;
import com.lln.agriculture.user.mapper.order.OrderMapper;
import com.lln.agriculture.user.mapper.order.OrderSkuMapper;
import com.lln.agriculture.user.mapper.userinfo.AddressMapper;
import com.lln.agriculture.user.mapper.userinfo.SpuMapper;
import com.lln.agriculture.user.service.order.OrderService;
import com.lln.agriculture.user.service.order.PayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/21 18:30
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;
    private final SpuMapper spuMapper;
    private final OrderSkuMapper orderSkuMapper;
    private final PayService payService;
    private final SkuMapper skuMapper;


    public OrderServiceImpl(OrderMapper orderMapper, AddressMapper addressMapper, SpuMapper spuMapper, OrderSkuMapper orderSkuMapper, PayService payService, SkuMapper skuMapper) {
        this.orderMapper = orderMapper;
        this.addressMapper = addressMapper;
        this.spuMapper = spuMapper;
        this.orderSkuMapper = orderSkuMapper;
        this.payService = payService;
        this.skuMapper = skuMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message submitOrder(BuyInfo buyInfo) {
        int addressId = buyInfo.getAddressId();
        Address address = addressMapper.selectAddressByIdAndUsername(addressId, CommonUtils.getCurrentUsername());
        if (address == null) {
            throw new BadRequestException("错误的id");
        }
        Sku sku = spuMapper.selectSkuBySkuId(buyInfo.getSkuId());
        if (sku == null) {
            throw new BadRequestException("不存在的型号！");
        }
        //获取商品信息
        SimpleSpu simpleSpu = spuMapper.selectSimpleSpuBySkuId(sku.getId());
        if (simpleSpu == null) {
            throw new BadRequestException("不存在的商品");
        }
        if (buyInfo.getBuyNum() > sku.getStock()) {
            throw new BadRequestException("很抱歉，您购买的此单商品全部无货！");
        }
        //更新库存信息
        skuMapper.updateSkuStock(sku.getId(), -buyInfo.getBuyNum());
        Order order = Order.builder()
                .orderNum(OrderUtils.generateOrderNum())
                .name(address.getName())
                .phone(address.getPhone())
                .address(address.getAddress())
                .detail(address.getDetail())
                .user(User.builder().id(address.getUserId()).build())
                .price(sku.getPrice() * buyInfo.getBuyNum())
                .status(Status.builder().id(1).build())
                .createTime(new Date())
                .logisticsCompany(LogisticsCompany.builder().build())
                .payMethod(PayMethod.builder().id(1).build())
                .remark(buyInfo.getRemark())
                .build();
        orderMapper.insertSelective(order);
        OrderSku orderSku = OrderSku.builder()
                .skuId(buyInfo.getSkuId())
                .spuId(simpleSpu.getId())
                .spuName(simpleSpu.getName())
                .skuName(sku.getName())
                .num(buyInfo.getBuyNum())
                .spuImg(simpleSpu.getImageUrl())
                .skuPrice(sku.getPrice())
                .orderId(order.getId())
                .build();
        orderSkuMapper.insert(orderSku);
        return startPay(buyInfo.isPc(), orderSku.getSpuName(), order.getOrderNum(), OrderUtils.parsePrice(order.getPrice()));
    }


    private Message startPay(boolean pc, String subject, String no, String price) {
        String s = payService.pay(pc, subject, no, price);
        return Message.builder()
                .data(s)
                .build();
    }


    @Override
    public Message pay(OrderPayInfo orderPayInfo) {
        Order order = orderMapper.selectByOrderNumAndUsername(orderPayInfo.getOrderNum(), CommonUtils.getCurrentUsername());
        if (order.getStatus().getId() != 1) {
            throw new BadRequestException("该订单非未支付状态，无法进行支付！");
        }
        if (order.getSkus().size() <= 0) {
            throw new InternalServerErrorException("服务器内部错误");
        }
        OrderSku orderSku = order.getSkus().get(0);
        return startPay(orderPayInfo.isPc(), orderSku.getSpuName(), orderPayInfo.getOrderNum(), OrderUtils.parsePrice(order.getPrice()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message check(Map<String, String> param) {
        if (payService.check(param)) {
            orderMapper.updateOrderByOrderNumAndCheckStatus(
                    Order.builder()
                            .orderNum(param.get("out_trade_no"))
                            .payOrderNum(param.get("trade_no"))
                            .status(Status.builder().id(2).build())
                            .payTime(new Date())
                            .build(), Collections.singletonList(1));
            return Message.builder()
                    .message("支付成功！")
                    .build();
        }
        throw new BadRequestException("支付失败！");
    }

    @Override
    public String asyncCheck(Map<String, String> param) {
        if (payService.check(param)) {
            orderMapper.updateOrderByOrderNumAndCheckStatus(
                    Order.builder()
                            .orderNum(param.get("out_trade_no"))
                            .payOrderNum(param.get("trade_no"))
                            .status(Status.builder().id(2).build())
                            .payTime(new Date())
                            .build(), Collections.singletonList(1));
            return "success";
        }
        return "fail";
    }

    @Override
    public Message selectHistoryOrder(int type) {
        List<Order> orders = orderMapper
                .selectOrdersByUsername(CommonUtils.getCurrentUsername(), type <= -1 ? null : type);
        orders.forEach(order -> order.setPhone(CommonUtils.encryptedPhoneNumber(order.getPhone())));
        return Message.builder()
                .message("查询成功")
                .data(orders)
                .build();
    }

    @Override
    public Message queryOrder(String orderNum) {
        Order order = orderMapper.selectByOrderNumAndUsername(orderNum, CommonUtils.getCurrentUsername());
        if (order == null) {
            throw new BadRequestException("不存在的订单");
        }
        order.setPhone(CommonUtils.encryptedPhoneNumber(order.getPhone()));
        return Message.builder().data(order).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message confirm(String orderNum) {
        Order order = orderMapper.selectByOrderNumAndUsername(orderNum, CommonUtils.getCurrentUsername());
        if (order == null) {
            throw new BadRequestException("不存在的订单");
        }
        if (order.getStatus().getId() != 3) {
            throw new BadRequestException("非法的订单状态");
        }
        Order newOrder = Order.builder().confirmTime(new Date()).status(Status.builder().id(4).build()).orderNum(orderNum).build();
        orderMapper.updateOrderByOrderNumAndCheckStatus(newOrder, Collections.singletonList(3));
        return Message.builder().message("收货成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message cancel(String orderNum) {
        Order order = orderMapper.selectByOrderNumAndUsername(orderNum, CommonUtils.getCurrentUsername());
        if (order == null) {
            throw new BadRequestException("不存在的订单");
        }
        if (order.getStatus().getId() != 1) {
            throw new BadRequestException("非法的订单状态");
        }
        Order newOrder = Order.builder().status(Status.builder().id(0).build()).orderNum(orderNum).build();
        orderMapper.updateOrderByOrderNumAndCheckStatus(newOrder, Collections.singletonList(1));
        //恢复库存
        order.getSkus().forEach(orderSku -> skuMapper.updateSkuStock(orderSku.getSkuId(), orderSku.getNum()));
        return Message.builder().message("取消成功").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message delete(String orderNum) {
        Order order = orderMapper.selectByOrderNumAndUsername(orderNum, CommonUtils.getCurrentUsername());
        if (order == null) {
            throw new BadRequestException("不存在的订单");
        }
        Integer id = order.getStatus().getId();
        if (id != 0 && id != 4) {
            throw new BadRequestException("错误的订单状态");
        }
        orderSkuMapper.deleteByOrderId(order.getId());
        orderMapper.deleteByPrimaryKey(order.getId());
        return Message.builder().message("删除成功").build();
    }
}