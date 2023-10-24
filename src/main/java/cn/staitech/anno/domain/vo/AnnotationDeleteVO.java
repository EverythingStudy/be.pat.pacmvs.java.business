package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 通过项目ID，用户名批量删除标注VO
 *
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
    @NotNull(message = "{ProjectRemoveIn.projectId.isnull}")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;

    /**
     * 创建者id .
     */
    @NotNull(message = "{AnnotationDeleteVO.createBy.isnull}")
    @ApiModelProperty(value = "创建者id", required = true)
    private Long[] createBy;
}
