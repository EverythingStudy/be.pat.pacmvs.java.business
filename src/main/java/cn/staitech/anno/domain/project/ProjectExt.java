package cn.staitech.anno.domain.project;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProjectExt implements Serializable, Cloneable {

    @ApiModelProperty(name = "项目ID")
    private Long projectId;

    @ApiModelProperty(name = "项目名称")
    private String projectName;

    @ApiModelProperty(name = "专题id")
    private Long specialId;

    @ApiModelProperty(name = "系统类型code")
    private Long systemCode;

    @ApiModelProperty(name = "系统类型name")
    private String systemName;

    @ApiModelProperty(name = "脏器类型code")
    private Long viscusCode;

    @ApiModelProperty(name = "脏器类型name")
    private String viscusName;

    @ApiModelProperty(name = "切片数")
    private Integer slideTotal;

    @ApiModelProperty(name = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    @ApiModelProperty(name = "创建者id")
    private Long createBy;

    @ApiModelProperty(name = "创建者名称")
    private String createName;

    @ApiModelProperty(name = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "更新者")
    private Long updateBy;

    @ApiModelProperty(name = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "起始创建时间")
    private Date beginTime;
    @ApiModelProperty(value = "终止创建时间")
    private Date endTime;
    @ApiModelProperty(value = "机构编号")
    @TableField(value = "organization_id")
    private Long organizationId;
    @ApiModelProperty(value = "机构名称")
    @TableField(exist = false)
    private String organizationName;
}