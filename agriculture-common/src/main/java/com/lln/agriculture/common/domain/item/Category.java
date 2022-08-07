package com.lln.agriculture.common.domain.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lilinnan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    /**
     * id
     */
    private Integer id;

    /**
     * 类别名称
     */
    private String name;


    /**
     * 父节点id
     */
    private Integer parentId;

    /**
     * 儿子
     */
    private List<Category> children;

}