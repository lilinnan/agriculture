package com.lln.agriculture.user.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/17 13:20
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSpu {
    /**
     * 商品id
     */
    private Integer id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品url
     */
    private String imageUrl;
    /**
     * 商品sku最低价格
     */
    private Integer price;
}
