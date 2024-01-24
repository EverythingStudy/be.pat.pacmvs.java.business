package cn.staitech.anno.vo.projectstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class labelingPersonnelOut {
    @ApiModelProperty(value = "标注人id")
    private Long userId;

    @ApiModelProperty(value = "标注人名称")
    private String nickName;
}
