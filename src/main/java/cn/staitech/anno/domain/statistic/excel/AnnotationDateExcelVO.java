package cn.staitech.anno.domain.statistic.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Project ：staitech-anno
 * @File ：AnnoProject
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/4/12 星期三 13:29
 * @Description ：
 */

@Data
public class AnnotationDateExcelVO {

    @ApiModelProperty(value = "统计维度名称")
    private String statisticDate;

    @ApiModelProperty(value = "统计数量")
    private Long statisticCount;
}