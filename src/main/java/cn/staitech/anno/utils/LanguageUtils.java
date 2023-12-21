package cn.staitech.anno.utils;

import cn.staitech.common.security.utils.SecurityUtils;

/**
 * @author: wangfeng
 * @create: 2023-10-17 17:11:48
 * @Description: 语言判断
 */

public class LanguageUtils {
    public static boolean isEn() {
        return SecurityUtils.getLoginUser().getLanguage() != null && "en-us".equals(SecurityUtils.getLoginUser().getLanguage());
    }
}
