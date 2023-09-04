package cn.staitech.anno.domain.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProjectExt implements Serializable,Cloneable {

    @ApiModelProperty(name = "项目ID", notes = "")
    private Long projectId;

    @ApiModelProperty(name = "项目名称", notes = "")
    private String projectName;

    @ApiModelProperty(name = "专题id", notes = "")
    private Long specialId;

    @ApiModelProperty(name = "系统类型code", notes = "")
    private Long systemCode;

    @ApiModelProperty(name = "系统类型name", notes = "")
    private String systemName;

    @ApiModelProperty(name = "脏器类型code", notes = "")
    private Long viscusCode;

    @ApiModelProperty(name = "脏器类型name", notes = "")
    private String viscusName;

    @ApiModelProperty(name = "切片数", notes = "")
    private Integer slideTotal;

    @ApiModelProperty(name = "删除标志（0代表存在 1代表删除）", notes = "")
    private String delFlag;

    @ApiModelProperty(name = "创建者id", notes = "")
    private Long createBy;

    @ApiModelProperty(name = "创建者名称", notes = "")
    private String createName;

    @ApiModelProperty(name = "创建时间", notes = "")
    private Date createTime;

    @ApiModelProperty(name = "更新者", notes = "")
    private Long updateBy;

    @ApiModelProperty(name = "更新时间", notes = "")
    private Date updateTime;

    @ApiModelProperty(value = "起始创建时间")
    private Date beginTime;

    @ApiModelProperty(value = "终止创建时间")
    private Date endTime;

    @ApiModelProperty(name = "备用字段1", notes = "")
    private String text1;

    @ApiModelProperty(name = "备用字段2", notes = "")
    private String text2;
}