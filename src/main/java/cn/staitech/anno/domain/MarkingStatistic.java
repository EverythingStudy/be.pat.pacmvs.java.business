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
    private String projectName;

    @ApiModelProperty(value = "创建人姓名", required = true)
    private String nickName;

    @ApiModelProperty(value = "标签集名称", required = true)
    private String indicatorName;

    @ApiModelProperty(value = "标签名称", required = true)
    private String categoryName;

    @ApiModelProperty(value = "项目ID", required = true)
    private Long projectId;

    @ApiModelProperty(value = "创建人", required = true)
    private Long createBy;

    @ApiModelProperty(value = "标签集", required = true)
    private Long indicatorId;

    @ApiModelProperty(value = "标签", required = true)
    private String categoryId;

    @ApiModelProperty(value = "标注总数", required = true)
    private Long markingNum;

    @ApiModelProperty(value = "机构ID", required = true)
    private Long organizationId;
}
