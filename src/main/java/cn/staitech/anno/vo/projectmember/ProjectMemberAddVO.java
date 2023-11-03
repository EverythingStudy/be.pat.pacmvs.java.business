package cn.staitech.anno.vo.projectmember;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * @author wangfeng
 * @date 2023/03/23 18:10
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProjectMemberAddVO {
    @NotNull(message = "{ProjectRemoveIn.projectId.isnull}")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;
    @NotNull(message = "{AnnotationDeleteVO.createBy.isnull}")
    @ApiModelProperty(value = "用户id列表", required = true)
    private Long[] userId;
}
