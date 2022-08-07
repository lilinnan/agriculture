package com.lln.agriculture.common.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsCompany {
    /**
    * id
    */
    private Integer id;

    /**
    * 物流公司名称
    */
    private String name;
}