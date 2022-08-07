package com.lln.agriculture.user.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.google.gson.Gson;
import com.lln.agriculture.common.util.CommonUtils;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/4 9:05
 */

@Configuration
public class CommonBean {

    private final AlipayConfig alipayConfig;

    @Autowired
    public CommonBean(AlipayConfig alipayConfig) {
        this.alipayConfig = alipayConfig;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Bean
    public Gson gson() {
        return CommonUtils.getGson();
    }

    @Bean
    public void initAlipay() {
        Config config = new Config();
        config.protocol = alipayConfig.getProtocol();
        config.gatewayHost = alipayConfig.getGateway();
        config.signType = alipayConfig.getSignType();
        config.appId = alipayConfig.getAppId();
        config.merchantPrivateKey = alipayConfig.getMerchantPrivateKey();
        config.merchantCertPath = alipayConfig.getMerchantCertPath();
        config.alipayCertPath = alipayConfig.getAlipayCertPath();
        config.alipayRootCertPath = alipayConfig.getAlipayRootCertPath();
        config.notifyUrl = alipayConfig.getNotifyUrl();
        Factory.setOptions(config);
    }
}
