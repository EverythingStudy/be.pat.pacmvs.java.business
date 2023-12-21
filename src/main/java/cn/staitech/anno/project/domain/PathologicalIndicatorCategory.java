package cn.staitech.anno.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * tb_pathological_indicator_category
 *
 * @TableName tb_pathological_indicator_category
 */
@TableName(value = "tb_pathological_indicator_category")
@Data
public class PathologicalIndicatorCategory implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
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
    @Size(max = 200, message = "{PathologicalIndicatorCategory.categoryName.length}")
    @ApiModelProperty("标注类别名称")
    @Length(max = 200, message = "{PathologicalIndicatorCategory.categoryName.length}")
    private String categoryName;
    /**
     * 结构ID
     */
    @Size(max = 100, message = "{PathologicalIndicatorCategory.structureId.length}")
    @ApiModelProperty("结构ID")
    @Length(max = 100, message = "{{PathologicalIndicatorCategory.structureId.length}}")
    private String structureId;
    /**
     * 颜色的RGB值
     */
    @Size(max = 255, message = "{projectType.length}")
    @ApiModelProperty("颜色的RGB值")
    @Length(max = 255, message = "{projectType.length}")
    private String rgb;
    /**
     * 颜色的HEX值
     */
    @Size(max = 200, message = "{PathologicalIndicatorCategory.categoryName.length}")
    @ApiModelProperty("颜色的HEX值")
    @Length(max = 200, message = "{PathologicalIndicatorCategory.categoryName.length}")
    private String hex;
    /**
     * 完整编码
     */
    @Size(max = 50, message = "{PathologicalIndicatorCategory.number.length}")
    @ApiModelProperty("完整编码")
    @Length(max = 50, message = "{PathologicalIndicatorCategory.number.length}")
    private String number;
    /**
     * 图层顺序
     */
    @Size(max = 50, message = "{PathologicalIndicatorCategory.number.length}")
    @ApiModelProperty("图层顺序")
    private Integer orderNumber;
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
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", categoryId=" + categoryId +
                ", indicatorId=" + indicatorId +
                ", categoryName=" + categoryName +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", annoType=" + annoType +
                ", orderNumber=" + orderNumber +
                ", delFlag=" + delFlag +
                ", number=" + number +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}