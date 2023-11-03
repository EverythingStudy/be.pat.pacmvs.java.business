package cn.staitech.anno.vo.question.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/9/26 13:59
 * @desc 项目下拉框
 */
@Data
public class GetProjectBoxOut {

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;
}
