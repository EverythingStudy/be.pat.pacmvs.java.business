package cn.staitech.annotation.vo.anno;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author .
 */
@Data
public class AnnotationOperationReq extends AnnotationReq{

    @NotNull(message = "{DescriptionUpdateVO.annotationId.isnull}")
    @ApiModelProperty(value = "标注id")
    @JsonProperty("marking_id")
    private Long annotationId;

    @NotNull(message = "{ARGUMENT_INVALID}")
    @ApiModelProperty(value = "(新图形) 标注坐标")
    private Geometry geometry;

    @NotBlank(message = "操作不可为空")
    @ApiModelProperty(value = "要执行的操作(UNION:相交,DIFFERENCE:相差)")
    private String operation;

    @NotNull(message = "校验不可为空")
    @ApiModelProperty(value = "校验")
    private Boolean check;

    @ApiModelProperty(value = "分辨率")
    private String resolution;
}
