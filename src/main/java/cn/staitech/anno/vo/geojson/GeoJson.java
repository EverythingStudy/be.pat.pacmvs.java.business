package cn.staitech.anno.vo.geojson;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GeoJson {

    @ApiModelProperty(value = "type")
    private String type = "FeatureCollection";

    @ApiModelProperty(value = "标注数据")
    private List<Features> features;

    @ApiModelProperty(value = "项目信息")
    private GeoProject project;

    @ApiModelProperty(value = "图片信息")
    private GeoImage image;

    @ApiModelProperty(value = "标签信息")
    private List<GeoLabel> label_info;

    @ApiModelProperty(value = "作者信息")
    private GeoAttribute attribute;


}
