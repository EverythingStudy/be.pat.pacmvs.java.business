package cn.staitech.anno.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 切片标注关联表 tb_slide_annotation_result
 */
@Data
public class SlideAnnotationResult {
    
    /**
     * 切片ID
     */
    @ApiModelProperty(hidden = true)
    private Long slideId;
    
    /**
     * 标注类别ID
     */
    @ApiModelProperty(hidden = true)
    private Long categoryId;
    
    /**
     * 标注状态 (0未开始 1标注中 2标注完成 3已提交复核)
     */
    @ApiModelProperty(hidden = true)
    private Integer processFlag;
    
    /**
     * 数量
     */
    @ApiModelProperty(hidden = true)
    private Integer sum;
    
    /**
     * 用户ID
     */
    @ApiModelProperty(hidden = true)
    private Long updateBy;
    
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
