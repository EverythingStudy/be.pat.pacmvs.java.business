package cn.staitech.anno.project.constants;

import cn.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description 常量
 * @date 2023/9/14 09:44:28
 */
public class Constants {
    /**
     * 下载任务状态：1、运行中，2、完成
     */
    public static final String DOWN_STATE_RUNNING = "1";
    public static final String DOWN_STATE_FINISH = "2";

    public static final Map<String, String> STATUS = MapUtil.builder(new HashMap<String, String>())
            .put("1", "未开始").put("2", "标注中")
            .put("3", "标注完成").put("4", "未复核")
            .put("5", "复核中").put("6", "已复核")
            .put("7", "已交付").build();

    public static final Map<String, String> STATUS_EN = MapUtil.builder(new HashMap<String, String>())
            .put("1", "未开始en").put("2", "标注中en")
            .put("3", "标注完成en").put("4", "未复核en")
            .put("5", "复核中en").put("6", "已复核en")
            .put("7", "已交付en").build();
}
