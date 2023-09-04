package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class LabelVO {

    @ApiModelProperty(required = true,value = "病理指标id")
    private Long indicatorId;

    @ApiModelProperty(value = "标签名称")
    private String categoryName;

    @ApiModelProperty("请求参数（开始和结束时间）")
    private Map<String, Object> params;

    @ApiModelProperty(value = "页码")
    public int pageNum = 1;

    @ApiModelProperty(value = "每页显示多少条")
    public int pageSize = 10;

}
