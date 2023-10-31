package cn.staitech.anno.vo.project.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author wudi
 * @Date 2023/5/30 16:26
 * @desc
 */
@Data
public class NavigationBarData {

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "移走原因")
    private List<NavigationBarDataOut> reasonsList;

}
