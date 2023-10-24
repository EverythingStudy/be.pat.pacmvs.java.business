package cn.staitech.anno.domain.vo.statistic.excel;

import cn.staitech.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnnoIndicatorExportExcelVO {

    @Excel(name = "病理指标")
    @ApiModelProperty(value = "统计维度名称")
    private String statisticName;

    @Excel(name = "标注数量")
    @ApiModelProperty(value = "统计数量")
    private Long statisticCount;
}
