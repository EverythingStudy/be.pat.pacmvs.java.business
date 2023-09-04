package cn.staitech.anno.domain.vo.specialImageAnno;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SpecialAnnotationEditVO {

    /**
     * 标注id
     */
    @NotNull(message = "标注id不可为空！")
    @ApiModelProperty(value = "标注id", required = true)
    private Long annotationId;

    /**
     * 详细地理位置信息（第二个图形）
     */
    @NotBlank(message = "确认状态不可为空字符串！")
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
    
    @ApiModelProperty(name = "dictId" , value = "脏器标签ID")
	private Long dictId;
}
