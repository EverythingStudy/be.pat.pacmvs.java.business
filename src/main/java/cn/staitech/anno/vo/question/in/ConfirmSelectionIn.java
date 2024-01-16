package cn.staitech.anno.vo.question.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author wudi
 * @Date 2023/9/26 15:07
 * @desc
 */
@Data
public class ConfirmSelectionIn {
    
    @ApiModelProperty(value = "考题id")
    private List<Long> questionId;

    @ApiModelProperty(value = "项目id")
    @NotNull(message = "{ProjectRemoveIn.projectId.isnull}")
    private Long projectId;
}
