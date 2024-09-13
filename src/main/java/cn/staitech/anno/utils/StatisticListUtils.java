package cn.staitech.anno.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.NumberFormat;

/**
 * 数据统计模块通用类
 *
 * @author zhaoshaoning
 */
@Slf4j
public class StatisticListUtils {

    /**
     * 机构数字格式化
     *
     * @param number
     * @return C012
     */
    public static String getFourNumberNoSlide(Long number) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(3);
        formatter.setGroupingUsed(false);
        return "C" + formatter.format(number);
    }
}
