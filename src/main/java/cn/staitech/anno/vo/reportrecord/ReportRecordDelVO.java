package cn.staitech.anno.vo.reportrecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportRecordDelVO {
    @ApiModelProperty(required = true, value = "报告id")
    private Long reportId;

    @ApiModelProperty(required = true, value = "专题id")
    private Long specialId;
}
