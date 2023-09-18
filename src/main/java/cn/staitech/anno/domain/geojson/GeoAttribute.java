package cn.staitech.anno.domain.geojson;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GeoAttribute {

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "部门")
    private String department;
}
