package com.lln.agriculture.user.security.service.impl;

import com.google.gson.Gson;
import com.lln.agriculture.common.util.CommonUtils;
import com.lln.agriculture.user.security.config.bean.VaptchaConfig;
import com.lln.agriculture.user.security.service.CaptchaVerifyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/4 9:12
 */

@Service
public class CaptchaVerifyServiceImpl implements CaptchaVerifyService {

    private final Gson gson;
    private final OkHttpClient okHttpClient;
    private final VaptchaConfig vaptchaConfig;

    @Autowired
    public CaptchaVerifyServiceImpl(Gson gson, OkHttpClient okHttpClient, VaptchaConfig vaptchaConfig) {
        this.gson = gson;
        this.okHttpClient = okHttpClient;
        this.vaptchaConfig = vaptchaConfig;
    }

    @Override
    public boolean verify(HttpServletRequest request, String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        String ip = CommonUtils.getIpAddress(request);
        FormBody formBody = new FormBody.Builder()
                .add("id", vaptchaConfig.getVid())
                .add("secretkey", vaptchaConfig.getKey())
                .add("scene", "1")
                .add("token", token)
                .add("ip", ip)
                .build();

        Request request1 = new Request
                .Builder()
                .url("http://0.vaptcha.com/verify")
                .post(formBody)
                .build();
        try {
            String string = Objects.requireNonNull(okHttpClient.newCall(request1).execute().body()).string();
            SecondVerify secondVerify = gson.fromJson(string, SecondVerify.class);
            System.out.println(secondVerify);
            return secondVerify.getScore() >= 70;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SecondVerify {
    private int success;
    private String msg;
    private int score;
}
