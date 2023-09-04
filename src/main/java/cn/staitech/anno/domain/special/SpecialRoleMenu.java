package cn.staitech.anno.domain.special;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description ：专题角色和菜单关联表 tb_special_role_menu
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SysRoleMenu
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/5/30 星期二 17:01
 */
@Api(value = "专题角色和菜单关联", tags = "角色和菜单关联")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialRoleMenu {
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;
}
