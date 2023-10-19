package cn.staitech.anno.domain.vo.indicator;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IndicatorReviseVO {
    
    @ApiModelProperty(required = true, value = "病例指标id")
    @NotNull(message = "{IndicatorReviseVO.indicatorId.isnull}")
    private Integer indicatorId;
    
    @ApiModelProperty(required = true, value = "病例指标名称")
    private String indicatorName;
    
    @ApiModelProperty(hidden = true, value = "更新者")
    private String updateBy;
    
    @ApiModelProperty(hidden = true, value = "更新时间")
    private String updateTime;

    @ApiModelProperty(hidden = true, value = "标注类别数量")
    private Integer annotationCategoryTotal;

    @ApiModelProperty(hidden = true, value = "关联项目数量")
    private Integer projectTotal;
    
}
