package com.lln.agriculture.common.agriculture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
    * 智慧农业数据服务-实体下的分类
 * @author lilinnan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgricultureClassify {
    /**
    * 主键
    */
    private Integer id;

    /**
    * 分类名称
    */
    private String name;

    /**
    * 实体id
    */
    private Integer entityId;

    /**
     * 详情信息
     */
    private List<AgricultureDetail> details;
}