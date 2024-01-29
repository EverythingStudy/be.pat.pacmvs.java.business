package cn.staitech.anno.vo.projectstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProjectLabelIn {
    @ApiModelProperty(value = "标签列表")
    private List<Long> categoryIds;

    @ApiModelProperty(value = "",hidden = true)
    private Long indicatorId;

    @ApiModelProperty(value = "标注者列表")
    private List<Long> userIds;

    @ApiModelProperty(value = "项目id",required = true)
    @NotNull(message = "项目id为空")
    private Long projectId;

}
