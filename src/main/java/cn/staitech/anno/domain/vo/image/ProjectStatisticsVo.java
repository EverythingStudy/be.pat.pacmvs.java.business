package cn.staitech.anno.domain.vo.image;

import cn.staitech.anno.domain.project.ProjectExt;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mugw
 * @version 1.0
 * @description 项目内切片统计对象
 * @date 2023/6/14 09:48:19
 */
@Data
public class ProjectStatisticsVo extends ProjectExt {

    /**
     * 阴性
     */
    @ApiModelProperty(name = "阴性")
    private Integer negativeTotal;
    /**
     * 阳性
     */
    @ApiModelProperty(name = "阳性")
    private Integer positiveTotal;
    /**
     * AI分析完成
     */
    @ApiModelProperty(name = "AI分析完成")
    private Integer finishTotal;
    /**
     * 待AI分析
     */
    @ApiModelProperty(name = "待AI分析")
    private Integer analysisTotal;
    /**
     * 人工诊断
     */
    @ApiModelProperty(name = "人工诊断")
    private Integer diagnosisTotal;


}
