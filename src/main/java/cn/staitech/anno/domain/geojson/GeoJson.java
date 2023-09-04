package cn.staitech.anno.domain.geojson;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GeoJson {

    @ApiModelProperty(value = "type")
    private String type;

    @ApiModelProperty(value = "标注数据")
    private List<Features> features;

    @ApiModelProperty(value = "项目信息")
    private GeoProject project;

    @ApiModelProperty(value = "图片信息")
    private GeoProject image;

    @ApiModelProperty(value = "标签信息")
    private List<GeoLabel> label_info;



}
