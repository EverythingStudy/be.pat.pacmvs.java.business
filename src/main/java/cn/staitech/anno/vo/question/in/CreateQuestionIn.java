package cn.staitech.anno.vo.question.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author wudi
 * @Date 2023/9/26 10:55
 * @desc 生成考题
 */
@Data
public class CreateQuestionIn {
    @ApiModelProperty(value = "项目id")
    @NotNull(message = "{CreateQuestionIn.projectId.isnull}")
    private Long projectId;
}
