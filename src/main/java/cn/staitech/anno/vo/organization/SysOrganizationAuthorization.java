package cn.staitech.anno.vo.organization;


import cn.staitech.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author gjt
 */
@Data
public class SysOrganizationAuthorization extends BaseEntity {

    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID")
    private Long organizationId;

    /**
     * 授权用户人数
     */
    @ApiModelProperty(value = "授权用户人数")
    private Long authorizationMemberLimit;

    /**
     * 当前用户数
     */
    @ApiModelProperty(value = "当前用户数")
    private Long authorizationMemberUsed;

    /**
     * 授权用户数量
     */
    @ApiModelProperty(value = "授权图像数量")
    private Long authorizationImageLimit;

    /**
     * 当前图像使用数量
     */
    @ApiModelProperty(value = "当前图像使用数量")
    private Long authorizationImageUsed;

    /**
     * 授权时间
     */
    @ApiModelProperty(value = "授权时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String authorizationTime;

    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String expirationTime;

    /**
     * 授权时间
     */
    @ApiModelProperty(value = "授权时间")
    private Map<String, Object> authorizationTimeParams;

    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    private Map<String, Object> expirationTimeParams;

}
