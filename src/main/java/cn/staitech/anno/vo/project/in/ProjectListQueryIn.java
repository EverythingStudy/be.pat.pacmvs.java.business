package cn.staitech.anno.vo.project.in;

import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
    @ApiModelProperty(value = "专题id")
    private Long topicId;
    @ApiModelProperty(value = "专题名称")
    private Long topicName;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty("种属ID")
    private String speciesId;
    @ApiModelProperty("品系ID")
    private Integer productSeriesId;
    @ApiModelProperty(value = "结构指标ID")
    private Long indicatorId;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "机构编号")
    private Long organizationId;
    @ApiModelProperty(value = "创建时间-查询入参")
    private Map<String, Object> createTimeParams;
    @ApiModelProperty(value = "病理病变id")
    private Long modelId;
}
