package cn.staitech.anno.domain.assessment.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AssessmentExportIN {
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "算法考核id")
    private List<Long> algorithmentList;

}
