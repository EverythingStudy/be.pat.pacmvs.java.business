package cn.staitech.system.domain.vo.organization;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author gjt.
 */
@Data
public class OrganizationInsertVo {

    @NotNull(message = "机构名称不可为空!")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z\\d]{1,20}$",message = "请输入正确的机构名称")
    @ApiModelProperty(value = "机构名称", required = true)
    private String organizationName;

    @NotNull(message = "联系人不可为空!")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,6}$",message = "请输入正确的联系人名称")
    @ApiModelProperty(value = "联系人", required = true)
    private String contactName;

    @NotNull(message = "联系方式不可为空!")
    @Pattern(regexp = "^1[3-9]\\d{9}$",message = "请输入正确的手机号码")
    @ApiModelProperty(value = "联系方式", required = true)
    private String phonenumber;

    @NotNull(message = "授权用户人数不可为空!")
    @ApiModelProperty(value = "授权用户人数")
    private Long authorizationMemberLimit;

//    @NotNull(message = "授权图像数量不可为空!")
//    @ApiModelProperty(value = "授权图像数量", required = true)
//    private Long authorizationImageLimit;

    @NotNull(message = "授权时间不可为空!")
    @ApiModelProperty(value = "授权时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String authorizationTime;

    @NotNull(message = "到期时间不可为空!")
    @ApiModelProperty(value = "到期时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String expirationTime;

}
