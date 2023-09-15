package cn.staitech.anno.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 切片操作表
 * @TableName tb_opt
 */
@TableName(value ="tb_opt")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Opt implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long optId;

    /**
     * 切片主键id
     */
    @NotNull(message="[切片主键id]不能为空")
    @ApiModelProperty("切片主键id")
    private Long slideId;
    /**
     * 操作名称
     */
    @NotBlank(message="[操作名称]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("操作名称")
    @Length(max= 255,message="编码长度不能超过255")
    private String optName;
    /**
     * 操作编码
     */
    @NotBlank(message="[操作编码]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("操作编码")
    @Length(max= 255,message="编码长度不能超过255")
    private String optCode;
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @NotNull(message="[删除标志（0代表存在 1代表删除）]不能为空")
    @ApiModelProperty("删除标志（0代表存在 1代表删除）")
    private String delFlag;
    /**
     * 创建者
     */
    @NotNull(message="[创建者]不能为空")
    @ApiModelProperty("创建者")
    private Long createBy;
    /**
     * 创建时间
     */
    @NotNull(message="[创建时间]不能为空")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新者
     */
    @NotNull(message="[更新者]不能为空")
    @ApiModelProperty("更新者")
    private Long updateBy;
    /**
     * 更新时间
     */
    @NotNull(message="[更新时间]不能为空")
    @ApiModelProperty("更新时间")
    private Date updateTime;

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
        Opt other = (Opt) that;
        return (this.getOptId() == null ? other.getOptId() == null : this.getOptId().equals(other.getOptId()))
            && (this.getSlideId() == null ? other.getSlideId() == null : this.getSlideId().equals(other.getSlideId()))
            && (this.getOptName() == null ? other.getOptName() == null : this.getOptName().equals(other.getOptName()))
            && (this.getOptCode() == null ? other.getOptCode() == null : this.getOptCode().equals(other.getOptCode()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOptId() == null) ? 0 : getOptId().hashCode());
        result = prime * result + ((getSlideId() == null) ? 0 : getSlideId().hashCode());
        result = prime * result + ((getOptName() == null) ? 0 : getOptName().hashCode());
        result = prime * result + ((getOptCode() == null) ? 0 : getOptCode().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
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
        sb.append(", optId=").append(optId);
        sb.append(", slideId=").append(slideId);
        sb.append(", optName=").append(optName);
        sb.append(", optCode=").append(optCode);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}