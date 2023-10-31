package cn.staitech.anno.project.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tb_marking
 */
@TableName(value = "tb_marking", autoResultMap = true)
@Data
public class Marking implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键自增id
     */
    @TableId(type = IdType.AUTO)
    private String markingId;
    /**
     * 标注id
     */
    private String annotationId;
    /**
     * 面积
     */
    private String area;
    /**
     * 周长
     */
    private String perimeter;
    /**
     * 描述
     */
    private String description;
    /**
     * 标签id
     */
    private Long categoryId;
    /**
     * 标注名称
     */
    private Long number;
    /**
     * 测量轮廓类型(0:正常,表示有关系,默认为0")
     */
    private Integer measureType;
    /**
     * 测量关系
     */
    private String measureRelation;
    /**
     * 测量轮廓表示名称:L
     */
    private String measureName;
    /**
     * 测量轮廓标识：1
     */
    private Integer measureNumber;
    /**
     * 周长（圆）
     */
    private String radius;
    /**
     * 平均间距
     */
    private Double meanDistance;
    /**
     * 最大间距
     */
    private Double maxDistance;
    /**
     * 最小间距
     */
    private Double minDistance;
    /**
     * 内角
     */
    private String innerAngle;
    /**
     * 外角
     */
    private String exteriorAngle;
    /**
     * 创建者
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量工具数据)
     */
    private String annotationType;
    /**
     * 标注数据类型(LineString,Polygon,point,pc,p,L)
     */
    private String locationType;
    /**
     * 切片id
     */
    private Long slideId;
    /**
     * 中心
     */
    private String centerPoint;
    /**
     * 不同标签点的总数
     */
    private Integer pointCount;
    /**
     * 标注数据
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONObject geometry;
    /**
     * 更新者
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 标注绘制者
     */
    private String annotationOwner;
    /**
     * 标注更新者
     */
    private String annotationUpdateOwner;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 图像id
     */
    private Long imageId;
    /**
     * 图像url
     */
    private String imageUrl;
    /**
     * 机构id
     */
    private Long organizationId;

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
        Marking other = (Marking) that;
        return (this.getMarkingId() == null ? other.getMarkingId() == null : this.getMarkingId().equals(other.getMarkingId()))
                && (this.getAnnotationId() == null ? other.getAnnotationId() == null : this.getAnnotationId().equals(other.getAnnotationId()))
                && (this.getArea() == null ? other.getArea() == null : this.getArea().equals(other.getArea()))
                && (this.getPerimeter() == null ? other.getPerimeter() == null : this.getPerimeter().equals(other.getPerimeter()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getCategoryId() == null ? other.getCategoryId() == null : this.getCategoryId().equals(other.getCategoryId()))
                && (this.getNumber() == null ? other.getNumber() == null : this.getNumber().equals(other.getNumber()))
                && (this.getMeasureType() == null ? other.getMeasureType() == null : this.getMeasureType().equals(other.getMeasureType()))
                && (this.getMeasureRelation() == null ? other.getMeasureRelation() == null : this.getMeasureRelation().equals(other.getMeasureRelation()))
                && (this.getMeasureName() == null ? other.getMeasureName() == null : this.getMeasureName().equals(other.getMeasureName()))
                && (this.getMeasureNumber() == null ? other.getMeasureNumber() == null : this.getMeasureNumber().equals(other.getMeasureNumber()))
                && (this.getRadius() == null ? other.getRadius() == null : this.getRadius().equals(other.getRadius()))
                && (this.getMeanDistance() == null ? other.getMeanDistance() == null : this.getMeanDistance().equals(other.getMeanDistance()))
                && (this.getMaxDistance() == null ? other.getMaxDistance() == null : this.getMaxDistance().equals(other.getMaxDistance()))
                && (this.getMinDistance() == null ? other.getMinDistance() == null : this.getMinDistance().equals(other.getMinDistance()))
                && (this.getInnerAngle() == null ? other.getInnerAngle() == null : this.getInnerAngle().equals(other.getInnerAngle()))
                && (this.getExteriorAngle() == null ? other.getExteriorAngle() == null : this.getExteriorAngle().equals(other.getExteriorAngle()))
                && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getAnnotationType() == null ? other.getAnnotationType() == null : this.getAnnotationType().equals(other.getAnnotationType()))
                && (this.getLocationType() == null ? other.getLocationType() == null : this.getLocationType().equals(other.getLocationType()))
                && (this.getSlideId() == null ? other.getSlideId() == null : this.getSlideId().equals(other.getSlideId()))
                && (this.getCenterPoint() == null ? other.getCenterPoint() == null : this.getCenterPoint().equals(other.getCenterPoint()))
                && (this.getPointCount() == null ? other.getPointCount() == null : this.getPointCount().equals(other.getPointCount()))
                && (this.getGeometry() == null ? other.getGeometry() == null : this.getGeometry().equals(other.getGeometry()))
                && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getAnnotationOwner() == null ? other.getAnnotationOwner() == null : this.getAnnotationOwner().equals(other.getAnnotationOwner()))
                && (this.getAnnotationUpdateOwner() == null ? other.getAnnotationUpdateOwner() == null : this.getAnnotationUpdateOwner().equals(other.getAnnotationUpdateOwner()))
                && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
                && (this.getImageId() == null ? other.getImageId() == null : this.getImageId().equals(other.getImageId()))
                && (this.getImageUrl() == null ? other.getImageUrl() == null : this.getImageUrl().equals(other.getImageUrl()))
                && (this.getOrganizationId() == null ? other.getOrganizationId() == null : this.getOrganizationId().equals(other.getOrganizationId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMarkingId() == null) ? 0 : getMarkingId().hashCode());
        result = prime * result + ((getAnnotationId() == null) ? 0 : getAnnotationId().hashCode());
        result = prime * result + ((getArea() == null) ? 0 : getArea().hashCode());
        result = prime * result + ((getPerimeter() == null) ? 0 : getPerimeter().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCategoryId() == null) ? 0 : getCategoryId().hashCode());
        result = prime * result + ((getNumber() == null) ? 0 : getNumber().hashCode());
        result = prime * result + ((getMeasureType() == null) ? 0 : getMeasureType().hashCode());
        result = prime * result + ((getMeasureRelation() == null) ? 0 : getMeasureRelation().hashCode());
        result = prime * result + ((getMeasureName() == null) ? 0 : getMeasureName().hashCode());
        result = prime * result + ((getMeasureNumber() == null) ? 0 : getMeasureNumber().hashCode());
        result = prime * result + ((getRadius() == null) ? 0 : getRadius().hashCode());
        result = prime * result + ((getMeanDistance() == null) ? 0 : getMeanDistance().hashCode());
        result = prime * result + ((getMaxDistance() == null) ? 0 : getMaxDistance().hashCode());
        result = prime * result + ((getMinDistance() == null) ? 0 : getMinDistance().hashCode());
        result = prime * result + ((getInnerAngle() == null) ? 0 : getInnerAngle().hashCode());
        result = prime * result + ((getExteriorAngle() == null) ? 0 : getExteriorAngle().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getAnnotationType() == null) ? 0 : getAnnotationType().hashCode());
        result = prime * result + ((getLocationType() == null) ? 0 : getLocationType().hashCode());
        result = prime * result + ((getSlideId() == null) ? 0 : getSlideId().hashCode());
        result = prime * result + ((getCenterPoint() == null) ? 0 : getCenterPoint().hashCode());
        result = prime * result + ((getPointCount() == null) ? 0 : getPointCount().hashCode());
        result = prime * result + ((getGeometry() == null) ? 0 : getGeometry().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getAnnotationOwner() == null) ? 0 : getAnnotationOwner().hashCode());
        result = prime * result + ((getAnnotationUpdateOwner() == null) ? 0 : getAnnotationUpdateOwner().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getImageId() == null) ? 0 : getImageId().hashCode());
        result = prime * result + ((getImageUrl() == null) ? 0 : getImageUrl().hashCode());
        result = prime * result + ((getOrganizationId() == null) ? 0 : getOrganizationId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", markingId=").append(markingId);
        sb.append(", annotationId=").append(annotationId);
        sb.append(", area=").append(area);
        sb.append(", perimeter=").append(perimeter);
        sb.append(", description=").append(description);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", number=").append(number);
        sb.append(", measureType=").append(measureType);
        sb.append(", measureRelation=").append(measureRelation);
        sb.append(", measureName=").append(measureName);
        sb.append(", measureNumber=").append(measureNumber);
        sb.append(", radius=").append(radius);
        sb.append(", meanDistance=").append(meanDistance);
        sb.append(", maxDistance=").append(maxDistance);
        sb.append(", minDistance=").append(minDistance);
        sb.append(", innerAngle=").append(innerAngle);
        sb.append(", exteriorAngle=").append(exteriorAngle);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", annotationType=").append(annotationType);
        sb.append(", locationType=").append(locationType);
        sb.append(", slideId=").append(slideId);
        sb.append(", centerPoint=").append(centerPoint);
        sb.append(", pointCount=").append(pointCount);
        sb.append(", geometry=").append(geometry);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", annotationOwner=").append(annotationOwner);
        sb.append(", annotationUpdateOwner=").append(annotationUpdateOwner);
        sb.append(", projectId=").append(projectId);
        sb.append(", imageId=").append(imageId);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append(", organizationId=").append(organizationId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}