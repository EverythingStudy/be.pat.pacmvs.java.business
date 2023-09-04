package cn.staitech.anno.domain.project.in;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

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
    @NotNull(message = "项目名称不能为空")
    @Size(min = 0, max = 50, message = "项目名称长度不能超过50个字符")
    private String projectName;

    @ApiModelProperty(value = "病理系统id")
    //@NotNull(message = "病理系统id不能为空")
    private Long systemCode;

    @ApiModelProperty(value = "脏器类型id")
    @NotNull(message = "脏器类型id不能为空")
    private Long viscusCode;
}
