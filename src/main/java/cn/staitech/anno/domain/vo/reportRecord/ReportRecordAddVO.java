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
public class ReportRecordAddVO {

    @ApiModelProperty(required = true,value = "专题编号")
    private String specialNumber;

    @ApiModelProperty(required = true,value = "报告类型（1单切片报告，2组间报告,3脏器病变报告）")
    private Integer reportType;

    @ApiModelProperty(required = true,value = "专题id")
    private long specialId;

    @ApiModelProperty(required = true,value = "移走原因（1给药结束安乐死、2恢复期结束安乐死）")
    private Integer reasons;
}
