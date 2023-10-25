package cn.staitech.anno.domain.assessment.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author wudi
 * @Date 2023/10/17 15:18
 * @desc 生成算法考核数据
 */
@Data
public class CreateAssessmentIn {
    @ApiModelProperty(value = "且片编号")
    @NotEmpty(message = "{CreateAssessmentIn.slideList.notEmpty}")
    private List<CreateAssessmentDataIn> slideList;


}
