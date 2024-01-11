package cn.staitech.anno.vo.predictioninfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: CreateAssessmentDataIn
 * @Description:
 * @date 2023年11月6日
 */
@Data
public class SetMainImageDataIn {

    @ApiModelProperty(value = "原始切片ID")
    @NotNull(message = "{SetMainImageDataIn.slidePredictionId.isnull}")
    private Long slidePredictionId;

    @ApiModelProperty(value = "切片ID")
    @NotNull(message = "{SlidePredictionIn.slideId.isnull}")
    private Long slideId;

}
