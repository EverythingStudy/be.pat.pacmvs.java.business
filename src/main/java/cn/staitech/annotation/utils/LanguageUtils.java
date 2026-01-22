package cn.staitech.annotation.utils;

import cn.staitech.common.security.utils.SecurityUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wangfeng
 * @create: 2023-10-17 17:11:48
 * @Description: 语言判断
 */

public class LanguageUtils {
    public static boolean isEn(ServerHttpRequest request) {
        if (request.getHeaders().getFirst("Language") != null && "en-us".equals(request.getHeaders().getFirst("Language"))) {
            return true;
        }
        return false;
    }
    public static boolean isEn() {
        return SecurityUtils.getLoginUser().getLanguage() != null && "en-us".equals(SecurityUtils.getLoginUser().getLanguage());
    }
    public static String getLanguage(HttpServletRequest request) {
        return request.getHeader("Language") != null ? request.getHeader("Language") : "zh-cn";
    }
}