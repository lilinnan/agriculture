package com.lln.agriculture.common.util;

import java.util.UUID;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/29 14:34
 */

public class NameUtils {
    public static String randomFilename(String suffix) {
        return UUID.randomUUID().toString() + "." + suffix;
    }
}
