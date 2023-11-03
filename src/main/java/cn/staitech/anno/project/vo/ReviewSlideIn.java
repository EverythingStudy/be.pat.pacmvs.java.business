package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mugw
 * @version 1.0
 * @description 评审切片查询条件
 * @date 2023/9/22 14:56:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSlideIn {
    @ApiModelProperty("项目id")
    private Long projectId;
    @ApiModelProperty("评审轮次ID")
    private String reviewRoundId;
    @ApiModelProperty("分值")
    private String score;
    @ApiModelProperty("评审状态：1未评审，2已评审")
    private String reviewStatus;
    @ApiModelProperty("切片编号")
    private String imageCode;
    @ApiModelProperty("移走原因")
    private String removeReason;

}
