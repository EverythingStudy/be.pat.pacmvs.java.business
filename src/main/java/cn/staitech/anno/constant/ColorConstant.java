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
            .put(1, "RGB")
            .put(2, "HEX")
            .build();
}
