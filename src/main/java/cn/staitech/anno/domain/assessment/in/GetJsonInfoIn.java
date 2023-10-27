package cn.staitech.anno.domain.assessment.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author wudi
 * @Date 2023/10/18 10:30
 * @desc 获取json入参
 */
@Data
public class GetJsonInfoIn {
    @NotEmpty(message = "{GetJsonInfoIn.reqList.isnull}")
    @ApiModelProperty("算法数据")
    private List<GetJsonInfoDataIn> reqList;
    @ApiModelProperty("项目id")
    private Long projectId;
}
