package com.lln.agriculture.common.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayMethod {
    /**
    * id
    */
    private Integer id;

    /**
    * 支付方式
    */
    private String name;
}