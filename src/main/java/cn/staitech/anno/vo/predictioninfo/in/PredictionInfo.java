package cn.staitech.anno.vo.predictioninfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: PredictionInfo
 * @Description:
 * @date 2023年11月7日
 */
@Data
public class PredictionInfo {
    @ApiModelProperty(value = "切片预测ID")
    private Long slidePredictionId;

    @ApiModelProperty(value = "图像地址")
    private String imagePath;

}
