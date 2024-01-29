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
public class ImageMarkingIn {
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = true)
    private Long projectId;

    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id", hidden = true)
    private Long categoryId;

    /**
     * 标注类别
     */
    @ApiModelProperty(value = "标注类别", hidden = true)
    private String annotationType;

    @ApiModelProperty(value = "项目状态列表(要排除的状态)")
    private List<Integer> statusList;

    @ApiModelProperty(value = "项目id", hidden = true)
    private List<Long> projectIds;

    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id", hidden = true)
    private List<Long> categoryIds;
}
