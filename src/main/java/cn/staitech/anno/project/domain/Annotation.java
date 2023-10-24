package cn.staitech.anno.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tb_annotation
 *
 * @TableName tb_annotation
 */
@TableName(value = "tb_annotation")
@Data
public class Annotation implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 标注ID
     */
    @TableId(type = IdType.AUTO)
    private Long annotationId;
    /**
     * 面积
     */
    private Long measure;
    /**
     * 周长
     */
    private Long perimeter;
    /**
     * 标注类型（1人工标注  2算法标注 ）
     */
    private Integer annotationType;
    /**
     * 描述
     */
    private String description;
    /**
     * 属性
     */
    private String attribute;
    /**
     * 标注类型
     */
    private String locationType;
    /**
     * 标注地方位置图形数据
     */
    private Object location;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 图像ID
     */
    private Long imageId;
    /**
     * 切片ID
     */
    private Long slideId;
    /**
     * 层数ID
     */
    private Integer tileId;
    /**
     * 标注审核状态（0未审核标注,1已审核标注  ）
     */
    private Integer examinationFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 标注类别id
     */
    private Long categoryId;
    /**
     * 标注图的质心x
     */
    private String x;
    /**
     * 标注图的质心y
     */
    private String y;
    /**
     * 1:ROI
     */
    private Integer annoType;
    /**
     * 标注类别创建者
     */
    private Integer createCategoryId;

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
        Annotation other = (Annotation) that;
        return (this.getAnnotationId() == null ? other.getAnnotationId() == null : this.getAnnotationId().equals(other.getAnnotationId()))
                && (this.getMeasure() == null ? other.getMeasure() == null : this.getMeasure().equals(other.getMeasure()))
                && (this.getPerimeter() == null ? other.getPerimeter() == null : this.getPerimeter().equals(other.getPerimeter()))
                && (this.getAnnotationType() == null ? other.getAnnotationType() == null : this.getAnnotationType().equals(other.getAnnotationType()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getAttribute() == null ? other.getAttribute() == null : this.getAttribute().equals(other.getAttribute()))
                && (this.getLocationType() == null ? other.getLocationType() == null : this.getLocationType().equals(other.getLocationType()))
                && (this.getLocation() == null ? other.getLocation() == null : this.getLocation().equals(other.getLocation()))
                && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
                && (this.getImageId() == null ? other.getImageId() == null : this.getImageId().equals(other.getImageId()))
                && (this.getSlideId() == null ? other.getSlideId() == null : this.getSlideId().equals(other.getSlideId()))
                && (this.getTileId() == null ? other.getTileId() == null : this.getTileId().equals(other.getTileId()))
                && (this.getExaminationFlag() == null ? other.getExaminationFlag() == null : this.getExaminationFlag().equals(other.getExaminationFlag()))
                && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getCategoryId() == null ? other.getCategoryId() == null : this.getCategoryId().equals(other.getCategoryId()))
                && (this.getX() == null ? other.getX() == null : this.getX().equals(other.getX()))
                && (this.getY() == null ? other.getY() == null : this.getY().equals(other.getY()))
                && (this.getAnnoType() == null ? other.getAnnoType() == null : this.getAnnoType().equals(other.getAnnoType()))
                && (this.getCreateCategoryId() == null ? other.getCreateCategoryId() == null : this.getCreateCategoryId().equals(other.getCreateCategoryId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAnnotationId() == null) ? 0 : getAnnotationId().hashCode());
        result = prime * result + ((getMeasure() == null) ? 0 : getMeasure().hashCode());
        result = prime * result + ((getPerimeter() == null) ? 0 : getPerimeter().hashCode());
        result = prime * result + ((getAnnotationType() == null) ? 0 : getAnnotationType().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getAttribute() == null) ? 0 : getAttribute().hashCode());
        result = prime * result + ((getLocationType() == null) ? 0 : getLocationType().hashCode());
        result = prime * result + ((getLocation() == null) ? 0 : getLocation().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getImageId() == null) ? 0 : getImageId().hashCode());
        result = prime * result + ((getSlideId() == null) ? 0 : getSlideId().hashCode());
        result = prime * result + ((getTileId() == null) ? 0 : getTileId().hashCode());
        result = prime * result + ((getExaminationFlag() == null) ? 0 : getExaminationFlag().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCategoryId() == null) ? 0 : getCategoryId().hashCode());
        result = prime * result + ((getX() == null) ? 0 : getX().hashCode());
        result = prime * result + ((getY() == null) ? 0 : getY().hashCode());
        result = prime * result + ((getAnnoType() == null) ? 0 : getAnnoType().hashCode());
        result = prime * result + ((getCreateCategoryId() == null) ? 0 : getCreateCategoryId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", annotationId=").append(annotationId);
        sb.append(", measure=").append(measure);
        sb.append(", perimeter=").append(perimeter);
        sb.append(", annotationType=").append(annotationType);
        sb.append(", description=").append(description);
        sb.append(", attribute=").append(attribute);
        sb.append(", locationType=").append(locationType);
        sb.append(", location=").append(location);
        sb.append(", projectId=").append(projectId);
        sb.append(", imageId=").append(imageId);
        sb.append(", slideId=").append(slideId);
        sb.append(", tileId=").append(tileId);
        sb.append(", examinationFlag=").append(examinationFlag);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", annoType=").append(annoType);
        sb.append(", createCategoryId=").append(createCategoryId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}