package cn.staitech.anno.utils;

import cn.staitech.common.security.utils.SecurityUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 国际化工具
 */
public class MessageSource {

    private static org.springframework.context.MessageSource messageSource;

    public static void init(org.springframework.context.MessageSource messageSource) {
        MessageSource.messageSource = messageSource;
    }

    public static String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * @param code ：对应messages配置的key.
     * @param args : 数组参数.
     * @return
     */
    public static String getMessage(String code, Object[] args) {
        return getMessage(code, args, "");
    }

    /**
     * @param code           ：对应messages配置的key.
     * @param args           : 数组参数.
     * @param defaultMessage : 没有设置key的时候的默认值.
     * @return
     */
    public static String getMessage(String code, Object[] args, String defaultMessage) {
        //这里使用比较方便的方法，不依赖request.
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }


    /**
     * 自定义Local
     * @param code
     * @return
     */
    public static String M(String code) {
        String language = SecurityUtils.getLoginUser().getLanguage();
        if (language == null) {
            language = "";
        }
        System.out.println("language = " + language);
        // language = "en_US";
        // String language = "en";
        // String language = "zh";
        Locale locale = new Locale(language);
        return messageSource.getMessage(code, null, locale);
    }

}
