package cn.staitech.anno.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.geo.FeatureCollection;
import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;

import org.json.*; // JSON库
import com.vividsolutions.jts.geom.*; // GeoTools库

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: WktUtil
 * @Description:WKT格式的Geomotry和GeoJSON互换工具
 * @date 2023年6月21日
 */
public class WktUtil {
    private static final String GEO_JSON_TYPE = "GeometryCollection";

    public static String wktToJson(String wkt) {
        String json = null;
        try {
            WKTReader reader = new WKTReader();
            Geometry geometry = reader.read(wkt);
            StringWriter writer = new StringWriter();
            GeometryJSON g = new GeometryJSON(20);
            g.write(geometry, writer);
            json = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String jsonToWkt(JSONObject jsonObject) {
        String wkt = null;
        String type = jsonObject.getString("type");
        GeometryJSON gJson = new GeometryJSON(6);
        try {
            // {"geometries":[{"coordinates":[4,6],"type":"Point"},{"coordinates":[[4,6],[7,10]],"type":"LineString"}],"type":"GeometryCollection"}
            if (WktUtil.GEO_JSON_TYPE.equals(type)) {
                // 由于解析上面的json语句会出现这个geometries属性没有采用以下办法
                JSONArray geometriesArray = jsonObject.getJSONArray("geometries");
                // 定义一个数组装图形对象
                int size = geometriesArray.size();
                Geometry[] geometries = new Geometry[size];
                for (int i = 0; i < size; i++) {
                    String str = geometriesArray.get(i).toString();
                    // 使用GeoUtil去读取str
                    Reader reader = GeoJSONUtil.toReader(str);
                    Geometry geometry = gJson.read(reader);
                    geometries[i] = geometry;
                }
                GeometryCollection geometryCollection = new GeometryCollection(geometries, new GeometryFactory());
                wkt = geometryCollection.toText();
            } else {
                Reader reader = GeoJSONUtil.toReader(jsonObject.toString());
                Geometry read = gJson.read(reader);
                wkt = read.toText();
            }

        } catch (IOException e) {
            System.out.println("GeoJson转WKT出现异常");
            e.printStackTrace();
        }
        return wkt;
    }







    private static double[] getCoordinatesFromJsonArray(JSONArray jsonArr) throws JSONException {
        return new double[]{jsonArr.getDouble(0), jsonArr.getDouble(1)};
    }

    private static List<List<double[]>> getPolygonPointsFromJsonArrays(JSONArray jsonArr, GeometryFactory factory) throws JSONException {
        List<List<double[]>> points = new ArrayList<>();

        for (int i = 0; i < jsonArr.size(); ++i) {
            List<double[]> ringPoints = new ArrayList<>();

            JSONArray ringArr = jsonArr.getJSONArray(i);
            for (int j = 0; j < ringArr.size(); ++j) {
                double[] coord = getCoordinatesFromJsonArray(ringArr.getJSONArray(j));

                ringPoints.add(coord);
            }

            points.add(ringPoints);
        }

        return points;
    }

    private static Polygon createPolygonFromRings(List<List<double[]>> rings, GeometryFactory factory) {
        LinearRing shell = null;
        List<LinearRing> holes = new ArrayList<>();

        for (List<double[]> ring : rings) {
            CoordinateSequence seq = factory.getCoordinateSequenceFactory().create((Coordinate[]) ring.toArray());
            LineString lineString = factory.createLineString(seq);

            if (shell == null) {
                shell = factory.createLinearRing(lineString.getCoordinates());
            } else {
                holes.add((LinearRing) lineString);
            }
        }

        return factory.createPolygon(shell, holes.toArray(new LinearRing[holes.size()]));
    }




}