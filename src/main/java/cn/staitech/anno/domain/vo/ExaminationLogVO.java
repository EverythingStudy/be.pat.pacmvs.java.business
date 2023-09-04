package cn.staitech.anno.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExaminationLogVO {

    /**
     * 切片id
     * */
    @ApiModelProperty(value = "切片ID")
    private Long slideId;

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private Long createBy;

    /**
     * 操作人名称
     */
    @ApiModelProperty(value = "操作人名称")
    private String operator;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 审核的状态：0.提交复合 1.开始复合  2.审核通过  3.驳回  4.交付'
     */
    @ApiModelProperty(value = "状态")
    private Integer examinationFlag;

    /**
     * 状态名称
     * */
    @ApiModelProperty(value = "操作名称")
    private String statusName;
}
