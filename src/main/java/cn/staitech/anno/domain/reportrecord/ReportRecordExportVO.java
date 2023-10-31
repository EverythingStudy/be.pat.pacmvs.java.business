package cn.staitech.anno.domain.reportrecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportRecordExportVO {
    @ApiModelProperty(value = "切片id")
    private Long slideId;

    @ApiModelProperty(value = "新切片编号")
    private String imageCode;
}
