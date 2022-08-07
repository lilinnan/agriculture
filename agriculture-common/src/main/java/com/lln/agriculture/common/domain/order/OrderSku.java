package com.lln.agriculture.common.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单商品表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSku {
    /**
     * id
     */
    @JsonIgnore
    private Integer id;

    /**
     * spu Id
     */
    private Integer spuId;

    /**
     * skuId 用来恢复库存
     */
    private Integer skuId;

    /**
     * 购买时的商品名称
     */
    private String spuName;

    /**
     * 购买的商品规格名称
     */
    private String skuName;

    /**
     * 购买的商品数量
     */
    private Integer num;

    /**
     * 商品图片
     */
    private String spuImg;

    /**
     * 单个商品价格（单位：分）
     */
    private Long skuPrice;

    /**
     * 订单id
     */
    private Integer orderId;
}