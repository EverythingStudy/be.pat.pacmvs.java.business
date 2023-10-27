package cn.staitech.anno.domain.question.in;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author wudi
 * @Date 2023/9/26 10:58
 * @desc 根据切片创建考题
 */
@Data
public class CreateBySlideIn {
    @ApiModelProperty(value = "所选的切片列表")
    @NotEmpty(message = "{CreateAssessmentIn.slideList.notEmpty}")
    private List<CreateBySlideData> slideDataList;

}
