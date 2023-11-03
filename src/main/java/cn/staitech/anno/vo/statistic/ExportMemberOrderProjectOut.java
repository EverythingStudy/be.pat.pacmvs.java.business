package cn.staitech.anno.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/11/2 10:35
 * @desc
 */
@Data
public class ExportMemberOrderProjectOut {
    private Long userId;
    private String userName;
    private Long projectId;
    private String projectName;
    private Long markingTotal=0L;
    @ApiModelProperty("未复核标注数")
    private Long notReviewed=0L;
    @ApiModelProperty("已复核标注数")
    private Long reviewed=0L;


}
