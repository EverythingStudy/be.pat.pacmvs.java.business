package cn.staitech.anno.vo.predictionInfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: PredictionInfo
* @Description:
* @author wanglibei
* @date 2023年11月7日
* @version V1.0
 */
@Data
public class PredictionInfo {
	@ApiModelProperty(value = "切片预测ID")
    private Long slidePredictionId;

    @ApiModelProperty(value = "图像地址")
    private String imagePath;

}
