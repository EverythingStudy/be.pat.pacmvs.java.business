package cn.staitech.anno.domain.vo.reportRecord;

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
    private long slideId;

    @ApiModelProperty(value = "新切片编号")
    private String imageCode;
}
