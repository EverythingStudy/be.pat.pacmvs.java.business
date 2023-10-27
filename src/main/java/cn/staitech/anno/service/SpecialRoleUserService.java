package cn.staitech.anno.service;

import cn.staitech.anno.domain.special.SpecialRole;
import cn.staitech.anno.domain.special.SpecialRoleUser;
import cn.staitech.anno.domain.vo.special.*;
import cn.staitech.anno.utils.PageMaster;

import java.util.List;

/**
 * @author gjt.
 * @data 2023/6/1 10:26
 */
public interface SpecialRoleUserService {

    /**
     * 查询可用的角色
     *
     * @param specialId 专题id
     * @return list
     */
    List<SpecialRole> selectSpecialRole(Long specialId);

    /**
     * 查询用户所参与的专题
     *
     * @param userId 用户id
     * @return List<SpecialRoleUser>
     */
    List<SpecialRoleUser> selectUserId(Long userId);

    /**
     * 添加专题角色用户信息
     *
     * @param specialRoleUser 专题角色用户
     * @return true||false
     */
    int insert(SpecialRoleUserVo specialRoleUser);

    /**
     * 更新专题下的用户角色
     *
     * @param specialRoleUser 专题角色用户
     * @return true||false
     */
    int updateRole(SpecialRoleUserVo specialRoleUser);

    /**
     * 更新专题下的用户状态
     *
     * @param specialRoleUser 专题角色用户
     * @return true||false
     */
    int updateStatus(SpecialRoleUserStatusVo specialRoleUser);


    /**
     * 按照条件查询专题信息
     *
     * @param specialRoleUser 专题信息
     * @return list
     */
    PageMaster<SpecialRoleUserSelectResVo> selectList(SpecialRoleUserSelectVo specialRoleUser);

    /**
     * 根据用户查看详情信息
     *
     * @param userId 用户id
     * @return true||false
     */
    SpecialRoleUserSelectResVo selectUserSpecialBy(SpecialSelectByIn req);
}
