package cn.staitech.anno.domain.geojson;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GeoLabel {

    @ApiModelProperty(value = "标签名称")
    private String label_name;

    @ApiModelProperty(value = "标签颜色")
    private String label_color;

    @ApiModelProperty(value = "结构编码")
    private String label_code;


}
