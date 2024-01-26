package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectStatisticsOut {

    @ApiModelProperty(value = "标签名称")
    private String categoryName;

    @ApiModelProperty(value = "标签图像数量")
    private Integer categoryImageNum;

    @ApiModelProperty(value = "标注人员")
    private String userName;

    @ApiModelProperty(value = "标注数量")
    private Integer markingCount;

    @ApiModelProperty(value = "标注总数量")
    private Integer markingSum;
}
