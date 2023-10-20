package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AnnotationEditVO {

    /**
     * 标注id
     */
    @NotNull(message = "{MarkingDelIn.annotationId.notBlank}")
    @ApiModelProperty(value = "标注id", required = true)
    private Long annotationId;

    /**
     * 详细地理位置信息（第二个图形）
     */
    @NotBlank(message = "{SpecialAnnotationEditVO.location.isnull}")
    @ApiModelProperty(value = "详细地理位置信息（第二个图形）", required = true)
    private String location;

    /**
     * 要执行的操作
     */
    @ApiModelProperty(value = "要执行的操作(UNION:相交,DIFFERENCE:相差,null)")
    private String operation;

    /**
     * 权限标识 .
     */
    @ApiModelProperty(value = "权限标识", required = true)
    private String permission;
}
