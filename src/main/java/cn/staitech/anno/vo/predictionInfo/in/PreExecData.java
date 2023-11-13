package cn.staitech.anno.vo.predictionInfo.in;

import java.util.List;

import cn.staitech.anno.vo.predictionInfo.out.SlidePredictionInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
* @ClassName: PreExecData
* @Description:
* @author wanglibei
* @date 2023年11月7日
* @version V1.0
 */
@Data
public class PreExecData {
    
    @ApiModelProperty(value = "原始切片数据")
    private List<PredictionInfo> predictionInfoList;
    
    
    
    

}
