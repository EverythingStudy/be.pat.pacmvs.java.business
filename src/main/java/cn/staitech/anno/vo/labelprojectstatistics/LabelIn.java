package cn.staitech.anno.vo.labelprojectstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LabelIn {
    @ApiModelProperty(value = "项目id列表")
    private List<Long> projectIds;

    @ApiModelProperty(value = "标签集id列表")
    private List<Long> indicatorIds;

    @ApiModelProperty(value = "机构id", hidden = true)
    private Long organizationId;

    @ApiModelProperty(value = "用户id", hidden = true)
    private Long userId;
}
