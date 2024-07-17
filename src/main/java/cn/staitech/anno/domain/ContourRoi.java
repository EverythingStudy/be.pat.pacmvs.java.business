package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName tb_contour_roi
 */
@Data
@TableName(value ="tb_contour_roi")
public class ContourRoi implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "contour_roi_id", type = IdType.AUTO)
    private Long contourRoiId;

    /**
     * 
     */
    private String area;

    /**
     * 
     */
    private String perimeter;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Long categoryId;

    /**
     * 
     */
    private String locationType;

    /**
     * 
     */
    private String annotationType;

    /**
     * 
     */
    private Long createBy;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Long updateBy;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Long slideId;

    /**
     * 
     */
//    @TableField(typeHandler = JacksonTypeHandler.class)
    private String contour;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}