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

    /**
     * cache key
     */
    public static final String IMAGE_CACHE_KEY = "image_cache_key";
    public static final String INDICATOR_CACHE_KEY = "indicator_cache_key";
    public static final String PROJECT_CACHE_KEY = "project_cache_key";

    public static final String NUMBER_0 = "0";
    public static final String NUMBER_1 = "1";

    public static final String GLIDE_LINE = "_";


    public static final String FILE_SUFFIX_JSON = ".json";
    public static final String FILE_SUFFIX_TXT = ".txt";
    public static final String FILE_SUFFIX_XLSX = ".xlsx";

    public static final String PATH = "path";
    public static final String FILE_PATH = "annotation";
    public static final String FILENAME = "filename";
    public static final String IMAGE_URL = "imageUrl";
    public static final String CHARACTER_SET_UTF8 = "UTF-8";
    public static final String CONTENT_TYPE = "application/json;charset=utf-8";
    public static final String HEADER = "Content-Disposition";

    public static final Integer NOT_START_REVIEW = 0;
    public static final Integer SUBMIT_REVIEW = 3;

    /**
     * Project
     */
    public static final String NONE = "none";
    public static final String REGIONS = "regions";
    public static final String REGION_ATTRIBUTES = "region_attributes";
    public static final String BONE_MARROW = "bone_marrow";
    public static final String SHAPE_ATTRIBUTES = "shape_attributes";
    public static final String ALL_POINTS_X = "all_points_x";
    public static final String ALL_POINTS_Y = "all_points_y";
    public static final String NAME = "name";
    public static final String POLYGON = "POLYGON";
    public static final String POINT = "POINT";
    public static final String LINESTRING = "LINESTRING";
    public static final String POLYGON_WITH_HOLES = "polygon_with_holes";
    public static final String CHILDREN_CNTS = "children_cnts";
    public static final String VIA_IMG_METADATA = "_via_img_metadata";
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    public static final Integer NOT_INDICATOR_STATUS = 1;
    public static final Integer INDICATOR_STATUS = 2;
    public static final Integer NOT_ATTRIBUTE_STATUS = 3;
    public static final Long NOT_AUDIT = 0L;
    public static final Long AUTO_CREATE_PROJECT_SUC = 1L;
    public static final Long AUTO_CREATE_PROJECT_FAL = 2L;

    /**
     * Annotation
     */
    public static final String MULTIPOLYGON = "MultiPolygon";
    public static final String GEOMETRYCOLLECTION = "GeometryCollection";
    public static final String ADD_STATUS = "add";
    public static final String UPDATE_STATUS = "update";
    public static final String DELETE_STATUS = "delete";
    public static final String CLEAN = "clean";
    public static final String UNION = "UNION";
    public static final String DIFFERENCE = "DIFFERENCE";

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
     * Special Role
     */
    public static final String RESP = "resp";
    public static final String ANNO = "anno";
    public static final String READ = "read";
    public static final Long[] RESPONSIBLE_MENU = {1001L, 1002L, 1003L, 1004L, 1005L, 1006L, 1007L, 1008L, 1009L, 1010L, 1011L, 1012L, 1013L, 1014L, 1015L, 1016L, 1017L, 1018L, 1019L, 1020L, 1021L, 1022L, 1023L, 1024L, 1025L, 1026L, 1027L, 1028L, 1029L};
    public static final Long[] ANNOTATOR_MENU = {1001L, 1003L, 1004L, 1008L, 1009L, 1010L, 1011L, 1012L, 1013L, 1014L, 1015L};
    public static final Long[] READER_MENU = {1002L, 1005L, 1006L, 1007L, 1016L, 1017L, 1018L, 1019L, 1020L, 1021L, 1022L, 1023L, 1024L, 1025L, 1026L, 1027L, 1028L, 1029L};
    public static final String[] SPECIAL_ROLE_TYPE = new String[]{"专题负责人", "标注员", "普通阅片员"};

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

    /**
     * TODO:统计模块switch case语句中用到，多语言版本暂未处理，后续建议优化
     */
    public static final String ANNOTATION_DATE = "标注日期";

    public static final String PROJECT = "项目";
    public static final String PATHOLOGY_INDICATOR = "病理指标";
    public static final String ANNOTATION_CATEGORY = "标注类别";
    public static final String USER = "成员";
    public static final String SLIDE = "图像";
}
