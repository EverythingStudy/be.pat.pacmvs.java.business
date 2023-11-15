package cn.staitech.anno.service.impl;

import cn.staitech.anno.mapper.SysUserMapper;
import cn.staitech.anno.service.SysUserService;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户 业务层处理
 *
 * @author staitech
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper userMapper;


    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }


    /**
     * 根据机构id查询用户列表
     * */
    @Override
    public List<SysUser> userList(){
        Long organizationId=SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
        return userMapper.userList(organizationId);
    }
}
