package cn.staitech.anno.constant;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map
 *
 * @author wangf
 */
public class Container {

    /**
     * FileUpload - 定义一个基于多线程 的 hashmap
     */
    public static final Map<String, ArrayList<Integer>> FILE_MAP = new ConcurrentHashMap<>();


    /**
     * Image - 图片处理过程状态 不可用原因共三种：0上传失败，1解析中,2解析失败
     */
    public static final Map<Integer, String> IMAGE_PROCESS_MAP = new ImmutableMap.Builder<Integer, String>()
            .put(0, "上传失败")
            .put(1, "解析中")
            .put(2, "解析失败")
            .build();

    /**
     * Image - 图片处理过程状态 不可用原因共三种：0上传失败，1解析中,2解析失败
     */
    public static final Map<Integer, String> IMAGE_PROCESS_MAP_EN = new ImmutableMap.Builder<Integer, String>()
            .put(0, "UPLOAD FAILED")
            .put(1, "IN ANALYSIS")
            .put(2, "ANALYSIS FAILED")
            .build();

    /**
     * Image - 可用状态：0不可用1可用
     */
    public static final Map<Integer, String> IMAGE_STATUS_MAP = new ImmutableMap.Builder<Integer, String>()
            .put(0, "不可用")
            .put(1, "可用")
            .build();

    /**
     * Image - EN - 可用状态：0不可用1可用
     */
    public static final Map<Integer, String> IMAGE_STATUS_MAP_EN = new ImmutableMap.Builder<Integer, String>()
            .put(0, "Unavailable")
            .put(1, "Available")
            .build();


    /**
     * Image - 图片删除状态：0逻辑删除，1未逻辑删除
     */
    public static final Map<Integer, String> IMAGE_DELETE_FLAG_MAP = new ImmutableMap.Builder<Integer, String>()
            .put(0, "已删除")
            .put(1, "未删除")
            .build();

    /**
     * 项目状态
     */
    public static final Map<Integer, String> PROJECT_STATUS = new ImmutableMap.Builder<Integer, String>()
            .put(1, "待启动")
            .put(2, "进行中")
            .put(3, "暂停")
            .put(4, "已完成")
            .build();

    /**
     * 项目状态 - EN
     */
    public static final Map<Integer, String> PROJECT_STATUS_EN = new ImmutableMap.Builder<Integer, String>()
            .put(1, "Pending Started")
            .put(2, "In process")
            .put(3, "Pause")
            .put(4, "Done")
            .build();

    /**
     * 专题用户状态：0开启，1禁用 SpecialRoleUser
     */
    public static final Map<Long, String> SPECIAL_ROLE_STATUS_MAP = new ImmutableMap.Builder<Long, String>()
            .put(0L, "开启")
            .put(1L, "禁用")
            .build();

    /**
     * 专题用户状态 - EN：0开启，1禁用 SpecialRoleUser
     */
    public static final Map<Long, String> SPECIAL_ROLE_STATUS_MAP_EN = new ImmutableMap.Builder<Long, String>()
            .put(0L, "ON")
            .put(1L, "FORBIDDEN")
            .build();

    /**
     * 颜色类型
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

    /**
     * 颜色类型 - EN
     */
    public static final Map<Integer, String> COLOR_TYPE_EN = new ImmutableMap.Builder<Integer, String>()
            /*.put(1, "RGB")
            .put(2, "HEX")*/
            .put(1, "Immunofluorescence")   // 荧光标记染色
            .put(2, "Immunohistochemical") // 免疫组织化学染色
            .put(3, "HE staining")  // HE染色
            .put(4, "Masson staining")   // Masson染色EN
            .put(5, "Van Gieson staining") // Van Gieson染色
            .put(6, "Victoria Blue staining")  // 维多利亚蓝染色
            .put(7, "Sudan III/IV") // 苏丹III/IV染色
            .put(8, "Oil Red O")    // 油红O染色
            .put(9, "PAS")  // PAS糖原染色
            .put(10, "AB-PAS") // AB-PAS染色
            .put(11, "Congo red")   // 刚果红染色(甲醇)
            .put(12, "toluidine blue")  // 甲苯胺蓝染色
            .put(13, "Prussian blue")   // 普鲁氏蓝染色
            .put(14, "Nissl")   // 尼氏染色
            .put(15, "Luxol Fast Blue myelin")// LFB髓鞘染色
            .put(16, "Tunel")   // Tunel染色
            .put(17, "Ki67")
            /*.put(18, "免疫组织化学染色")
            .put(19, "荧光标记染色")*/
            .put(20, "Other")
            .build();


}
