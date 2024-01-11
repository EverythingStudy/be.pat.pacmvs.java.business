package cn.staitech.anno.vo.labelprojectstatistics;

import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectListIn extends Pager {
    @ApiModelProperty(value = "项目id列表")
    private List<Long> projectIds;

    @ApiModelProperty(value = "项目状态列表")
    private List<Integer> statusList;

    @ApiModelProperty(value = "标签集id列表")
    private List<Long> indicatorIds;

    @ApiModelProperty(value = "创建者id列表")
    private List<Long> userIds;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private Map<String, Object> createTimeParams;

    @ApiModelProperty(value = "机构id", hidden = true)
    private Long organizationId;

    @ApiModelProperty(value = "用户id", hidden = true)
    private Long users;

    @ApiModelProperty(value = "项目类型", hidden = true)
    private String projectType;

}
