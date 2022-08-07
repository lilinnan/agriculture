package com.lln.agriculture.common.agriculture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 智慧农业数据服务-实体
 * @author lilinnan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgricultureEntity {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否是植物
     */
    private Boolean plant;
}