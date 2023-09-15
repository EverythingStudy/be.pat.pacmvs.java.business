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

import javax.validation.constraints.Size;

/**
 * tb_pathological_indicator_category
 * @TableName tb_pathological_indicator_category
 */
@TableName(value ="tb_pathological_indicator_category")
@Data
public class PathologicalIndicatorCategory implements Serializable {
    /**
     * 标注类别ID
     */
    @TableId(type = IdType.AUTO)
    private Long categoryId;

    /**
     * 结构指标ID
     */
    @ApiModelProperty("结构指标ID")
    private Long indicatorId;
    /**
     * 标注类别名称
     */
    @Size(max= 200,message="编码长度不能超过200")
    @ApiModelProperty("标注类别名称")
    @Length(max= 200,message="编码长度不能超过200")
    private String categoryName;
    /**
     * 结构ID
     */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("结构ID")
    @Length(max= 100,message="编码长度不能超过100")
    private String structureId;
    /**
     * 颜色的RGB值
     */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("颜色的RGB值")
    @Length(max= 255,message="编码长度不能超过255")
    private String rgb;
    /**
     * 颜色的HEX值
     */
    @Size(max= 200,message="编码长度不能超过200")
    @ApiModelProperty("颜色的HEX值")
    @Length(max= 200,message="编码长度不能超过200")
    private String hex;
    /**
     * 完整编码
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("完整编码")
    @Length(max= 50,message="编码长度不能超过50")
    private String number;
    /**
     * 图层顺序
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("图层顺序")
    @Length(max= 50,message="编码长度不能超过50")
    private String orderNumber;
    /**
     * 组织机构ID
     */
    @ApiModelProperty("组织机构ID")
    private Long organizationId;
    /**
     * 0:默认标注类型；1:unlable
     */
    @ApiModelProperty("0:默认标注类型；1:unlable")
    private Integer annoType;
    /**
     * 默认为0，1为删除
     */
    @ApiModelProperty("默认为0，1为删除")
    private Integer delFlag;
    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private Long createBy;
    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private Long updateBy;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
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
        PathologicalIndicatorCategory other = (PathologicalIndicatorCategory) that;
        return (this.getCategoryId() == null ? other.getCategoryId() == null : this.getCategoryId().equals(other.getCategoryId()))
            && (this.getIndicatorId() == null ? other.getIndicatorId() == null : this.getIndicatorId().equals(other.getIndicatorId()))
            && (this.getCategoryName() == null ? other.getCategoryName() == null : this.getCategoryName().equals(other.getCategoryName()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getAnnoType() == null ? other.getAnnoType() == null : this.getAnnoType().equals(other.getAnnoType()))
            && (this.getOrderNumber() == null ? other.getOrderNumber() == null : this.getOrderNumber().equals(other.getOrderNumber()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getNumber() == null ? other.getNumber() == null : this.getNumber().equals(other.getNumber()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCategoryId() == null) ? 0 : getCategoryId().hashCode());
        result = prime * result + ((getIndicatorId() == null) ? 0 : getIndicatorId().hashCode());
        result = prime * result + ((getCategoryName() == null) ? 0 : getCategoryName().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getAnnoType() == null) ? 0 : getAnnoType().hashCode());
        result = prime * result + ((getOrderNumber() == null) ? 0 : getOrderNumber().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getNumber() == null) ? 0 : getNumber().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", categoryId=").append(categoryId);
        sb.append(", indicatorId=").append(indicatorId);
        sb.append(", categoryName=").append(categoryName);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", annoType=").append(annoType);
        sb.append(", orderNumber=").append(orderNumber);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", number=").append(number);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}