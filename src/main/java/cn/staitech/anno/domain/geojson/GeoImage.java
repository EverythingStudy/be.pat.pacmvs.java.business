package cn.staitech.anno.domain.geojson;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GeoImage {

    @ApiModelProperty(value = "图像id")
    private String image_id;

    @ApiModelProperty(value = "图像名称")
    private String image_name;

    @ApiModelProperty(value = "图像类型")
    private String image_type;

    @ApiModelProperty(value = "图像形状")
    private String image_shape;

    @ApiModelProperty(value = "标注类型")
    private List<String> flow_path;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
}
