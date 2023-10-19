package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
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
 * @since 2023-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_algorithm_assessment")
public class AlgorithmAssessment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考核算法主键id
     */
    @TableId(value = "algorithm_assessment_id", type = IdType.AUTO)
    private Long algorithmAssessmentId;

    /**
     * 切片id
     */
    private Long slideId;

    /**
     * 缩略图地址
     */
    private String thumbUrl;

    /**
     * 标注类型id
     */
    private Long categoryId;

    /**
     * 标注类型名称
     */
    private String categoryName;

    /**
     * 标注json名称
     */
    private String annotationJsonName;


    /**
     * 标注json路径
     */
    private String annotationJsonUrl;


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
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 删除标志（0：存在，1：删除）
     */
    private String delFlag;


    @ApiModelProperty(value = "切片编号")
    private String imageName;

    @ApiModelProperty(value = "标注类别")
    private String categoryIds;


}
