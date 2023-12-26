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
    private Long[] projectIds;

    @ApiModelProperty(value = "创建人")
    private Long[] createBys;

    @ApiModelProperty(value = "标签集")
    private Long[] indicatorIds;

    @ApiModelProperty(value = "标签")
    private Long[] categoryIds;

    @ApiModelProperty(value = "机构ID", hidden = true, required = true)
    private Long organizationId;

    @ApiModelProperty(value = "当前登录用户ID", hidden = true, required = true)
    private Long userId;

}
