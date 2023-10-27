package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author wangf
 */
@Data
public class LabelVO {
    @ApiModelProperty(value = "页码")
    public int pageNum = 1;
    @ApiModelProperty(value = "每页显示多少条")
    public int pageSize = 10;
    @ApiModelProperty(required = true, value = "病理指标id")
    private Long indicatorId;
    @ApiModelProperty(value = "标签名称", hidden = true)
    private String categoryName;
    @ApiModelProperty(required = true, value = "结构ID")
    private String structureId;
    @ApiModelProperty(value = "结构名称")
    private String structureName;
    @ApiModelProperty(value = "机构ID")
    private Long organizationId;
    @ApiModelProperty("请求参数（开始和结束时间）")
    private Map<String, Object> createTimeParams;
    @ApiModelProperty(value = "请求参数（开始和结束时间）", hidden = true)
    private Map<String, Object> params;

}
