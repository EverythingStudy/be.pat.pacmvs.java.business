package cn.staitech.anno.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.staitech.anno.domain.UserManual;
import cn.staitech.anno.mapper.UserManualMapper;
import cn.staitech.anno.service.UserManualService;

/**
 * <p>
 * 用户手册 服务实现类
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-04
 */
@Service
public class UserManualServiceImpl extends ServiceImpl<UserManualMapper, UserManual> implements UserManualService {

}
