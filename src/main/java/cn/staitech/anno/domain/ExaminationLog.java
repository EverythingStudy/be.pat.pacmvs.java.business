package cn.staitech.anno.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 审核日志表 tb_examination_logx
 */
@Data
public class ExaminationLog {

    /**
     * 切片ID
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long slideId;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long projectId;

    /**
     * 图像ID
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long imageId;

    /**
     * 标注ID
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long annotationId;

    /**
     * 审核的状态：1.交付  2.审核通过  3.驳回'
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer examinationFlag;

    /**
     * 审核类型(1审核项目，2审核图像，3审核切片，4审核标注)
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer examinationType;

    /**
     * 审核人
     */
    @ApiModelProperty(value = "", hidden = true)
    private Long createBy;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}