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
public class ProjectLabelOut {
    @ApiModelProperty(value = "标注人id")
    private Long createBy;

    @ApiModelProperty(value = "标签id")
    private Long categoryId;

    @ApiModelProperty(value = "标注数量")
    private Integer markingNum;

    @ApiModelProperty(value = "标注图像总数")
    private Integer imageNum;

    @ApiModelProperty(value = "标注人员")
    private String nickName;

    @ApiModelProperty(value = "标签名称")
    private String categoryName;

    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "编号")
    private String num;

    @ApiModelProperty(value = "当前标签标注图像数量")
    private Integer labelImageNum;

    @ApiModelProperty(value = "标注总数")
    private Integer markingTotal;



}
