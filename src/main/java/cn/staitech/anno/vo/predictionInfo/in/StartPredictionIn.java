package cn.staitech.anno.vo.predictionInfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: StartPredictionIn
 * @Description:
 * @date 2023年11月6日
 */
@Data
public class StartPredictionIn {


    @ApiModelProperty(value = "项目ID")
    @NotNull(message = "{StartPredictionIn.projectId.isnull}")
    private Long projectId;

    @ApiModelProperty(value = "请求算法类型 0：启动算法 1：重算失败数据")
    @NotNull(message = "{StartPredictionIn.type.isnull}")
    private Integer type;
}
