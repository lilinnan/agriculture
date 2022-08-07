package com.lln.agriculture.common.domain.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpuImg {
    /**
    * id
    */
    private Integer id;

    /**
    * spu_id
    */
    @JsonIgnore
    private Integer spuId;

    /**
    * 商品图片链接
    */
    private String img;
}