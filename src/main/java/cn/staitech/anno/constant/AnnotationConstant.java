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
    public static final String MULTIPOLYGON = "MultiPolygon";

    public static final String GEOMETRYCOLLECTION = "GeometryCollection";

    public static final String ADD_STATUS = "add";

    public static final String UNION = "UNION";

    public static final String DIFFERENCE = "DIFFERENCE";
    public static final String UPDATE_STATUS = "update";

    public static final String point_count = "point_count";

    public static final String DELETE_STATUS = "delete";

    public static final String CLEAN = "clean";

}
