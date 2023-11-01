package cn.staitech.anno.vo.assessment.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/10/17 17:48
 * @desc
 */
@Data
public class RemoveAssessmentIn {
    @ApiModelProperty(value = "算法考核id")
    private Long algorithmAssessmentId;
}
