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
public class ReportRecordSingleVO {
    @ApiModelProperty(required = true, value = "系统类型")
    private Long systemCode;
    @ApiModelProperty(value = "脏器类型")
    private Long viscusCode;
    @ApiModelProperty(value = "项目id")
    private Long projectId;
    @ApiModelProperty(required = true, value = "分组id")
    private Long groupId;
    @ApiModelProperty(required = true, value = "新切片编号")
    private String imageCode;
}
