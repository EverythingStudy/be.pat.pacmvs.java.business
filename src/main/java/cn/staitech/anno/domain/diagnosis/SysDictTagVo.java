package cn.staitech.anno.domain.diagnosis;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SysDictTagVo
 * @Description:
 * @date 2023年7月11日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictTagVo {
    @ApiModelProperty(name = "dictType", value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "tagId", required = true)
    private String[] tagIdList;

    @ApiModelProperty(name = "filter", value = "filter")
    private String filter;
}
