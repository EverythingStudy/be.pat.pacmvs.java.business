package cn.staitech.anno.vo.predictionInfo.in;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: SlidePredictionIn
* @Description:
* @author wanglibei
* @date 2023年11月7日
* @version V1.0
 */
@Data
public class SlidePredictionIn{


    @ApiModelProperty(value = "切片ID")
    @NotNull(message = "{SlidePredictionIn.slideId.isnull}")
    private Long slideId;



}
