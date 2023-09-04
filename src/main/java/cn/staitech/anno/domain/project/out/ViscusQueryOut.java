package cn.staitech.anno.domain.project.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/6/30 9:20
 * @desc 脏器
 */
@Data
public class ViscusQueryOut {
    @ApiModelProperty("脏器代码")
    private Long viscusCode;
    @ApiModelProperty("脏器中文名称")
    private String viscusName;
}
