package cn.staitech.anno.vo.special;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author gjt.
 * &#064;data  2023/5/29 8:41
 */
@Data
public class SpecialReclaim {

    /**
     * 主键id .
     */
    private Long specialReclaimId;

    /**
     * 专题id .
     */
    private Long specialId;

    /**
     * 项目数量 .
     */
    private Long projectNum;

    /**
     * 切片数量 .
     */
    private Long slideNum;

    /**
     * 回收时间 .
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String reclaimTime;

    /**
     * 回收者 .
     */
    private Long reclaimBy;

    /**
     * 到期时间 .
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String expireTime;

    @ApiModelProperty(value = "回收时间")
    private Map<String, Object> reclaimTimeParams;

    @ApiModelProperty(value = "到期时间")
    private Map<String, Object> expirationTimeParams;

}
