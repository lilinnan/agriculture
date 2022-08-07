package com.lln.agriculture.common.domain.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 品牌
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    /**
     * 品牌id
     */
    private Integer id;

    /**
     * 品牌名称
     */

    @NotNull(message = "请输入品牌名称")
    @NotBlank(message = "请输入品牌名称")
    private String name;
}