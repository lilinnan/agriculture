package com.lln.agriculture.common.agriculture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 智慧农业数据服务-图片信息
 *
 * @author lilinnan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgricultureDetailImg {
    /**
     * 主键
     */
    @JsonIgnore
    private Integer id;

    /**
     * 图片
     */
    private String img;

    /**
     * 详情id
     */
    @JsonIgnore
    private Integer detailId;
}