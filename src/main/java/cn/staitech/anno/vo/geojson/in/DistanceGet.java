package cn.staitech.anno.vo.geojson.in;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistanceGet {


    @ApiModelProperty(value = "标注id", required = true)
    private String annotationIdOne;

    @ApiModelProperty(value = "标注id", required = true)
    private String annotationIdTwo;

    @ApiModelProperty(value = "标注类型")
    private String annotationTypeOne;

    @ApiModelProperty(value = "标注类型")
    private String annotationTypeTwo;

    @ApiModelProperty(value = "轮廓类型")
    private Long contourTypeOne;

    @ApiModelProperty(value = "轮廓类型")
    private Long contourTypeTwo;

    @ApiModelProperty(value = "专题id", required = true)
    private Long specialId;



}
