package com.lln.agriculture.user.service.order;

import com.lln.agriculture.common.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/21 15:17
 */

public interface OrderService {

    Message submitOrder(BuyInfo buyInfo);

    Message pay(OrderPayInfo orderPayInfo);

    Message check(Map<String, String> param);

    String asyncCheck(Map<String,String> param);

    Message selectHistoryOrder(int type);

    Message queryOrder(String order);

    Message confirm(String orderNum);

    Message cancel(String orderNum);

    Message delete(String orderNum);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class BuyInfo {
        private int skuId;
        private int buyNum;
        private int addressId;
        private boolean pc;
        private String remark = "";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class OrderPayInfo {
        private String orderNum;
        private boolean pc;
    }

}