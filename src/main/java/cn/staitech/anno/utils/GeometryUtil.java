package cn.staitech.anno.utils;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

/**
 * GeometryUtil工具类
 * @author admin
 */
public class GeometryUtil {
    
    /**
     * 初始化WKTReader
     * @return
     */
    public static WKTReader initWktReader(){
    
        // GeometryFactory工厂，参数一：数据精度 参数二空间参考系SAID
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
        // 熟知文本WKT阅读器，可以将WKT文本转换为Geometry对象
        WKTReader wktReader = new WKTReader(geometryFactory);
        
        return wktReader;
    }
    
}
