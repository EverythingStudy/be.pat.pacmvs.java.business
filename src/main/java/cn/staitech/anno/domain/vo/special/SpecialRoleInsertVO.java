package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Description ：角色编辑
 * @Project ：be.PathMedics.SaaS.java.system
 * @File ：SysRoleEditVO
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/7 星期三 14:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecialRoleInsertVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称(必填，只允许输入汉字，限制15个字)", required = true)
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 15, message = "角色名称长度不能超过15个汉字")
    private String roleName;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识(必填，只允许输入字母和数字)", required = true)
    @NotBlank(message = "权限标识不能为空")
    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    private String roleKey;

    /**
     * 功能权限
     */
    @ApiModelProperty(value = "功能权限", required = true)
    @NotEmpty(message = "功能权限列表不能为空")
    private Long[] menuIds;

    /**
     * 专题ID
     */
    @ApiModelProperty(value = "专题ID", required = true)
    private Long specialId;

    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述")
    @Size(min = 0, max = 50, message = "角色描述长度过长")
    private String remark;
}
