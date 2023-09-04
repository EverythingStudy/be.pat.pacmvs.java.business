package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 通过项目ID，用户名批量删除标注VO
 * @author 王峰
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnnotationDeleteVO {

    /**
     * 项目id .
     */
    @NotNull(message = "项目id不能为空")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;

    /**
     * 创建者id .
     */
    @NotNull(message = "用户id列表不能为空")
    @ApiModelProperty(value = "创建者id", required = true)
    private Long[] createBy;
}
