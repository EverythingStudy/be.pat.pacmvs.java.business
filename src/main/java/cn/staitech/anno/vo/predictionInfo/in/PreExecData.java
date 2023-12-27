package cn.staitech.anno.vo.predictionInfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: PreExecData
 * @Description:
 * @date 2023年11月7日
 */
@Data
public class PreExecData {

    @ApiModelProperty(value = "原始切片数据")
    private List<PredictionInfo> predictionInfoList;


}
