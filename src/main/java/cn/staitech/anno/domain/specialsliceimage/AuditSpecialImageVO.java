package cn.staitech.anno.domain.specialsliceimage;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AuditSpecialImageVO {
    /**
     * 选片ID
     */
    @ApiModelProperty(value = "选片ID")
    private Long specialImageId;

    @ApiModelProperty(value = "专题标注ID")
    private Long specialAnnotationId;

    /**
     * 图像ID
     */
    @ApiModelProperty(value = "图像ID")
    private Long imageId;

    /**
     * 专题ID
     */
    @ApiModelProperty(value = "专题ID")
    private Long specialId;

    /**
     * 是否可用（0不可用1可用）
     */
    @ApiModelProperty(value = "是否可用 （0不可用1可用）")
    private Integer status;

    /**
     * 逻辑删除状态（0删除，1未删除）
     */
    @ApiModelProperty(value = "逻辑删除状态（0删除，1未删除）")
    private Integer deleteFlag;

    /**
     * 切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中
     */
    @ApiModelProperty(value = "切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中")
    private Integer sliceImageStatus;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "编辑人id")
    @TableField(value = "edit_by", updateStrategy = FieldStrategy.IGNORED)
    private Long editBy;

    /**
     * 切片批次号 默认为第一批次
     */
    @ApiModelProperty(value = "切片批次号 默认为第一批次")
    private Integer sliceBatchNumber;

    /**
     * 审核状态 0：待审核 1：审核通过 2：审核不通过
     */
    @ApiModelProperty(value = "审核状态 0：待审核 1：审核通过 2：审核不通过")
    private Integer auditStatus;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /**
     * 交付状态 0：未交付 1：已交付
     */
    @ApiModelProperty(value = "交付状态 0：未交付 1：已交付")
    private Integer deliveryStatus;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人id
     */
    @ApiModelProperty(value = "更新人id")
    private Long updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(required = true, value = "topicId专题")
    private Long topicId;

    @ApiModelProperty(required = false, value = "specialImageIds 数组")
    private Long[] specialImageIds;
}