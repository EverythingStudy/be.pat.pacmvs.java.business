package cn.staitech.anno.domain.vo.reportRecord;

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
    private long systemCode;

    @ApiModelProperty(value = "脏器类型")
    private long viscusCode;

    @ApiModelProperty(value = "项目id")
    private long projectId;

    @ApiModelProperty(required = true, value = "分组id")
    private long groupId;

    @ApiModelProperty(required = true, value = "新切片编号")
    private String imageCode;
}
