package cn.staitech.anno.constant;

/**
 * @author wangf
 */
public class ProjectConstant {
    public static final String UNABLE_TO_DELETE = "关联数据未清理，不可删除";
    public static final String IMAGE_UPLOADED_ANNOTATION = "：该图像已有上传标注、不可上传";
    public static final String ANNOTATION_CATEGORY = "标注类别";
    public static final String NON_EXISTENT = "不存在，请添加后重试";
    public static final String INCORRECT_FORMAT = "请上传正确格式的json文件！";
    public static final String INDICATOR_DATA = "未发现病理指标";
    public static final String PROJECT_EXIST = "项目名已存在，请检查后输入";
    public static final String NOT_IMAGE = "未选择图像";
    public static final String NO_ATTRIBUTE = "无属性";
    public static final String IMAGE_NOT_EXIST = "图像不存在";
    public static final String SLIDE_ID_NOT_NULL = "切片id不可为空";

    public static final String INDICATOR_EXIST = "病理指标名已存在，请检查后输入";
    public static final String ADD_INDICATOR_ERROR = "病理指标添加失败";

    public static final String STATUS_ERROR = "状态值错误";
    public static final String SERIAL_NO = "序号:";
    public static final String IN_DIMENSION = "图片标注中，不能删除";
    public static final String DIMENSIONING_COMPLETE = "图片标注完成，不能删除";
    public static final String SUBMITTED_FOR_REVIEW = "图片已提交复核，不能删除";
    public static final String STRING_ADD_COMPLETE = "添加完成";
    public static final String NO_DATA_TRANSFERRED = "没传数据";
    public static final String ID_IS = "ID为:";
    public static final String PICTURE_NON_EXISTENT = "图片数据不存在";
    public static final String ADDED = "图片已添加过";
    public static final String MEMBER = "成员";
    public static final String PROPORTION = "占比";
    public static final String PROJECT_SLICE_DATA = "项目切片数据";
    public static final String LABEL_QUANTITY = "标注数量";
    public static final String PROPORTION_OF_MARKED_QUANTITY = "标注数量占比";
    public static final String NEW_ANNOTATION_PLATFORM = "新标注平台";
    public static final String IMAGE_ID = "图像id";
    public static final String IMAGE_NAME = "图像名称";
    public static final String PLATFORM_NAME = "平台名称";
    public static final String ENTRY_NAME = "项目名称";
    public static final String TOTAL_NUMBER_OF_IMAGE_ANNOTATIONS = "图像标注总数";
    public static final String NUMBER = "数量";
    public static final String ALREADY_ADD = "已添加";
    public static final String NOT_ADDED = "未添加";
    public static final String NOT_CHANGE = "没有变动";
    public static final String MODIFIED_SUCCESSFULLY = "项目名称修改成功";

    /**************
     * 项目模块字典值
     *************/
    public static final String PROJECT_NAME_EXIST = "当前专题下已存在该项目，请勿重复添加";
    public static final String VISCUS_CODE_EXIST = "当前脏器已存在，请勿重复添加";
    public static final String PROJECT_SLIDE_EXIST = "当前项目已绑定切片，禁止删除";
    public static final String PROJECT_GROUP_SLIDE_EXIST = "该项目分组下有绑定切片，请清空切片后再删除";
    public static final String PROJECT_NO_READY = "当前项目内有切片未分析完成，暂无法生成组间报告";
    public static final String PROJECT_SLIDE_RUNNING = "该组内的切片正在分析中，禁止清空";
    public static final String PROJECT_BATCH_INSERT = "一键创建项目异常";
    public static final String SPECIAL_NON_DELIVERY = "当前切片状态不满足一键创建，请先交付切片";
    public static final String SPECIAL_EXIST_PROJECT = "当前项目列表已进行过手工操作，禁止一键创建";

    public static final String AUTO_CREATE_REASON = "上游数据问题，一键创建项目异常";
    public static final String SPECIAL_EXIST_NON_DELIVERY = "专题未交付";
    public static final String SPECIAL_NOTEXIST_GROUP = "专题内不存在分组";

}
