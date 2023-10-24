package cn.staitech.anno.domain.project.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/5/30 15:46
 * @desc 项目详情
 */
@Data
public class ProjectInfoOut {
    @ApiModelProperty(value = "专题id")
    private Long specialId;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "病理系统id")
    private Long systemCode;

    @ApiModelProperty(value = "脏器类型id")
    private Long viscusCode;
}
