package cn.staitech.anno.vo.predictionInfo.in;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: CreateAssessmentDataIn
* @Description:
* @author wanglibei
* @date 2023年11月6日
* @version V1.0
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
