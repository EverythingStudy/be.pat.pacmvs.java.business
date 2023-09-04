package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupGetVO {
    @ApiModelProperty(value = "专题id", required = true)
    private Long specialId;

    @ApiModelProperty(value = "移走原因（1给药结束安乐死、2恢复期结束安乐死）", required = true)
    private Integer reasons;
}
