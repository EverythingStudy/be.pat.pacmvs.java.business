package cn.staitech.anno.domain.assessmentResults;

import cn.staitech.anno.project.vo.TimeRangeIN;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AssessmentResultsQueryIn {

    @ApiModelProperty(value = "json文件名称")
    private String jsonName;

    @ApiModelProperty(value = "考核人员")
    private String examinePeople;

    @ApiModelProperty(value = "考核标签")
    private String examineCategoryName;

    @NotNull(message = "{AssessmentResultsQueryIN.algorithmAssessmentId.isnull}")
    @ApiModelProperty(value = "考核id")
    private Long algorithmAssessmentId;

    @ApiModelProperty(value = "创建时间-查询入参")
    private TimeRangeIN createTimeParams;

    private Integer pageNum;

    private Integer pageSize;
}
