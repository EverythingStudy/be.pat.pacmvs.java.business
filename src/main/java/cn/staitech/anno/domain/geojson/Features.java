package cn.staitech.anno.domain.geojson;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Features {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "type")
    private String type = "Feature";

    @ApiModelProperty(value = "标注坐标")
    private JSONObject geometry;

    @ApiModelProperty(value = "type")
    private Properties properties;











































}
