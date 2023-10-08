package cn.staitech.anno.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author gjt
 * @since 2023-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_marking_examine",autoResultMap = true)
public class MarkingExamine implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增id
     */
    @TableId(value = "marking_examine_id", type = IdType.AUTO)
    private Long markingExamineId;

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
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 题库项目id
     */
    private Long questionProjectId;

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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 标注绘制者
     */
    private String annotationOwner;

    /**
     * 标注类型
     */
    private String locationType;


}
