package com.lln.agriculture.common.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/29 14:27
 */

public class ImageUtils {

    public static InputStream decodeBase64ImgToInputStream(String base64) {
        byte[] bytes = Base64.getDecoder().decode(base64.replace("data:image/png;base64,", ""));
        return new ByteArrayInputStream(bytes);
    }
}
