package cn.staitech.anno.domain.vo;

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
    @NotNull(message = "项目id不能为空")
    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;
    @NotNull(message = "用户id列表不能为空")
    @ApiModelProperty(value = "用户id列表", required = true)
    private Long[] userIds;
    @NotNull(message = "角色id不能为空")
    @ApiModelProperty(value = "角色名称", required = true)
    private Long roleId;
}
