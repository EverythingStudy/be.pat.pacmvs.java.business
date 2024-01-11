package cn.staitech.anno.vo.predictioninfo.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: EyeAlgorithmCallBackIn
 * @Description:
 * @date 2023年11月7日
 */
@Data
public class EyeAlgorithmCallBackIn {


    /*{
        "aiAnalyResult":[
            {
                "aiAnalyzed":1,
                "slidePredictionId":1
            },
            {
                "aiAnalyzed":1,
                "slidePredictionId":1
            }
        ],
        "mergeImagePath":"/home/pat_saas/.......svs",
        "aiAnalyzed":1,
        "slideId":942
    }*/
    @ApiModelProperty(value = "切片ID")
    @NotNull(message = "{EyeAlgorithmCallBackIn.slideId.isnull}")
    private Long slideId;

    @ApiModelProperty(value = "ai分析状态")
    @NotNull(message = "{EyeAlgorithmCallBackIn.aiAnalyzed.isnull}")
    private Integer aiAnalyzed;

    @ApiModelProperty(value = "合成图")
    private String mergeImagePath;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "组织id")
    private Long organizationId;

    @ApiModelProperty(value = "分析结果")
    private List<EyeAnalyzedResult> aiAnalyResult;


}
