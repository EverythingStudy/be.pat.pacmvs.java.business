package cn.staitech.anno.domain.projectgroup.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author wudi
 * @Date 2023/5/31 9:12
 * @desc 项目分组删除
 */
@Data
public class RemoveProjectGroupIn {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @NotNull(message = "{RemoveProjectGroupIn.projectGroupId.isnull}")
    private Long projectGroupId;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    @NotNull(message = "{CreateQuestionIn.projectId.isnull}")
    private Long projectId;
    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id" )
    @NotNull(message = "{RemoveProjectGroupIn.groupId.isnull}")
    private Long groupId;

}
