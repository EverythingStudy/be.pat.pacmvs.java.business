package cn.staitech.anno.domain.question.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author wudi
 * @Date 2023/10/8 18:20
 * @desc
 */
@Data
public class GetQuestionsOut {
    @ApiModelProperty("考题列表")
    private List<GetQuestionListOut> reqList;

    @ApiModelProperty(value = "应标个数")
    private Long shouldMarks;
}
