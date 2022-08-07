package com.lln.agriculture.user.service.order;

import com.lln.agriculture.common.domain.Message;

import java.util.Map;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/21 19:40
 */

public interface PayService {

    String pay(boolean pc, String subject, String no, String price);


    boolean check(Map<String, String> param);
}
