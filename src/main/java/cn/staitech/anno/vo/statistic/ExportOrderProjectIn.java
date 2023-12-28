package cn.staitech.anno.vo.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author wudi
 * @Date 2023/11/2 9:27
 * @desc
 */
@Data
public class ExportOrderProjectIn {
    @ApiModelProperty(value = "项目主键id列表")
    List<Long> projectList;
}
