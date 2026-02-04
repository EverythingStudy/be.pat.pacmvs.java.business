package cn.staitech.annotation.netty.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

//比较特殊的类，需要忽略geometry字段
@Data
public class AnnotationFeatureLogMixin {

    @JsonIgnore
    private Geometry geometry;
}
