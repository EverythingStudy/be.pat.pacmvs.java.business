package cn.staitech.anno.utils;

import cn.staitech.common.security.utils.SecurityUtils;

/**
 * @author: wangfeng
 * @create: 2023-10-17 17:11:48
 * @Description: 语言判断
 */

public class LanguageUtils {
    public static boolean isEn() {
        if (SecurityUtils.getLoginUser().getLanguage() != null && SecurityUtils.getLoginUser().getLanguage().equals("en")) {
            return true;
        }
        return false;
    }
}
