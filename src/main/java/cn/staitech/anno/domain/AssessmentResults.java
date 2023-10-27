package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("tb_assessment_results")
public class AssessmentResults implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考核结果
     */
    @TableId(value = "assessment_results_id", type = IdType.AUTO)
    @ApiModelProperty(value = "考核结果")
    private Long assessmentResultsId;

    /**
     * 考核算法主键
     */
    @ApiModelProperty(value = "考核算法主键")
    private Long algorithmAssessmentId;

    /**
     * 切片id
     */
    @ApiModelProperty(value = "切片id")
    private Long slideId;

    /**
     * 考核人员（vs）
     */
    @ApiModelProperty(value = "考核人员（vs）")
    private String examinePeople;

    /**
     * 考核标签
     */
    @ApiModelProperty(value = "考核标签")
    private String examineCategoryName;

    /**
     * 轮廓个数（vs）
     */
    @ApiModelProperty(value = "轮廓个数（vs）")
    private String outlineNumber;

    /**
     * 漏检率
     */
    @ApiModelProperty(value = "漏检率")
    private String missedDetectionRate;

    /**
     * 误检率
     */
    @ApiModelProperty(value = "误检率")
    private String falseDetectionRate;

    /**
     * miou拟合区间
     */
    @ApiModelProperty(value = "miou拟合区间")
    private String miou;

    /**
     * fiou拟合区间
     */
    @ApiModelProperty(value = "fiou拟合区间")
    private String fiou;

    /**
     * biou拟合区间
     */
    @ApiModelProperty(value = "biou拟合区间")
    private String biou;

    /**
     * tiou拟合区间
     */
    @ApiModelProperty(value = "tiou拟合区间")
    private String tiou;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private Integer updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;

    /**
     * json文件名称
     */
    @ApiModelProperty(value = "json文件名称")
    private String jsonName;


}
