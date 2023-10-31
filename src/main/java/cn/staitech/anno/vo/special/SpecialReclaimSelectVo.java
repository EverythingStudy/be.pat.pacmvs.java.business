package cn.staitech.anno.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author gjt.
 * &#064;data  2023/5/31 10:30
 */
@Data
public class SpecialReclaimSelectVo {

    @ApiModelProperty(value = "专题编号")
    private String specialNumber;

    @ApiModelProperty(value = "专题名称")
    private String specialName;

    @ApiModelProperty(value = "创建时间")
    private Map<String, Object> createTimeParams;

    @ApiModelProperty(value = "回收时间")
    private Map<String, Object> reclaimTimeParams;

    @ApiModelProperty(value = "到期时间")
    private Map<String, Object> expirationTimeParams;

    @ApiModelProperty(value = "当前页数")
    private int pageNum;

    @ApiModelProperty(value = "每页数据条数")
    private int pageSize;
}
