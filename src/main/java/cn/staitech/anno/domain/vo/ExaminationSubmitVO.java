package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExaminationSubmitVO {
    /**
     * 切片id
     */
    @NotNull(message = "切片id不能为空")
    @ApiModelProperty(required = true, value = "切片id")
    private Long slideId;

    /**
     * 标注状态 0未开始 1标注中 2标注完成 3已提交复核
     */
    @ApiModelProperty(hidden = true)
    private Integer processFlag;

    /**
     * 复核状态 (1已提交复核(未复核) 2复核通过 3驳回 4交付)
     */
    @ApiModelProperty(hidden = true)
    private Integer examinationFlag;

    /**
     * 更新者
     */
    @ApiModelProperty(hidden = true)
    private Long updateBy;
}


