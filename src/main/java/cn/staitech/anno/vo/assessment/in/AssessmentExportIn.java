package cn.staitech.anno.vo.assessment.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wangf
 */
@Data
public class AssessmentExportIn {
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "算法考核id")
    private List<Long> algorithmentList;

}
