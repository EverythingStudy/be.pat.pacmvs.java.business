package cn.staitech.anno.constant;

import com.google.common.collect.ImmutableMap;

import java.util.Map;


/**
 * 图片相关状态
 *
 * @author wangf
 */
public class ImageConstant {

    public static final String OPERATE_SUCCEED = "操作成功";

    public static final String OPERATE_ERROR = "操作失败";
    public static final String IMAGE_USING_FORBID_DELETE = "该切片正在使用中，不可删除。";

    public static final Integer INVISIBLE = 2;

    public static final String AVAILABLE = "可用";

    public static final String NOT_AVAILABLE = "不可用";

    public static final String PROCESSING = "处理中";

    public static final String ALREADY_TRANSMIT = "已传输";

    public static final String NOT_TRANSMIT = "未传输";

    public static final String TRANSMIT_IMAGE_ERROR = "传输图像不能为空，请选择要传输的图像";

    public static final String IMAGE_TRANSMIT_NOT_DELETE = "该图像已经完成传输,不可进行删除";

    public static final String FILE_DELETE_SUCCEED = "文件删除成功";


    /**
     * 图片处理过程状态 不可用原因共三种：0上传失败，1解析中,2解析失败
     */
    public static final Map<Integer, String> IMAGE_PROCESS_MAP = new ImmutableMap.Builder<Integer, String>()
            .put(0, "上传失败")
            .put(1, "解析中")
            .put(2, "解析失败")
            .build();

    /**
     * 可用状态：0不可用1可用
     */
    public static final Map<Integer, String> IMAGE_STATUS_MAP = new ImmutableMap.Builder<Integer, String>()
            .put(0, "不可用")
            .put(1, "可用")
            .build();

    /**
     * 图片删除状态：0逻辑删除，1未逻辑删除
     */
    public static final Map<Integer, String> IMAGE_DELETE_FLAG_MAP = new ImmutableMap.Builder<Integer, String>()
            .put(0, "已删除")
            .put(1, "未删除")
            .build();
}
