package cn.staitech.anno.vo.projectstatistics;

import cn.staitech.anno.domain.Pager;
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
public class ImageLabelIn extends Pager {
    @ApiModelProperty(value = "项目id",required = true)
    @NotNull(message = "项目id为空！")
    private Long projectId;

    @ApiModelProperty(value = "图像id列表")
    private List<Long> imageIds;

    @ApiModelProperty(value = "图像状态列表")
    private List<Integer> imageStatus;

    @ApiModelProperty(value = "标注人id列表")
    private List<Long> userIds;

    @ApiModelProperty(value = "标签id列表")
    private List<Long> categoryIds;
}
