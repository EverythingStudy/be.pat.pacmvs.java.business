package cn.staitech.anno.constant.R;

/**
 * @author admin
 */
public class MeasureResponseConstant {
    public static final String ARGUMENT_INVALID = "参数异常";
    public static final String FILE_PREFIX = "measure_";
    public static final String FILE_CONNECTOR = "_";
    public static final String FILE_SUFFIX_JSON = ".json";
    public static final String FILE_SUFFIX_TXT = ".txt";
    public static final String FILE_SUFFIX_EXCEL = ".xlsx";
    public static final String NO_FILE = "未上传文件";
    public static final String DOWNLOAD_SUCCESS = "下载完成";
    public static final String DOWNLOAD_ERROR = "下载过程发生异常";
    public static final String DOWNLOAD_STREAM_ERROR = "下载关流失败";
    public static final String UPLOAD_SUCCESS = "导入成功";
    public static final String UPLOAD_FAILED = "导入失败";
    public static final String INVALID = "您没有参与该项目不能操作";
    /**
     * 导入JSON文件，最大字节数，取50M  = 52428800 字节
     * 1G=1024MB，1MB=1024KB，1KB=1024字节
     */
    public static final int FILE_MAX_SIZE = 50 * 1024 * 1024;

    public static final String FILE_MAX_SIZE_INVALID = "文件太大，不能解析";
    public static final String EXCEL_TITLE = "标注测量详情";
    public static final String IMAGEID_NOT_EXISTS = "Json文件和该图像不匹配";


    // 构造表头的每个列头
    // 名称 周长/长度 面积 内角 外角 平均间距 最小间距 最大间距 总数 测量人 创建时间
    public static final String[] COLHEAD_KEY = {"名称", "周长/长度", "面积", "内角", "外角", "平均间距", "最小间距", "最大间距", "总数", "测量人", "创建时间"};
    public static final String[] COLHEAD_VALUE = {"measureName", "perimeter", "area", "innerAngle", "exteriorAngle", "meanDistance", "minDistance", "maxDistance", "pointCount", "userName", "createTime"};

    public static final String MEASURE_NUM = "measureNum:";
    public static final String SLIDE_LINE = "_";

    public static final String NOT_MEASURE = "未检测到测量信息";
}
