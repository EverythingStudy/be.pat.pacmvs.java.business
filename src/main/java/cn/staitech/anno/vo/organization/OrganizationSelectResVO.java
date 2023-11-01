package cn.staitech.anno.vo.organization;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gjt.
 * @data 2023/5/23 17:18
 */
@Data
public class OrganizationSelectResVO {

    @ApiModelProperty(value = "机构id")
    private Long organizationId;

    @ApiModelProperty(value = "机构名称")
    private String organizationName;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "联系方式")
    private String phonenumber;

    @ApiModelProperty(value = "帐号状态(0正常开启 1禁用)")
    private Long status;

    @ApiModelProperty(value = "删除标志(0代表存在 2代表删除)")
    private Long delFlag;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @ApiModelProperty(value = "授权用户人数")
    private Long authorizationMemberLimit;

    @ApiModelProperty(value = "当前用户数")
    private Long authorizationMemberUsed;

    @ApiModelProperty(value = "授权图像数量")
    private Long authorizationImageLimit;

    @ApiModelProperty(value = "当前图像使用数量")
    private Long authorizationImageUsed;

    @ApiModelProperty(value = "授权时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String authorizationTime;

    @ApiModelProperty(value = "到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String expirationTime;

    @ApiModelProperty(value = "", hidden = true)
    private String statusName;


}
