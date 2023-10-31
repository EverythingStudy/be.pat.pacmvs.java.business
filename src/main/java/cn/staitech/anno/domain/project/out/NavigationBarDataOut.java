package cn.staitech.anno.domain.project.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/7/17 17:51
 * @desc
 */
@Data
public class NavigationBarDataOut {
    @ApiModelProperty("移走原因描述")
    private String reasonsDesc;
    @ApiModelProperty("移走原因代码：1-给药结束安乐死、2-恢复期结束安乐死")
    private int reasons;
}
