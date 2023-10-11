package cn.staitech.anno.constant;

/**
 * @author admin
 */
public class PathologicalLogConstant {
    private PathologicalLogConstant() {
        throw new IllegalStateException("PathologicalLogConstant class");
    }

    /**
     * 当前病理指标下的该标签名称已经存在,请修改
     */
    public static final String CATEGORY_NAME_EXIST = "标签的结构编码、结构名称、图层顺序、颜色值在当前列表内不可重复,请修改";

    public static final String COLOR_NAME_EXIST = "当前颜色已被占用,请重新选择颜色";

    public static final String UNABLE_TO_DELETE = "关联数据未清理，不可删除";

    public static final String NOT_IMAGE = "未发现图像信息";

    public static final String MISSING_REQUIRED_VALUE = "未发现图像信息";

    public static final String LAYER_ALREADY_EXISTS = "当前图层已被占用，请重新选择图层";

    public static final String ONE = "1";

    public static final String USED = "当前标签正在被使用中，禁止删除";

    public static final String INDICATOR_ABSENT = "结构指标不存在";


}
