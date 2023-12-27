package cn.staitech.anno.vo.predictionInfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SlidePredictionIn
 * @Description:
 * @date 2023年11月7日
 */
@Data
public class SlidePredictionIn {


    @ApiModelProperty(value = "切片ID")
    @NotNull(message = "{SlidePredictionIn.slideId.isnull}")
    private Long slideId;


}
