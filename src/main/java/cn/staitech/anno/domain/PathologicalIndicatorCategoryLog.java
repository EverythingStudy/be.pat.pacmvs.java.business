package cn.staitech.anno.domain;

import lombok.Data;

import java.util.Date;

/**
 * 标注类型记录实体类 .
 */
@Data
public class PathologicalIndicatorCategoryLog {
    
    /**
     * 主键 .
     */
    private Long logId;
    
    /**
     * 标注id .
     */
    private Long annotationId;
    
    /**
     * 类别id .
     */
    private Long categoryId;
    
    /**
     * 创建人 .
     */
    private Long createBy;
    
    /**
     * 创建时间 .
     */
    private Date createTime;
    
    /**
     * 更新人 .
     */
    private Long updateBy;
    
    /**
     * 更新时间 .
     */
    private Date updateTime;
    
    
}