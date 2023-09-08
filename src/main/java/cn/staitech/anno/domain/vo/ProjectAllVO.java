package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProjectAllVO {
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "专题id")
    private Long specialId;

    @ApiModelProperty(value = "系统类型code")
    private Long systemCode;

    @ApiModelProperty(value = "系统类型name")
    private String systemName;

    @ApiModelProperty(value = "脏器类型code")
    private Long viscusCode;

    @ApiModelProperty(value = "脏器类型name")
    private String viscusName;

    @ApiModelProperty(value = "切片数")
    private Integer slideTotal;

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "创建者id")
    private Long createBy;

    @ApiModelProperty(value = "创建者名称")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "起始创建时间")
    private Date beginTime;
    @ApiModelProperty(value = "终止创建时间")
    private Date endTime;

}
