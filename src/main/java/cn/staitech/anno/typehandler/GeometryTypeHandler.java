package cn.staitech.anno.typehandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 *
 * @author
 *
 */
@SuppressWarnings("rawtypes")
@MappedJdbcTypes(value = JdbcType.OTHER)
@MappedTypes(value = {Geometry.class})
public class GeometryTypeHandler extends BaseTypeHandler<Geometry> {
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Geometry parameter, JdbcType jdbcType)
            throws SQLException {
    }
    
    @Override
    public Geometry getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            return fromMysqlWkb(rs.getBytes(columnName));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Geometry getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            return fromMysqlWkb(rs.getBytes(columnIndex));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Geometry getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            return fromMysqlWkb(cs.getBytes(columnIndex));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * java如何操作gis geometry类型数据
     * https://www.yisu.com/zixun/690495.html
     * @param bytes
     * @return
     * @throws ParseException
     */
    private Geometry fromMysqlWkb(byte[] bytes) throws ParseException {
        if (bytes == null) {
            return null;
        }
        byte[] geomBytes = ByteBuffer.allocate(bytes.length - 4).order(ByteOrder.LITTLE_ENDIAN)
                .put(bytes, 4, bytes.length - 4).array();
        
        //use the JTS WKBReader for WKB parsing
        WKBReader wkbReader = new WKBReader();
        
        // 使用geotool的WKBReader 把字节数组转成geometry对象。
        return wkbReader.read(geomBytes);
    }
}
