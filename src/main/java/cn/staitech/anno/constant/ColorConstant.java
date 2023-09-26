package cn.staitech.anno.constant;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author: wangfeng
 * @create: 2023-09-13 18:05:38
 * @Description: 颜色类型
 */

public class ColorConstant {
    /**
     * 图片删除状态：0逻辑删除，1未逻辑删除
     */
    public static final Map<Integer, String> COLOR_TYPE = new ImmutableMap.Builder<Integer, String>()
            /*.put(1, "RGB")
            .put(2, "HEX")*/
            .put(1, "荧光标记染色")
            .put(2, "免疫组织化学染色")
            .put(3, "HE染色")
            .put(4, "Masson染色")
            .put(5, "Van Gieson染色")
            .put(6, "维多利亚蓝染色")
            .put(7, "苏丹III/IV染色")
            .put(8, "油红O染色")
            .put(9, "PAS糖原染色")
            .put(10, "AB-PAS染色")
            .put(11, "刚果红染色(甲醇)")
            .put(12, "甲苯胺蓝染色")
            .put(13, "普鲁氏蓝染色")
            .put(14, "尼氏染色")
            .put(15, "LFB髓鞘染色")
            .put(16, "Tunel染色")
            .put(17, "Ki67")
            /*.put(18, "免疫组织化学染色")
            .put(19, "荧光标记染色")*/
            .put(20, "其他")
            .build();
}
