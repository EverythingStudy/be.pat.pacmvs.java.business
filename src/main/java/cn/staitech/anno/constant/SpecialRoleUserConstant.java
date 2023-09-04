package cn.staitech.anno.constant;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author gjt.
 * @data 2023/6/1 16:52
 */

public class SpecialRoleUserConstant {


    /**
     * 专题用户状态：0开启，1禁用
     */
    public static final Map<Long, String> SPECIAL_ROLE_STATUS_MAP = new ImmutableMap.Builder<Long, String>()
            .put(0L, "开启")
            .put(1L, "禁用")
            .build();
}
