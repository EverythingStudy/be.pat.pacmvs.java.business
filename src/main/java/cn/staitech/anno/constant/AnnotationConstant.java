package cn.staitech.anno.constant;

/**
 * .
 *
 * @author admin
 */
public class AnnotationConstant {
    private AnnotationConstant() {
        throw new IllegalStateException("AnnotationConstant class");
    }

    public static final String ANNO_DIRECT_EXCHANGE = "anno.direct.exchange";

    public static final String SLIDE_ANNOTATION_RESULT_ROUTING = "slide.annotation.result.routing";

    public static final String MULTIPOLYGON = "MultiPolygon";

    public static final String GEOMETRYCOLLECTION = "GeometryCollection";

    public static final Integer CODE = 200;

    public static final String ADD_ANNOTATION = "添加标注成功";

    public static final String ADD_STATUS = "add";

    public static final String UNION = "UNION";

    public static final String DIFFERENCE = "DIFFERENCE";

    public static final String UPDATE_ANNOTATION = "修改标注成功";

    public static final String UPDATE_STATUS = "update";

    public static final String point_count = "point_count";

    public static final String DELETE_ANNOTATION = "删除标注成功";

    public static final String DELETE_STATUS = "delete";

    public static final String UPDATE_DESCRIPTION = "修改描述成功";

//    public static final String UPDATE_ANNOTATION_CATEGORY_MESSAGE = "此图像已完成标注,不可对该图像进行操作";

    public static final String NOT_PERMISSION = "当前用户无权限";

    public static final String UPDATE_ANNOTATION_CATEGORY = "标注类别修改成功";

    public static final Integer ANNOTATION_STATUS = 3;

    public static final String NOTNULLROI = "暂无可删除的Anno轮廓";

    public static final Integer PAGE_SIZE = 1000;

    public static final String NOTEXISTCATEGORY = "项目尚未添加该标注";

    public static final String DISTANCENOTRIGHT = "请输入正确的distance";
}
