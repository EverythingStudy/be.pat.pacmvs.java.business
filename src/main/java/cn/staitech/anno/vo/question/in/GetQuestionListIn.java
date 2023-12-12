package cn.staitech.anno.vo.question.in;

import cn.staitech.common.core.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/9/26 11:19
 * @desc 题库列表查询
 */
@Data
public class GetQuestionListIn extends PageRequest {
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "切片编号")
    private String imageCode;

    @ApiModelProperty(value = "切片名称")
    private String imageName;

    @ApiModelProperty(value = "专题号")
    private String topicName;

    @ApiModelProperty(value = "机构名称")
    private String organizationName;

    @ApiModelProperty(value = "机构ID", hidden = true)
    private Long organizationId;


}
