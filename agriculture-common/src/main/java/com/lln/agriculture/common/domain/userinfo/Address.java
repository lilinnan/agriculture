package com.lln.agriculture.common.domain.userinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Integer id;

    /**
    * 用户id
    */
    private Integer userId;

    /**
    * 收货人姓名
    */
    private String name;

    /**
    * 手机号
    */
    private String phone;

    /**
    * 地址
    */
    private String address;

    /**
    * 详细地址
    */
    private String detail;
}