package cn.staitech.annotation.netty.message;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 筛差
 */
@Data
public class AnnotationSdFeature {
    private String id;
    private String type = "Feature";
    private JSONObject geometry;
    private AnnotationProperties properties;
}
