package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class AnnotationJsonVO {

    @ApiModelProperty(required = true, value = "标注id")
    private Long annotationId;

    @ApiModelProperty(required = true, value = "标注类型")
    private String type;

    @ApiModelProperty(required = true, value = "标注地方位置图形数据")
    private String coordinates;

    @ApiModelProperty(required = true, value = "标注类别名称")
    private String categoryName;

    @ApiModelProperty(required = true, value = "颜色的RGB值")
    private String color;
}
