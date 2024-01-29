package cn.staitech.anno.vo.predictioninfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: EyeAnalyzedResult
 * @Description:
 * @date 2023年11月8日
 */
@Data
public class EyeAnalyzedResult {


    @ApiModelProperty(value = "切片ID")
    private Long slidePredictionId;

    @ApiModelProperty(value = "ai分析状态")
    private Integer aiAnalyzed;


}
