package cn.staitech.anno.service;

import cn.staitech.system.api.domain.SysUser;

import java.util.List;

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
    SysUser selectUserById(Long userId);

    /**
     * 根据机构ID查询用户列表
     * */
    List<SysUser> userList();
}
