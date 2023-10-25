package cn.staitech.anno.domain.vo.reportrecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReportRecordVO {
    @ApiModelProperty(required = true, value = "专题id")
    private Long specialId;

    @ApiModelProperty(required = true, value = "系统类型id")
    private Long systemCode;

}
