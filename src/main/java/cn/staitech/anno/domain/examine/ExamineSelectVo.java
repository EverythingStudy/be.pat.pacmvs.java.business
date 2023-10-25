package cn.staitech.anno.domain.examine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamineSelectVo {

    @ApiModelProperty(value = "答题者")
    private String nickName;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "状态")
    private Long examResults;


}
