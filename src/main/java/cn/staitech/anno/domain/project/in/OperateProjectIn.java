package cn.staitech.anno.domain.project.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author wudi
 * @Date 2023/5/30 13:58
 * @desc 项目编辑入参
 */
@Data
public class OperateProjectIn {

    @ApiModelProperty(value = "专题id")
    @NotNull(message = "专题不能为空")
    private Long specialId;

    @ApiModelProperty(name = "项目ID", notes = "修改时必填，新增时必不填")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    @NotNull(message = "{OperateProjectIn.projectName.isnull}")
    @Size(min = 0, max = 50, message = "{OperateProjectIn.projectName.length}")
    private String projectName;

    @ApiModelProperty(value = "病理系统id")
    private Long systemCode;

    @ApiModelProperty(value = "脏器类型id")
    @NotNull(message = "脏器类型id不能为空")
    private Long viscusCode;
}
