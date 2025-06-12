package cn.staitech.annotation.vo.anno;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author mugw
 * @version 1.0
 * @description 轮廓间距
 * @date 2025/5/28 09:13:09
 */
@Data
public class AnnotationDistanceReq {

    @NotNull(message = "{NO_ANNOTATION_DATA}")
    @ApiModelProperty(value = "标注id", required = true)
    private Long annotationIdOne;

    @NotNull(message = "{NO_ANNOTATION_DATA}")
    @ApiModelProperty(value = "标注id", required = true)
    private Long annotationIdTwo;

    @NotNull(message = "{NO_ANNOTATION_DATA}")
    @ApiModelProperty(value = "标注类型", required = true)
    private String annotationTypeOne;

    @NotNull(message = "{NO_ANNOTATION_DATA}")
    @ApiModelProperty(value = "标注类型", required = true)
    private String annotationTypeTwo;

}
