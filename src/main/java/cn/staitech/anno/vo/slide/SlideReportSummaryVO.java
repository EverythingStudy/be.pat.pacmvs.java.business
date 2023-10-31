package cn.staitech.anno.vo.slide;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author mugw
 * @version 1.0
 * @description 组内切片报表摘要
 * @date 2023/6/13 16:39:42
 */
@Data
public class SlideReportSummaryVO {

    private String projectName;
    private String groupName;
    private String gender;
    private Integer total;
    private Integer finishTotal;
    private String description;
    /**
     * 移走原因
     */
    @ApiModelProperty(required = true)
    private Integer reasons;

    /**
     * 剂量
     */
    @ApiModelProperty(required = true)
    @Size(min = 0, max = 20, message = "{SlideReportSummaryVo.dosage.length}")
    private String dosage;


}
