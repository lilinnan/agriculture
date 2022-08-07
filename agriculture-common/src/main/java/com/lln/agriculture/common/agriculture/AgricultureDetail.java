package com.lln.agriculture.common.agriculture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 智慧农业数据服务-详情信息表
 *
 * @author lilinnan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgricultureDetail {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 详情名称
     */
    private String name;

    /**
     * 其他属性，直接存json
     */
    private String attr;

    /**
     * 属于哪个分类下面
     */
    private Integer classifyId;

    /**
     * 图片
     */
    private List<AgricultureDetailImg> detailImgs;
}