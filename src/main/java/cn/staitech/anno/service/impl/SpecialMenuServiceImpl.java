package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.special.SpecialMenu;
import cn.staitech.anno.domain.special.SpecialRole;
import cn.staitech.anno.domain.special.TreeSelect;
import cn.staitech.anno.domain.special.RouterVo;
import cn.staitech.anno.domain.special.SpecialMenuQuery;
import cn.staitech.anno.mapper.SpecialMenuMapper;
import cn.staitech.anno.mapper.SpecialRoleMapper;
import cn.staitech.anno.service.SpecialMenuService;
import cn.staitech.common.core.constant.Constants;
import cn.staitech.common.core.constant.UserConstants;
import cn.staitech.common.core.utils.StringUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description ：菜单 业务层处理
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialMenuServiceImpl
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/7 星期三 19:38
 */
@Service
public class SpecialMenuServiceImpl implements SpecialMenuService {
    @Resource
    private SpecialRoleMapper specialRoleMapper;

    @Resource
    private SpecialMenuMapper specialMenuMapper;

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SpecialMenu> selectMenuList(Long userId) {
        return selectMenuList(new SpecialMenuQuery(), userId);
    }

    @Override
    public List<SpecialMenu> querySpecialRolePermsByRoleId(Long roleId) {
        return specialMenuMapper.querySpecialRolePermsByRoleId(roleId);
    }


    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SpecialMenu> selectMenuList(SpecialMenuQuery menu, Long userId) {
        List<SpecialMenu> menuList;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId)) {
            menuList = specialMenuMapper.selectMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menuList = specialMenuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 根据专题查询用户菜单列表
     *
     * @param specialId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SpecialMenu> selectMenuListBySpecialId(Long userId, Long specialId) {
        return selectMenuListBySpecialId(new SpecialMenuQuery(), userId, specialId);
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SpecialMenu> selectMenuListBySpecialId(SpecialMenuQuery menu, Long userId, Long specialId) {
        List<SpecialMenu> menuList;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId)) {
            menuList = specialMenuMapper.selectMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menu.getParams().put("specialId", specialId);
            menuList = specialMenuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        SpecialRole role = specialRoleMapper.selectRoleById(roleId);
        return specialMenuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SpecialMenu> selectMenuTreeByUserId(Long userId, Long specialId) {
        List<SpecialMenu> menus;
        if (SecurityUtils.isAdmin(userId)) {
            menus = specialMenuMapper.selectMenuTreeAll();
        } else {
            menus = specialMenuMapper.selectMenuTreeByUserId(userId, specialId);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SpecialMenu selectMenuById(Long menuId) {
        return specialMenuMapper.selectMenuById(menuId);
    }


    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SpecialMenu> menus) {
        List<SpecialMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SpecialMenu> buildMenuTree(List<SpecialMenu> menus) {
        List<SpecialMenu> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        for (SpecialMenu dept : menus) {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SpecialMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SpecialMenu menu = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SpecialMenu> getChildPerms(List<SpecialMenu> list, int parentId) {
        List<SpecialMenu> returnList = new ArrayList<>();
        for (Iterator<SpecialMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SpecialMenu t = (SpecialMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            String perms = t.getPerms();


            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SpecialMenu> list, SpecialMenu t) {
        // 得到子节点列表
        List<SpecialMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SpecialMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SpecialMenu> getChildList(List<SpecialMenu> list, SpecialMenu t) {
        List<SpecialMenu> tlist = new ArrayList<>();
        Iterator<SpecialMenu> it = list.iterator();
        while (it.hasNext()) {
            SpecialMenu n = it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SpecialMenu> list, SpecialMenu t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SpecialMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SpecialMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMenuId(menu.getMenuId());
            router.setMenuName(menu.getMenuName());
            router.setPerms(menu.getPerms());
//            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath(), menu.getMenuId(), menu.getPerms()));
            List<SpecialMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
//            else if (isMenuFrame(menu)) {
//                router.setMeta(null);
//                List<RouterVo> childrenList = new ArrayList<RouterVo>();
//                RouterVo children = new RouterVo();
//                children.setPath(menu.getPath());
//                children.setComponent(menu.getComponent());
//                children.setName(StringUtils.capitalize(menu.getPath()));
//                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath(), menu.getMenuId(), menu.getPerms()));
//                children.setQuery(menu.getQuery());
//                childrenList.add(children);
//                router.setChildren(childrenList);
//            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
//                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getMenuId(), menu.getPerms()));
//                router.setPath("/");
//                List<RouterVo> childrenList = new ArrayList<RouterVo>();
//                RouterVo children = new RouterVo();
//                String routerPath = innerLinkReplaceEach(menu.getPath());
//                children.setPath(routerPath);
//                children.setComponent(UserConstants.INNER_LINK);
//                children.setName(StringUtils.capitalize(routerPath));
//                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath(), menu.getMenuId(), menu.getPerms()));
//                childrenList.add(children);
//                router.setChildren(childrenList);
//            }
            routers.add(router);
        }
        return routers;
    }


    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SpecialMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SpecialMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SpecialMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SpecialMenu menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS},
                new String[]{"", ""});
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SpecialMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SpecialMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }
}
