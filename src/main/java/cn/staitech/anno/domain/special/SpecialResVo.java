package cn.staitech.anno.domain.special;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gjt.
 * @data 2023/6/7 16:50
 */
@Data
public class SpecialResVo {

    @ApiModelProperty(value = "主键id")
    private Long specialId;

    @ApiModelProperty(value = "专题编号")
    private String specialNumber;

    @ApiModelProperty(value = "专题名称")
    private String specialName;

    @ApiModelProperty(value = "种属")
    private String species;

    @ApiModelProperty(value = "染色类型")
    private String stainType;

    @ApiModelProperty(value = "病理指标id")
    private Long indicatorId;

    @ApiModelProperty(value = "同行评议(0:需要,1:不需要)")
    private Long peerReview;

    @ApiModelProperty(value = "状态(0:待启动,1:进行中,2:暂停,3:锁定,4:已完成)")
    private Long status;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;

    @ApiModelProperty(value = "删除标志(0:正常,1:回收站,2:删除)")
    private Long delFlag;

    @ApiModelProperty(value = "交付状态 0：未交付 1：已交付")
    private Long deliveryStatus;

    @ApiModelProperty(value = "指标名称")
    private String indicatorName;

    @ApiModelProperty(value = "项目总数")
    private Long projectNum;

    @ApiModelProperty(value = "切片总数")
    private Long slideNum;

    @ApiModelProperty(value = "创建者名称")
    private String userName;

    @ApiModelProperty(value = "试验类型")
    private String trialType;

    @ApiModelProperty(required = true, value = "topicId专题")
    private Integer topicId;

}
