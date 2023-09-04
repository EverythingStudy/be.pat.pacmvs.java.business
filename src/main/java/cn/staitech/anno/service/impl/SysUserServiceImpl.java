package cn.staitech.anno.service.impl;

import cn.staitech.anno.mapper.SysUserMapper;
import cn.staitech.anno.service.SysUserService;
import cn.staitech.system.api.domain.SysUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 用户 业务层处理
 *
 * @author staitech
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    
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
    
   
    

    
}
