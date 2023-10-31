package cn.staitech.anno.vo.projectgroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangf
 */
@Data
public class ProjectGroup implements Serializable, Cloneable {

    @ApiModelProperty(value = "项目分组ID")
    private Long projectGroupId;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "分组id")
    private Long groupId;

    @ApiModelProperty(value = "组别")
    private String groupName;

    @ApiModelProperty(value = "性别", notes = "0-雌；1-雄")
    private Integer gender;

    @ApiModelProperty(value = "组别描述")
    private String description;

    @ApiModelProperty(value = "移走原因")
    private int reasons;

    @ApiModelProperty(value = "用药计量")
    private String dosage;

    @ApiModelProperty(value = "切片数")
    private Integer slideTotal;

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}