package cn.staitech.anno.vo.geojson;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GeoProject {

    @ApiModelProperty(value = "项目id")
    private String project_id;

    @ApiModelProperty(value = "项目名称")
    private String project_name;
}
