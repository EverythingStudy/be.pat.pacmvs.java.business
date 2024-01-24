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
public class ImageLabelOut {
    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "创建者id")
    private Long createBy;

    @ApiModelProperty(value = "标签id")
    private Long categoryId;

    @ApiModelProperty(value = "标签名称")
    private String categoryName;

    @ApiModelProperty(value = "标注人员名称")
    private String nickName;

    @ApiModelProperty(value = "图像名称")
    private String imageName;

    @ApiModelProperty(value = "图像id")
    private Long imageId;

    @ApiModelProperty(value = "标注数量")
    private Integer markingNum;

    @ApiModelProperty(value = "图像状态code")
    private String status;

    @ApiModelProperty(value = "图像状态code")
    private String statusName;
}
