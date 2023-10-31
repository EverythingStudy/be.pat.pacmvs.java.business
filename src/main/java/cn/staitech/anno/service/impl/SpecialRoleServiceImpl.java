package cn.staitech.anno.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.staitech.anno.mapper.SpecialRoleMapper;
import cn.staitech.anno.mapper.SpecialRoleMenuMapper;
import cn.staitech.anno.mapper.SpecialRoleUserMapper;
import cn.staitech.anno.service.SpecialRoleService;
import cn.staitech.anno.vo.special.SpecialRole;
import cn.staitech.anno.vo.special.SpecialRoleMenu;
import cn.staitech.anno.vo.special.SpecialRoleQueryVO;
import cn.staitech.anno.vo.special.SpecialRoleUser;
import cn.staitech.common.core.exception.ServiceException;
import cn.staitech.common.core.utils.StringUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static cn.staitech.common.core.constant.SysRoleConstant.*;
import static cn.staitech.common.core.constant.UserConstants.*;
import static cn.staitech.common.core.utils.SysRoleUtil.getSort;

/**
 * @Description ：专题角色 业务层处理
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialRoleServiceImpl
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/5/30 星期二 15:48
 */
@Service
public class SpecialRoleServiceImpl implements SpecialRoleService {
    @Resource
    private SpecialRoleMapper specialRoleMapper;

    @Resource
    private SpecialRoleMenuMapper specialRoleMenuMapper;

    @Resource
    private SpecialRoleUserMapper specialRoleUserMapper;

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SpecialRole> selectRoleList(SpecialRoleQueryVO role) {
        List<SpecialRole> list = specialRoleMapper.selectRoleList(role);
        list.forEach(o -> {
            o.setRoleLevelName(ROLE_LEVEL.get(o.getRoleLevel()));
            o.setStatusName(ROLE_STATUS.get((o.getStatus())));
        });

        return list;
    }

    /**
     * 根据用户id查询专题角色列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<SpecialRoleUser> querySpecialRoleListByUserId(Long userId) {
        List<SpecialRoleUser> specialRoleUsers;
        // 管理员拥有所有权限
        if (SysUser.isAdmin(userId)) {
            specialRoleUsers = specialRoleUserMapper.querySpecialRoleListByAdmin();
        } else {
            specialRoleUsers = specialRoleUserMapper.querySpecialRoleListByUserId(userId);
        }
        return specialRoleUsers;
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SpecialRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SpecialRole info = specialRoleMapper.checkRoleNameUnique(role);
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return FALSE;
        }
        return TRUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SpecialRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SpecialRole info = specialRoleMapper.checkRoleKeyUnique(role);
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return FALSE;
        }
        return TRUE;
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SpecialRole role) {
        if (!role.getRoleName().matches(VERIFY_CHINESE)) {
            throw new ServiceException("新增角色'" + role.getRoleName() + "'失败，角色名称只允许输入汉字");
        }
        if (!role.getRoleKey().matches(VERIFY_LETTER_NUMBER)) {
            throw new ServiceException("新增角色'" + role.getRoleName() + "'失败，权限标识只允许输入字母和数字");
        }
        // 设置角色编号
        List<SpecialRole> sysRoles = specialRoleMapper.selectRoleListBySpecialId(role.getSpecialId());
        if (ObjectUtil.isEmpty(sysRoles)) {
            role.setRoleSort(SPECIAL);
        } else {
            String roleSortLatest = sysRoles.get(sysRoles.size() - 1).getRoleSort();
            String roleSort = getSort(roleSortLatest);
            role.setRoleSort(roleSort);
        }
        role.setCreateBy(SecurityUtils.getUserId());
        role.setUpdateBy(SecurityUtils.getUserId());

        // 新增角色信息
        specialRoleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SpecialRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SpecialRoleMenu> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds()) {
            SpecialRoleMenu specialRoleMenu = new SpecialRoleMenu();
            specialRoleMenu.setRoleId(role.getRoleId());
            specialRoleMenu.setMenuId(menuId);
            list.add(specialRoleMenu);
        }
        if (list.size() > 0) {
            rows = specialRoleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SpecialRole role) {
        role.setUpdateBy(SecurityUtils.getUserId());
        // 修改角色信息
        specialRoleMapper.updateRole(role);
        // 删除角色与菜单关联
        specialRoleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SpecialRole selectRoleById(Long roleId) {
        return specialRoleMapper.selectRoleById(roleId);
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(Long roleId) {
        SpecialRole specialRole = specialRoleMapper.selectRoleById(roleId);
        if (ObjectUtil.isNull(specialRole)) {
            throw new ServiceException(String.format("当前角色ID:%1$s，暂无数据", roleId));
        }
        if (countUserRoleByRoleId(roleId) > 0 && UNIQUE.equals(specialRole.getStatus())) {
            throw new ServiceException(String.format("当前角色%1$s有绑定的用户，禁止禁用", specialRole.getRoleName()));
        }
        if (StringUtils.isNotEmpty(specialRole.getStatus()) && UNIQUE.equals(specialRole.getStatus())) {
            specialRole.setStatus(NOT_UNIQUE);
        } else if ((StringUtils.isNotEmpty(specialRole.getStatus()) && NOT_UNIQUE.equals(specialRole.getStatus()))) {
            specialRole.setStatus(UNIQUE);
        }
        specialRole.setUpdateBy(SecurityUtils.getUserId());

        return specialRoleMapper.updateRole(specialRole);
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return specialRoleUserMapper.countSpecialRoleUserByRoleId(roleId);
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleById(Long roleId) {
        SpecialRole specialRole = specialRoleMapper.selectRoleById(roleId);
        if (ObjectUtil.isNull(specialRole)) {
            throw new ServiceException(String.format("当前角色ID:%1$d，暂无数据", roleId));
        }
        if (UNIQUE.equals(specialRole.getStatus())) {
            throw new ServiceException(String.format("当前角色%1$s为启用状态，禁止删除", specialRole.getRoleName()));
        }
        if (countUserRoleByRoleId(roleId) > 0) {
            throw new ServiceException(String.format("当前角色%1$s有绑定的用户，禁止删除", specialRole.getRoleName()));
        }
        // 删除角色与菜单关联
        specialRoleMenuMapper.deleteRoleMenuByRoleId(roleId);
        return specialRoleMapper.deleteRoleById(roleId);
    }
}
