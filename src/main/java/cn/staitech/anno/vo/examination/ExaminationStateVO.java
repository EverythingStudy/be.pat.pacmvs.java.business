package cn.staitech.anno.vo.examination;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExaminationStateVO {

    /**
     * 切片ID
     */
    @NotNull(message = "{SlideUpdateVO.slideId.isnull}")
    @ApiModelProperty(required = true, value = "切片ID")
    private Long slideId;

    /**
     * 标注状态 0未开始 1标注中 2标注完成 3已提交复核
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer processFlag;

    /**
     * 复核状态 (0提交复核(未复核) 1开始复核(复核中) 2复核通过 3驳回 4交付)
     */
    @ApiModelProperty(required = true, value = "复核状态 (0提交复核(未复核) 1开始复核(复核中) 2复核通过(已复核) 3复核未通过 4交付) ")
    private Integer examinationFlag;
}