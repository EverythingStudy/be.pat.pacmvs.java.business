package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.special.SpecialMenu;
import cn.staitech.anno.vo.special.SpecialMenuQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description ：专题菜单表 数据层
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialmenuMapper
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/8 星期四 1:54
 */
public interface SpecialMenuMapper {
    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SpecialMenu> selectMenuList(SpecialMenuQuery menu);

    /**
     * 根据角色id查询专题权限列表
     *
     * @param roleId
     * @return
     */
    List<SpecialMenu> querySpecialRolePermsByRoleId(Long roleId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SpecialMenu> selectMenuListByUserId(SpecialMenuQuery menu);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    public List<SpecialMenu> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SpecialMenu> selectMenuTreeByUserId(@Param("userId") Long userId, @Param("specialId") Long specialId);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    SpecialMenu selectMenuById(Long menuId);
}
