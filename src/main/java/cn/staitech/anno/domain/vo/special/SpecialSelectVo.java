package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author gjt.
 * &#064;data  2023/5/30 8:26
 */

@Data
public class SpecialSelectVo {

    @ApiModelProperty(value = "页码")
    public int pageNum = 1;
    @ApiModelProperty(value = "每页显示多少条")
    public int pageSize = 10;
    @ApiModelProperty(value = "专题编号")
    private String specialNumber;
    @ApiModelProperty(value = "专题名称")
    private String specialName;
    @ApiModelProperty(value = "种属")
    private String species;
    @ApiModelProperty(value = "染色类型")
    private String stainType;
    @ApiModelProperty(value = "病理指标id")
    private Long indicatorId;
    @ApiModelProperty(value = "试验类型")
    private String trialType;
    @ApiModelProperty(value = "状态(0:待启动,1:进行中,2:暂停,3:锁定,4:已完成)")
    private Long status;
    @ApiModelProperty(value = "创建者")
    private String createBy;
    @ApiModelProperty(value = "创建时间")
    private Map<String, Object> createTimeParams;
}
