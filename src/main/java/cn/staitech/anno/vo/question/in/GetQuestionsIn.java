package cn.staitech.anno.vo.question.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author wudi
 * @Date 2023/9/26 11:19
 * @desc 题库列表查询
 */
@Data
public class GetQuestionsIn {
    @ApiModelProperty(value = "项目id")
    @NotNull(message = "{ProjectRemoveIn.projectId.isnull}")
    private String projectId;

    @ApiModelProperty(value = "切片编号")
    private String imageCode;

    @ApiModelProperty(value = "专题号")
    private String topicName;


}
