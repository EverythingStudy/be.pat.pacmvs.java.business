package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/14 09:20:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlideRemarkIn {
    @ApiModelProperty("切片id集合")
    private List<Long> slideIds;
    @ApiModelProperty("备注")
    private String remark;
}
