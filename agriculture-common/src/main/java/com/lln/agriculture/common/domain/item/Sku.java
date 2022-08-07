package com.lln.agriculture.common.domain.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存数据
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sku {
    private Integer id;

    /**
     * spu_id
     */
    private Integer spuId;

    /**
     * 规格值,1-1,2-1的形式
     */
    private String specificationValue;

    /**
     * 价格,单位：分
     */
    private Long price;

    /**
     * 库存量，单位：个
     */
    private Integer stock;

    /**
     * 规格名称
     */
    private String name;
}