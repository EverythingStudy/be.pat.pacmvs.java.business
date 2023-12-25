package cn.staitech.anno.vo.labelprojectstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LabelSetOut {
    @ApiModelProperty(value = "标准集id")
    private Long indicatorId;

    @ApiModelProperty(value = "标准集")
    private String indicatorName;

    @ApiModelProperty(value = "标准集en")
    private String indicatorNameEn;


}
