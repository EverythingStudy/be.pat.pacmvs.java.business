package cn.staitech.anno.vo.examine;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExamineScoreUpdateVO {
    @ApiModelProperty(value = "评分id")
    @NotNull(message="{ExamineScoreUpdateVO.examineScoreId.isnull}")
    private Long examineScoreId;

    @ApiModelProperty(value = "考核状态（1通过，2不通过）")
    @NotBlank(message = "{ExamineScoreUpdateVO.examResults.isnull}")
    private String examResults;
}
