package cn.staitech.anno.domain.assessment.in;

import cn.staitech.common.core.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @Author wudi
 * @Date 2023/10/17 16:55
 * @desc
 */
@Data
public class GetAssessmentListIn  extends PageRequest {
    @ApiModelProperty(value = "项目id")

    private Long projectId;

    @ApiModelProperty(value = "切片编号")
    private String imageName;

    @ApiModelProperty(value = "标注类型id")
    private Long categoryId;

    @ApiModelProperty(value = "生成时间")
    private Map<String, Date> createTimeParams;

}
