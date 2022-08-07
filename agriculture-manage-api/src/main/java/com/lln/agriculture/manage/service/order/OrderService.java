package com.lln.agriculture.manage.service.order;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/1 14:06
 */

public interface OrderService {

    /**
     * 查询订单信息
     *
     * @param pageRequest 分页请求
     * @param status      订单状态 -1为全部
     * @return message
     */
    Message selectAllOrder(PageRequest pageRequest, int status);


    Message delivery(DeliveryData deliveryData);


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeliveryData {
        @NotBlank(message = "订单号不能为空")
        @NotNull(message = "订单号不能为空")
        private String orderNum;
        @Min(value = 1, message = "请输入正确的物流公司")
        private Integer logisticsCompanyId;
        @NotBlank(message = "物流单号不能为空")
        @NotNull(message = "物流单号不能为空")
        private String trackNumber;
    }
}
