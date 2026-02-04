package cn.staitech.annotation.netty.message;

import cn.staitech.sft.logaudit.annotation.IgnoreLogField;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2025/5/22 13:50:42
 */
@Data
public class AnnotationFeature {

    @IgnoreLogField
    private String id;
    @IgnoreLogField
    private String type = "Feature";
    @IgnoreLogField
    private Geometry geometry;
    @IgnoreLogField
    private AnnotationProperties properties;

    /**
     * 记录日志
     */
    private String description;
    private String tagIdLog;
    private String encrypt;

}
