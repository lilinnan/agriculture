package com.lln.agriculture.common.domain.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品信息表
 * @author lilinnan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpuInfo {
    
    private Integer id;

    /**
     * 商品id
     */
    private Integer spuId;

    /**
     * 商品属性,json键值对
     */
    private String attr;

    /**
     * 商品规格,json格式
     */
    private String specification;

    /**
     * 商品详细信息,html格式
     */
    private String description;
}