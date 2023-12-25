package cn.staitech.anno.vo.labelprojectstatistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectLabelOut {

    @ApiModelProperty(value = "标签id")
    private Long categoryId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "标签集id")
    private Long indicatorId;

    @ApiModelProperty(value = "标签名称")
    private String categoryName;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "标签集名称")
    private String indicatorName;

    @ApiModelProperty(value = "项目状态code")
    private Integer status;

    @ApiModelProperty(value = "图像数量")
    private String imageNum;

    @ApiModelProperty(value = "标准总数")
    private String markingNum;

    @ApiModelProperty(value = "创建者")
    private String userName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value ="状态",hidden = true)
    private String statusName;

}
