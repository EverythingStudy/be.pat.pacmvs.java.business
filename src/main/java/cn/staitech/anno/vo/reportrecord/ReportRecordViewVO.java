package cn.staitech.anno.vo.reportrecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReportRecordViewVO {

    @ApiModelProperty(value = "页码")
    public int pageNum = 1;
    @ApiModelProperty(value = "每页显示多少条")
    public int pageSize = 10;
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    @ApiModelProperty(value = "报告类型")
    private Integer reportType;
    @ApiModelProperty(required = true, value = "专题id")
    private Long specialId;
}
