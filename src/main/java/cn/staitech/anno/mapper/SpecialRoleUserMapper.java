package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.special.SpecialRoleUser;
import cn.staitech.anno.domain.vo.special.SpecialRoleUserSelectResVo;
import cn.staitech.anno.domain.vo.special.SpecialRoleUserSelectVo;
import cn.staitech.anno.domain.vo.special.SpecialSelectByIn;

import java.util.List;

/**
 * 专题角色用户
 */
public interface SpecialRoleUserMapper {

    /**
     * 根据用户查看详情信息
     *
     * @param req
     * @return
     */
    SpecialRoleUserSelectResVo selectUserSpecialBy(SpecialSelectByIn req);

    /**
     * 查询用户所参与的专题
     *
     * @param userId 用户id
     * @return List<SpecialRoleUser>
     */
    List<SpecialRoleUser> selectUserId(Long userId);

    List<SpecialRoleUser> querySpecialRoleListByUserId(Long role);

    List<SpecialRoleUser> querySpecialRoleListByAdmin();

    /**
     * 添加专题角色用户信息
     *
     * @param specialRoleUser 专题角色用户
     * @return true||false
     */
    int insert(SpecialRoleUser specialRoleUser);

    /**
     * 更新专题下的用户角色
     *
     * @param specialRoleUser 专题角色用户
     * @return true||false
     */
    int updateRole(SpecialRoleUser specialRoleUser);

    /**
     * 更新专题下的用户状态
     *
     * @param specialRoleUser 专题信息
     * @return true||false
     */
    int updateStatus(SpecialRoleUser specialRoleUser);

    /**
     * 按照条件查询专题信息
     *
     * @param specialRoleUser 专题信息
     * @return true||false
     */
    List<SpecialRoleUserSelectResVo> selectList(SpecialRoleUserSelectVo specialRoleUser);

    /**
     * 按照条件查询专题信息
     *
     * @param SpecialRoleUser 专题信息
     * @return true||false
     */
    SpecialRoleUser select(SpecialRoleUser SpecialRoleUser);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int countSpecialRoleUserByRoleId(Long roleId);
}
