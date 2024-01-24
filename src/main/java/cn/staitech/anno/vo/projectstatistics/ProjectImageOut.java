package cn.staitech.anno.vo.projectstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProjectImageOut {
    @ApiModelProperty(value = "图像id")
    private Long imageId;

    @ApiModelProperty(value = "图像名称")
    private String imageName;

}
