package com.lln.agriculture.common.domain.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品表
 *
 * @author lilinnan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Spu {
    /**
     * 商品id
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 是否上架
     */
    private Boolean saleable;

    /**
     * 是否删除
     */
    private Boolean delete;

    /**
     * 商品品牌
     */
    private Brand brand;

    /**
     * 商品图片
     */
    private List<SpuImg> spuImgs;

    /**
     * 商品分类
     */
    private List<Category> categories;

    /**
     * 商品详情信息
     */
    private SpuInfo spuInfo;

    /**
     * 商品规格
     */
    private List<Sku> skus;
}