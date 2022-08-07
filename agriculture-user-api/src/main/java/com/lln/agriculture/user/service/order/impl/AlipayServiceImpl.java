package com.lln.agriculture.user.service.order.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.user.config.AlipayConfig;
import com.lln.agriculture.user.service.order.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/21 19:40
 */

@Service
public class AlipayServiceImpl implements PayService {

    private final AlipayConfig alipayConfig;

    @Autowired
    public AlipayServiceImpl(AlipayConfig alipayConfig) {
        this.alipayConfig = alipayConfig;
    }

    @Override
    public String pay(boolean pc, String subject, String no, String price) {
        try {
            if (pc) {
                AlipayTradePagePayResponse pay = Factory.Payment.Page().pay(subject, no, price, alipayConfig.getSuccessUrl());
                if (ResponseChecker.success(pay)) {
                    return pay.getBody();
                }
            } else {
                AlipayTradeWapPayResponse pay = Factory.Payment.Wap()
                        .asyncNotify(alipayConfig.getNotifyUrl())
                        .pay(subject, no, price, String.format(alipayConfig.getExitUrl(), no), alipayConfig.getSuccessUrl());
                if (ResponseChecker.success(pay)) {
                    return pay.getBody();
                }
            }
            throw new BadRequestException("支付失败");
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public boolean check(Map<String, String> param) {
        try {
            return Factory.Payment.Common().verifyNotify(param);
        } catch (Exception e) {
            return false;
        }
    }
}
