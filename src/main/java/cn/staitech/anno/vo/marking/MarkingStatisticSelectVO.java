package cn.staitech.anno.vo.marking;


import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: wangfeng
 * @create: 2023-12-26 13:41:05
 * @Description: 标注统计查询VO
 */
@Data
public class MarkingStatisticSelectVO extends Pager implements Serializable {
    @ApiModelProperty(value = "项目ID")
    Long[] projectIds;

    @ApiModelProperty(value = "创建人")
    Long[] createBys;

    @ApiModelProperty(value = "标签集")
    Long[] indicatorIds;

    @ApiModelProperty(value = "标签")
    Long[] categoryIds;

    @ApiModelProperty(value = "机构ID", required = true)
    private Long organizationId;
}
