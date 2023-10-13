package cn.staitech.anno.constant;

/**
 * .
 *
 * @author admin
 */
public class CommonConstant {

    private CommonConstant() {
        throw new IllegalStateException("CommonConstant class");
    }

    public static final String NUMBER_0 = "0";
    public static final String NUMBER_1 = "1";

    public static final String GLIDE_LINE = "_";


    public static final String FILE_SUFFIX_JSON = ".json";
    public static final String FILE_SUFFIX_TXT = ".txt";
    public static final String FILE_SUFFIX_XLSX = ".xlsx";

    public static final String PATH = "path";
    public static final String FILE_PATH = "annotation";
    public static final String IMAGE_URL = "imageUrl";

    public static final String CHARACTER_ENCODING = "UTF-8";
    public static final String CONTENT_TYPE = "application/json;charset=utf-8";
    public static final String HEADER = "Content-Disposition";

    public static final Integer NOT_START_REVIEW = 0;
    public static final Integer SUBMIT_REVIEW = 3;

    /**
     * Viewer
     */
    public static final Double MICRON = 0.26;

    /**
     * Statistic
     */
    public static final int THIRTEEN_DAY = 13;
    public static final int THIRTY_ONE_DAY = 31;
    public static final int ONE_YEAR = 366;
    public static final int THREE_YEAR = 1096;

    /**
     * ProjectRole:构造3个默认项目角色类型：1、项目代表；2、项目管理者；3、项目贡献者
     */
    public static final String[] ROLE_TYPE = new String[]{"项目代表", "项目管理者", "项目贡献者"};

    /**
     * Excel表头 - ExamineScore
     */
    public static final String[] EXAMINESCORE_COLHEAD_KEY = {"项目名称", "切片编号", "答题者", "开始时间", "交卷时间", "应标个数(下限)", "实标个数", "miou拟合区间", "fiou拟合区间", "biou拟合区间", "tiou拟合区间", "个人拟合度", "考核结果"};
    public static final String[] EXAMINESCORE_COLHEAD_VALUE = {"projectName", "imageName", "nickName", "startTime", "endTime", "shouldNumber", "realityNumber", "miou", "fiou", "biou", "tiou", "personalFit", "examResults"};


    /**
     * Excel表头 - Measure - 构造表头的每个列头：名称 周长/长度 面积 内角 外角 平均间距 最小间距 最大间距 总数 测量人 创建时间
     */
    public static final String[] MEASURE_COLHEAD_KEY = {"名称", "周长/长度", "面积", "内角", "外角", "平均间距", "最小间距", "最大间距", "总数", "测量人", "创建时间"};
    public static final String[] MEASURE_COLHEAD_VALUE = {"measure_full_name", "perimeter", "area", "inner_angle", "exterior_angle", "mean_distance", "min_distance", "max_distance", "point_count", "annotation_owner", "create_time"};

    /**
     * Excel表头 - Export
     */
    public static final String[] EXPORT_COLHEAD_KEY = {"文件路径", "图像路径"};
    public static final String[] EXPORT_COLHEAD_VALUE = {"path", "imageUrl"};
}
