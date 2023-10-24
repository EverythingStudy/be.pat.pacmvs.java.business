package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 项目成员表
 * tb_project_member
 *
 * @author 王峰
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectMemberDeleteVO {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "{ProjectRemoveIn.projectId.isnull}")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;

    @NotNull(message = "{AnnotationDeleteVO.createBy.isnull}")
    @ApiModelProperty(value = "用户id列表", required = true)
    private Long[] userIds;
}