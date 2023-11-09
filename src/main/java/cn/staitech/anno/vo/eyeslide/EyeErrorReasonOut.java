package cn.staitech.anno.vo.eyeslide;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EyeErrorReasonOut {
    @ApiModelProperty(value = "提示语code")
    private String prompt;

    @ApiModelProperty(value = "错误原因")
    private String reason;
}
