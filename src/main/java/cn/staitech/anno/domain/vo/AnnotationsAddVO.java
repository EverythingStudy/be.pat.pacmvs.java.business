package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnnotationsAddVO {

    @NotNull(message = "{SlideUpdateVO.slideId.isnull}")
    @ApiModelProperty(value = "切片id", required = true)
    private Long slideId;

    @NotNull(message = "{SpecialRoleUserVo.userId.isnull}")
    @ApiModelProperty(value = "用户id", required = true)
    private Long markingCreateBy;

    /**
     * 位置 .
     */
    @ApiModelProperty(value = "标注地方位置图形数据", required = true)
    private String location;

    /**
     * 标注类型 .
     */
    @ApiModelProperty(value = "标注类型")
    private String locationType;

    /**
     * 标注类别id .
     */
    @ApiModelProperty(value = "标注类别id")
    private Long categoryId = 0L;

    /**
     * 项目id .
     */
    @ApiModelProperty(hidden = true, value = "项目id")
    private Long projectId;

    /**
     * 数量 .
     */
    @ApiModelProperty(hidden = true, value = "数量")
    private int sum;

    /**
     * 创建者id .
     */
    @ApiModelProperty(hidden = true, value = "创建者id")
    private Long createBy;

    /**
     * 描述 .
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 权限标识 .
     */
    @ApiModelProperty(value = "权限标识", required = true)
    private String permission;
}
