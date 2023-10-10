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
 * @since 2023-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_examine_score")
public class ExamineScore implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "examine_score_id", type = IdType.AUTO)
    @ApiModelProperty(value = "评分id")
    private Long examineScoreId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /**
     * 项目题库id
     */
    @ApiModelProperty(value = "项目题库id")
    private Long questionProjectId;

    /**
     * 切片编号
     */
    @ApiModelProperty(value = "切片编号")
    private String imageName;

    /**
     * 答题者
     */
    @ApiModelProperty(value = "答题者")
    private String nickName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 应标个数
     */
    @ApiModelProperty(value = "应标个数")
    private Long shouldNumber;

    /**
     * 实标个数
     */
    @ApiModelProperty(value = "实标个数")
    private Long realityNumber;

    /**
     * 算法拟合区间
     */
    @ApiModelProperty(value = "算法拟合区间")
    private String algorithmInterval;

    /**
     * 个人拟合度
     */
    @ApiModelProperty(value = "个人拟合度")
    private String personalFit;

    /**
     * 考试结果(0无结果，1通过、2未通过)
     */
    @ApiModelProperty(value = "考试结果(0无结果，1通过、2未通过)")
    private String examResults;

    /**
     * 操作状态（0：开始考试，考试完成）
     */
    @ApiModelProperty(value = "操作状态（0：开始考试，考试完成）")
    private String operateStatus;

    /**
     * 考试状态（0：未交卷，1：已交卷）
     */
    @ApiModelProperty(value = "考试状态（0：未交卷，1：已交卷）")
    private String examStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private Long createBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private Long updateBy;


}
