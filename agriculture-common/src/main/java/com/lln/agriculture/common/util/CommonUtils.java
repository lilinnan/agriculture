package com.lln.agriculture.common.util;

import com.google.gson.Gson;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.order.Order;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/6 17:11
 */

public class CommonUtils {
    private final static Gson GSON = new Gson();


    /**
     * 获取文件后缀，当文件无后缀，返回空字符串，有后缀则返回后缀，包含.
     *
     * @param filename 文件名
     * @return 文件后缀
     */
    public static String getFileSuffix(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        return filename.substring(dotIndex);
    }


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String s : ips) {
                if (!(unknown.equalsIgnoreCase(s))) {
                    ip = s;
                    break;
                }
            }
        }
        return ip;
    }

    public static String getParameter(HttpServletRequest request, String name) {
        String parameter = request.getParameter(name);
        if (parameter == null) {
            return "";
        }
        return parameter;
    }

    public static Gson getGson() {
        return GSON;
    }

    public static void print(HttpServletResponse response, Message o) throws IOException {
        response.setStatus(o.getStatus());
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(getGson().toJson(o));
    }

    public static String getCurrentUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String encryptedPhoneNumber(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    public static void processOrder(Order order) {

    }

}
