package cn.staitech.anno.domain.project.in;

import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @Author wangfeng
 * @Date 2023/09/18 10:46
 * @desc 项目列表查询入参
 * <p>
 * 项目列表-搜索条件： POST /anno/project/list
 * 专题编号：    topicId
 * 专题名称： topicName
 * 种属： speciesId
 * 品系： productSeriesId
 * 关联结构指标： indicatorId
 * 状态： status
 * 机构：organizationId
 * 创建时间： createTime
 */
@Data
public class ProjectListQueryIn extends Pager implements Serializable {
    @ApiModelProperty(value = "专题id", notes = "")
    private Long topicId;
    @ApiModelProperty(value = "专题名称", notes = "")
    private Long topicName;
    @ApiModelProperty(value = "项目名称", notes = "")
    private String projectName;
    @ApiModelProperty("种属ID")
    private Integer speciesId;
    @ApiModelProperty("品系ID")
    private Integer productSeriesId;
    @ApiModelProperty(value = "结构指标ID")
    private Long indicatorId;
    @ApiModelProperty(value = "状态", notes = "")
    private Long status;
    @ApiModelProperty(value = "机构编号")
    private Long organizationId;
    @ApiModelProperty(value = "创建时间范围")
    private Map<String, Date> createTime;
}
