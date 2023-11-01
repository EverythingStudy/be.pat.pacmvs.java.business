package cn.staitech.anno.vo.project.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author wudi
 * @Date 2023/5/30 16:25
 * @desc 导航栏响应
 */
@Data
public class NavigationBarQueryOut {
    @ApiModelProperty(value = "项目数据集合")
    private List<NavigationBarData> respList;
    @ApiModelProperty(value = "项目总数")
    private int projectTotal;
}
