package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author gjt.
 * @data 2023/6/1 14:54
 */
@Data
public class SpecialRoleUserSelectResVo {

    @ApiModelProperty(value = "用户编号")
    private String userCode;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户名称")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private Long roleId;

    @ApiModelProperty(value = "姓名")
    private String nickName;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "专题角色")
    private String specialRoleName;

    @ApiModelProperty(value = "专题角色id")
    private Long specialRoleId;

    @ApiModelProperty(value = "系统角色id")
    private String roleName;

    @ApiModelProperty(value = "机构名称")
    private String organizationName;

    @ApiModelProperty(value = "状态")
    private Long status;

    @ApiModelProperty(value = "状态注释")
    private String statusFlag;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
