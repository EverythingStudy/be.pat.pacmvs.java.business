package cn.staitech.anno.domain.assessmentResults;

import cn.staitech.anno.project.vo.TimeRangeIN;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AssessmentResultsQueryIN {

    @ApiModelProperty(value = "json文件名称")
    private String jsonName;

    @ApiModelProperty(value = "考核人员")
    private String examinePeople;

    @ApiModelProperty(value = "考核标签")
    private String examineCategoryName;

    @ApiModelProperty(value = "创建时间-查询入参")
    private TimeRangeIN createTimeParams;


    private Integer pageNum;

    private Integer pageSize;
}
