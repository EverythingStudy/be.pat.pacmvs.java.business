package cn.staitech.anno.domain.vo;

import cn.staitech.anno.domain.po.VisceraTagPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mugw
 * @version 1.0
 * @description 脏器标签
 * @date 2023/6/26 16:05:07
 */
@Data
public class VisceraTagVo extends VisceraTagPo {

    @ApiModelProperty(value = "脏器名称")
    private String visceraName;

    @ApiModelProperty(value = "脏器编码")
    private String visceraCode;
}
