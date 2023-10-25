package cn.staitech.anno.utils;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.exception.AnnoException;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.operation.overlay.OverlayOp;
import org.apache.commons.lang3.StringUtils;


/**
 * @author
 */
public class MarkVerify {

    /**
     * 初始化熟知文本WKT阅读器，可以将WKT文本转换为Geometry对象
     */
    public static WKTReader wktReader = GeometryUtil.initWktReader();

    /**
     * @param location 标注位置
     * @return null
     * @throws AnnoException AnnoException
     */
    public static Geometry addVerify(String location) throws AnnoException {
        Geometry geometry;
        try {
            // 使用WKT将字符串location转换为geometry对象
            geometry = wktReader.read(location);
            // 获取geometry对象类型
            String geometryType = geometry.getGeometryType();
            // 判断location是否为混合类型
            if (!CommonConstant.GEOMETRYCOLLECTION.equals(geometryType)) {
                // 判断是否为复杂多边形
                geometry.union(geometry);
            }
            // 判断新增图形是否为多聚体
            if (CommonConstant.MULTIPOLYGON.equals(geometryType)) {
                throw new AnnoException(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
            }
        } catch (Exception e) {
            throw new AnnoException(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
        }
        return geometry;
    }

    /**
     * @param location    旧图形
     * @param newLocation 新图形
     * @param operation   操作
     * @return 新旧图像合并后的图形
     * @throws AnnoException AnnoException
     */
    public static String updateVerify(String location, String newLocation, String operation) throws AnnoException {
        // WKT输出器，将Geometry对象写出为WKT文本
        WKTWriter wktWriter = new WKTWriter();

        String data = newLocation;

        // 校验是否有要执行的操作  相交或者相差
        if (StringUtils.isNotBlank(operation)) {
            Geometry geometry1;
            try {
                geometry1 = wktReader.read(location);
            } catch (Exception e) {
                // throw new AnnoException(AnnotationResponseConstant.ORIGINALLY_GRAPHICS_MARK_NOT_RULES);
                return "3";
            }
            Geometry geometry2;
            try {
                geometry2 = wktReader.read(newLocation);
            } catch (Exception e) {
                //throw new AnnoException(AnnotationResponseConstant.NEW_GRAPHICS_MARK_NOT_RULES);
                return "2";
            }
            OverlayOp op = new OverlayOp(geometry1, geometry2);
            int code = 0;
            // 如果操作为相交
            if (CommonConstant.UNION.equals(operation)) {
                code = OverlayOp.UNION;

                // 操作为相差
            } else if (CommonConstant.DIFFERENCE.equals(operation)) {
                code = OverlayOp.DIFFERENCE;

                // 校验旧图形在新图形中(新图形不能将旧图形完全覆盖)
                if (geometry1.within(geometry2)) {
                    // 修改失败,请检查后输入
                    return "1";
                }
            }
            Geometry g;

            try {
                // code=OverlayOp.UNION;相交  或者  code=OverlayOp.DIFFERENCE;相差
                // 将code转换成Geometry对象
                g = op.getResultGeometry(code);
                // 获取geometry类型
                String geometryType = g.getGeometryType();
                // 判断新图形是否为复杂多边型(比如大标注嵌套小标注
                if (CommonConstant.MULTIPOLYGON.equals(geometryType)) {
                    // 新图形不符合规则
                    return "0";
                }
            } catch (Exception e) {
                throw new AnnoException(MessageSource.M("NEW_GRAPHICS_MARK_NOT_RULES"));
            }
            data = wktWriter.write(g);
        }
        return data;
    }
}
