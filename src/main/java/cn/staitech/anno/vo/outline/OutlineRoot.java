package cn.staitech.anno.vo.outline;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Redis: OUTLINE_ROOT:
 *
 * @author wangfeng
 * @since 2024-01-09 14:55:03
 */
@Data
public class OutlineRoot {
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "图像ID")
    private Long imageId;

    @NotNull
    @ApiModelProperty(value = "切片ID")
    private Long slideId;

    @NotNull
    @ApiModelProperty(value = "创建者ID")
    private Long createBy;

    @ApiModelProperty(value = "token")
    private String token;
}
