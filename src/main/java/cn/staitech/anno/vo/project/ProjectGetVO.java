package cn.staitech.anno.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectGetVO {

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;
}
