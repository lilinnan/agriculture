package com.lln.agriculture.common.domain.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lln.agriculture.common.domain.userinfo.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 订单表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    /**
     * 主键
     */
    @JsonIgnore
    private Integer id;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人手机号
     */
    private String phone;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 订单用户
     */
    private User user;

    /**
     * 订单金额
     */
    private Long price;

    /**
     * 订单状态
     */
    private Status status;

    /**
     * 快递单号
     */
    private String trackNumber;

    /**
     * 物流公司id
     */
    private LogisticsCompany logisticsCompany;

    /**
     * 订单创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 订单支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;

    /**
     * 成交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmTime;

    /**
     * 支付方式
     */
    private PayMethod payMethod;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 支付单号（来自支付渠道）
     */
    private String payOrderNum;


    private List<OrderSku> skus;
}