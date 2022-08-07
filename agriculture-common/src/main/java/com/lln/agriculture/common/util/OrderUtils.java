package com.lln.agriculture.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/21 15:18
 */

public class OrderUtils {

    public static String generateOrderNum() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        String s = String.valueOf(Math.random() * Math.random() * 903).replace(".", "").substring(0, 4);
        return simpleDateFormat.format(new Date()) + String.valueOf(System.currentTimeMillis()).substring(4) + s;
    }


    public static String parsePrice(long price) {
        StringBuilder sb = new StringBuilder(String.valueOf(price));
        while (sb.length() < 3) {
            sb.insert(0, '0');
        }
        sb.insert(sb.length() - 2, '.');
        return sb.toString();
    }

}
