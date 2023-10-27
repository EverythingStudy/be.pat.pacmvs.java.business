package cn.staitech.anno.service;

import cn.staitech.system.api.domain.SysUser;

/**
 * 用户 业务层
 *
 * @author staitech
 */
public interface SysUserService {

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);


}
