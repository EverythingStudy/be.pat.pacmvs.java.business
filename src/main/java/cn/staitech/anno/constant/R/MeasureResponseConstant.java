package cn.staitech.anno.constant.R;

/**
 * @author admin
 */
public class MeasureResponseConstant {
    public static final String ARGUMENT_INVALID = "参数异常";
    public static final String FILE_CONNECTOR = "_";
    public static final String FILE_SUFFIX_TXT = ".txt";
    public static final String NO_FILE = "未上传文件";
    public static final String DOWNLOAD_ERROR = "下载过程发生异常";
    public static final String UPLOAD_SUCCESS = "导入成功";


    public static final String EXCEL_TITLE = "标注测量详情";

    // 构造表头的每个列头
    // 名称 周长/长度 面积 内角 外角 平均间距 最小间距 最大间距 总数 测量人 创建时间
    public static final String[] COLHEAD_KEY = {"名称", "周长/长度", "面积", "内角", "外角", "平均间距", "最小间距", "最大间距", "总数", "测量人", "创建时间"};
    public static final String[] COLHEAD_VALUE = {"measure_full_name", "perimeter", "area", "inner_angle", "exterior_angle", "mean_distance", "min_distance", "max_distance", "point_count", "annotation_owner", "create_time"};

}
