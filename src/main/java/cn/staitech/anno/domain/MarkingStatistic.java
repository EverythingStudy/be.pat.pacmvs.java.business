package cn.staitech.anno.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wangfeng
 * @create: 2023-12-26 13:41:05
 * @Description: 标注统计查询VO
 */
@Data
public class MarkingStatistic {
    @ApiModelProperty(value = "项目名称", required = true)
    String projectName;

    @ApiModelProperty(value = "创建人姓名", required = true)
    String nickName;

    @ApiModelProperty(value = "标签集名称", required = true)
    String indicatorName;

    @ApiModelProperty(value = "标签名称", required = true)
    String categoryName;

    @ApiModelProperty(value = "项目ID", required = true)
    Long projectId;

    @ApiModelProperty(value = "创建人", required = true)
    Long createBy;

    @ApiModelProperty(value = "标签集", required = true)
    Long indicatorId;

    @ApiModelProperty(value = "标签", required = true)
    Long categoryId;

    @ApiModelProperty(value = "标注总数", required = true)
    Long sums;

    @ApiModelProperty(value = "机构ID", required = true)
    private Long organizationId;
}
