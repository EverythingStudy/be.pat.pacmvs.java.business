package cn.staitech.anno.vo.predictionInfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: EyeAnalyzedResult
* @Description:
* @author wanglibei
* @date 2023年11月8日
* @version V1.0
 */
@Data
public class EyeAnalyzedResult{


    @ApiModelProperty(value = "切片ID")
    private Long slidePredictionId;
    
    @ApiModelProperty(value = "ai分析状态")
    private Integer aiAnalyzed;
    



}
