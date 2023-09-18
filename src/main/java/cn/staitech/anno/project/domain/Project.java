package cn.staitech.anno.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 项目表
 * @TableName tb_project
 */
@TableName(value ="tb_project")
@Data
public class Project implements Serializable {
    /**
     * 项目ID
     */
    @TableId(type = IdType.AUTO)
    private Long projectId;

    /**
     * 项目名称
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("项目名称")
    @Length(max= 50,message="编码长度不能超过50")
    private String projectName;
    /**
     * 专题id
     */
    @NotNull(message="[专题id]不能为空")
    @ApiModelProperty("专题id")
    private Long specialId;
    /**
     * 系统类型code
     */
    @ApiModelProperty("系统类型code")
    private Long systemCode;
    /**
     * 脏器类型code
     */
    @ApiModelProperty("脏器类型code")
    private Long viscusCode;
    /**
     * 种属ID
     */
    @ApiModelProperty("种属ID")
    private Integer speciesId;
    /**
     * 品系ID
     */
    @ApiModelProperty("品系ID")
    private Integer productSeriesId;
    /**
     * 染色类型（1RGB，2HEX）
     */
    @ApiModelProperty("染色类型（1RGB，2HEX）")
    private Integer colorType;
    /**
     * 关联结构指标
     */
    @ApiModelProperty("关联结构指标")
    private Long tagId;
    /**
     * 切片数
     */
    @ApiModelProperty("切片数")
    private Integer slideTotal;
    /**
     * 状态:1待启动，2进行中，3暂停，4已完成
     */
    @ApiModelProperty("状态:1待启动，2进行中，3暂停，4已完成")
    private Integer status;
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 1代表删除）")
    private String delFlag;
    /**
     * 项目类型:1标注2评审3标准训练集
     */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("项目类型:1标注2评审3标准训练集")
    @Length(max= 255,message="编码长度不能超过255")
    private String projectType;
    /**
     * 机构ID
     */
    @ApiModelProperty("机构ID")
    private Long organizationId;
    /**
     * 描述
     */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("描述")
    @Length(max= 255,message="编码长度不能超过255")
    private String description;
    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private Long createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private Long updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Long topicId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Project other = (Project) that;
        return (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getProjectName() == null ? other.getProjectName() == null : this.getProjectName().equals(other.getProjectName()))
            && (this.getSpecialId() == null ? other.getSpecialId() == null : this.getSpecialId().equals(other.getSpecialId()))
            && (this.getSystemCode() == null ? other.getSystemCode() == null : this.getSystemCode().equals(other.getSystemCode()))
            && (this.getViscusCode() == null ? other.getViscusCode() == null : this.getViscusCode().equals(other.getViscusCode()))
            && (this.getSpeciesId() == null ? other.getSpeciesId() == null : this.getSpeciesId().equals(other.getSpeciesId()))
            && (this.getProductSeriesId() == null ? other.getProductSeriesId() == null : this.getProductSeriesId().equals(other.getProductSeriesId()))
            && (this.getColorType() == null ? other.getColorType() == null : this.getColorType().equals(other.getColorType()))
            && (this.getTagId() == null ? other.getTagId() == null : this.getTagId().equals(other.getTagId()))
            && (this.getSlideTotal() == null ? other.getSlideTotal() == null : this.getSlideTotal().equals(other.getSlideTotal()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getOrganizationId() == null ? other.getOrganizationId() == null : this.getOrganizationId().equals(other.getOrganizationId()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getProjectName() == null) ? 0 : getProjectName().hashCode());
        result = prime * result + ((getSpecialId() == null) ? 0 : getSpecialId().hashCode());
        result = prime * result + ((getSystemCode() == null) ? 0 : getSystemCode().hashCode());
        result = prime * result + ((getViscusCode() == null) ? 0 : getViscusCode().hashCode());
        result = prime * result + ((getSpeciesId() == null) ? 0 : getSpeciesId().hashCode());
        result = prime * result + ((getProductSeriesId() == null) ? 0 : getProductSeriesId().hashCode());
        result = prime * result + ((getColorType() == null) ? 0 : getColorType().hashCode());
        result = prime * result + ((getTagId() == null) ? 0 : getTagId().hashCode());
        result = prime * result + ((getSlideTotal() == null) ? 0 : getSlideTotal().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getOrganizationId() == null) ? 0 : getOrganizationId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", projectId=").append(projectId);
        sb.append(", projectName=").append(projectName);
        sb.append(", specialId=").append(specialId);
        sb.append(", systemCode=").append(systemCode);
        sb.append(", viscusCode=").append(viscusCode);
        sb.append(", speciesId=").append(speciesId);
        sb.append(", productSeriesId=").append(productSeriesId);
        sb.append(", colorType=").append(colorType);
        sb.append(", tagId=").append(tagId);
        sb.append(", slideTotal=").append(slideTotal);
        sb.append(", status=").append(status);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", organizationId=").append(organizationId);
        sb.append(", description=").append(description);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}