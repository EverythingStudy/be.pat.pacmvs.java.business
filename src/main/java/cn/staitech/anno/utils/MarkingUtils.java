package cn.staitech.anno.utils;

import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.Properties;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.operation.overlay.OverlayOp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class MarkingUtils {
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
    private static final WKTReader wktReader = new WKTReader(geometryFactory);

    /**
     * 判断小轮廓防止失误裁剪
     *
     * @param oldLocations
     * @param newLocations
     * @param operation
     * @return
     * @throws Exception
     */
    public static double updateOperationVerify(JSONObject oldLocations, JSONObject newLocations, String operation) throws Exception {
//        try {
        String oldLocation = WktUtil.jsonToWkt(oldLocations);

        String newLocation = WktUtil.jsonToWkt(newLocations);

        double percentage = 0;

        // 校验是否有要执行的操作  相交或者相差
        if (StringUtils.isNotBlank(operation)) {
            Geometry geometry1;
            try {
                geometry1 = wktReader.read(oldLocation);
            } catch (Exception e) {
                throw new Exception("图形不符合规则");
            }
            Geometry geometry2;
            try {
                geometry2 = wktReader.read(newLocation);
            } catch (Exception e) {
                throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
            }
            OverlayOp op = new OverlayOp(geometry1, geometry2);
            int code = 0;
            // 如果操作为相交
            String geometryType = geometry2.getGeometryType();
            // 判断新增图形是否为混合类型
            if (!"GeometryCollection".equals(geometryType)) {
                // 判断是否自相交
                try {
                    geometry2.union(geometry2);
                } catch (Exception e) {
                    throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
                }
            }
            // 判断新增图形是否为多聚体
            if ("MultiPolygon".equals(geometryType)) {
                throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
            }
            if ("UNION".equals(operation)) {
                code = OverlayOp.UNION;
                // 操作为相差
            } else if ("DIFFERENCE".equals(operation)) {
                code = OverlayOp.DIFFERENCE;
                // 校验旧图形在新图形中(新图形不能将旧图形完全覆盖)
                if (geometry1.within(geometry2)) {
                    throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
                }
                try {
                    Geometry geometryIntersection = geometry1.intersection(geometry2);
                    // 判断图形是否有交集
                    if (geometryIntersection.isEmpty()) {
                        throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
                    }
                } catch (Exception e) {
                    throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
                }
            }
            percentage = geometry2.getArea() / geometry1.getArea();
        }
        return percentage;

//        } catch (Exception e) {
//            throw new Exception("图形不符合规则");
//        }

    }


    /**
     * @param oldLocations
     * @param newLocations
     * @param operation
     * @param check        true 校验，false校验
     * @return
     * @throws Exception
     */
    public static String updateVerify(JSONObject oldLocations, JSONObject newLocations, String operation, boolean check) throws Exception {
//        try {

        String oldLocation = WktUtil.jsonToWkt(oldLocations);
        String newLocation = WktUtil.jsonToWkt(newLocations);
        // WKT输出器，将Geometry对象写出为WKT文本
        WKTWriter wktWriter = new WKTWriter();
        String data = newLocation;
        // 校验是否有要执行的操作  相交或者相差
        if (StringUtils.isNotBlank(operation)) {
            Geometry geometry1;
            try {
                geometry1 = wktReader.read(oldLocation);
            } catch (Exception e) {
                throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
            }
            Geometry geometry2;
            try {
                geometry2 = wktReader.read(newLocation);
            } catch (Exception e) {
                throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
            }
            if (check) {
                String geometryType = geometry2.getGeometryType();
                // 判断新增图形是否为混合类型
                if (!"GeometryCollection".equals(geometryType)) {
                    // 判断是否自相交
                    try {
                        geometry2.union(geometry2);
                    } catch (Exception e) {
                        throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
                    }
                }
                // 判断新增图形是否为多聚体
                if ("MultiPolygon".equals(geometryType)) {
                    throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
                }
            }
            OverlayOp op = new OverlayOp(geometry1, geometry2);
            int code = 0;
            // 取出交集图形
            if (check) {
                Geometry geometryIntersection = geometry1.intersection(geometry2);
                // 判断图形是否有交集
                if (geometryIntersection.isEmpty()) {
                    throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
                }
            }
            // 如果操作为相交
            if ("UNION".equals(operation)) {
                code = OverlayOp.UNION;

                // 操作为相差
            } else if ("DIFFERENCE".equals(operation)) {
                code = OverlayOp.DIFFERENCE;
                if (check) {
                    // 校验旧图形在新图形中(新图形不能将旧图形完全覆盖)
                    if (geometry1.within(geometry2)) {
                        // throw new AnnoException(AnnotationResponseConstant.UPDATE_ANNO_ERROR);
                        throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));// 修改失败,请检查后输入
                    }
                    // 校验标注不能过小，不能小于1000.0
//                        if (geometry2.within(geometry1) && geometry2.getArea() < insideMaxArea) {
//                            throw new AnnoException(AnnotationResponseConstant.UPDATE_ANNO_ERROR + geometry2.getArea());
//                        }
                }

            }
            // 返回结果
            Geometry geometry;
            try {
                // code=OverlayOp.UNION;相交  或者  code=OverlayOp.DIFFERENCE;相差
                // 将code转换成Geometry对象
                geometry = op.getResultGeometry(code);
                // 获取geometry类型
                String geometryType = geometry.getGeometryType();
                // TODO：校验飞点 后续可写多种校验策略放入线程池中
/*                if ("Polygon".equals(geometryType)) {
                    geometry = removePolygonPoint(geometry.getCoordinates());
                }*/

                // 判断新图形是否为复杂多边型(比如大标注嵌套小标注
                if ("MultiPolygon".equals(geometryType)) {
                    // throw new AnnoException(AnnotationResponseConstant.NEW_GRAPHICS_MARK_NOT_RULES);
                    throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));// 新图形不符合规则
                }
            } catch (Exception e) {
                throw new Exception(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
            }
            data = wktWriter.write(geometry);
        }


        return data;

//        } catch (Exception e) {
//            throw new Exception("图形不符合规则");
//        }

    }

    /**
     * 剔除不规则点：
     *
     * @param geometry
     * @return
     */
    public static JSONObject updatePolygonPoint(JSONObject geometry) {
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        List<Object> lists = new ArrayList<>();
        JSONArray coordinatesJsonArray1 = geometry.getJSONArray("coordinates");
        String type = geometry.getString("type");
        if (Objects.equals(type, "Polygon")) {
            List<Object> list1 = new ArrayList<>();
            for (Object i1 : coordinatesJsonArray1) {
                JSONArray jsonArray1 = JSONArray.parseArray(i1.toString());
                // 定义一个变量
                Double x = null;
                Double y = null;
                for (Object i2 : jsonArray1) {
                    JSONArray jsonArray2 = JSONArray.parseArray(i2.toString());
                    List<Double> list = JSONObject.parseArray(jsonArray2.toJSONString(), Double.class);
                    List<Double> newList = new ArrayList<>();
                    double newX = 0;
                    double newY = 0;
                    if (x != null) {
                        // 取出绝对值
                        newX = Math.abs(x - list.get(0));
                    }
                    xList.add(list.get(0));
                    yList.add(list.get(1));
                    if (y != null) {
                        newY = Math.abs(y - list.get(1));
                    }
                    if (newX < 100 && newY < 100) {
                        newList.add(list.get(0));
                        newList.add(list.get(1));
                        list1.add(newList);
                        x = list.get(0);
                        y = list.get(1);
                    }
                }
            }
            lists.add(list1);
        }
        JSONObject geometryJson = new JSONObject();
        geometryJson.put("type", type);
        geometryJson.put("coordinates", lists);
        return geometryJson;
    }

    /**
     * 剔除不规则点：剔除数值明显过大或过小的值，暂定踢除x,y中绝对值大于50000的点
     * 例如点存在科学记数法 (47713.255571202826, -6.989704038477149E14, NaN)
     *
     * @param coordinates Coordinate数组
     * @return
     */
    public static Geometry removePolygonPoint(Coordinate[] coordinates) {
        int length = coordinates.length;
        Coordinate[] tmpCoordinates = new Coordinate[length];
        int distLength = 0;
        for (int i = 0; i < coordinates.length; i++) {
            if (Math.abs(coordinates[i].x) > 50000 || Math.abs(coordinates[i].y) > 50000) {
                log.info("{} {}", i, coordinates[i]);
                continue;
            }
            tmpCoordinates[distLength] = coordinates[i];
            distLength++;
        }

        Coordinate[] distCoordinates = new Coordinate[distLength];
        System.arraycopy(tmpCoordinates, 0, distCoordinates, 0, distLength);

        return geometryFactory.createPolygon(distCoordinates);
    }


    public static Features socketData(String annotationId, JSONObject geometry, Properties properties) {
        Features features = new Features();
        features.setGeometry(geometry);
        features.setId(annotationId);
        features.setType("Feature");
        JSONObject jsonObject = (JSONObject) JSON.toJSON(properties);
        features.setProperties(jsonObject);
        return features;
    }


}
