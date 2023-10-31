package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.special.*;
import cn.staitech.anno.mapper.SpecialRoleMapper;
import cn.staitech.anno.mapper.SpecialRoleUserMapper;
import cn.staitech.anno.service.SpecialRoleUserService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.exception.ServiceException;
import cn.staitech.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static cn.staitech.common.core.utils.PageUtils.startPage;

/**
 * @author gjt.
 * @data 2023/6/1 10:27
 */
@Service
public class SpecialRoleUserServiceImpl implements SpecialRoleUserService {

    @Resource
    private SpecialRoleUserMapper specialRoleUserMapper;

    @Resource
    private SpecialRoleMapper specialRoleMapper;


    /**
     * 查询可用的角色
     *
     * @param specialId 专题角色信息
     * @return list
     */
    @Override
    public List<SpecialRole> selectSpecialRole(Long specialId) {
        if (!Optional.ofNullable(specialId).isPresent()) {
            throw new ServiceException("专题不可为空");
        }
        SpecialRoleQueryVO specialRole = new SpecialRoleQueryVO();
        specialRole.setSpecialId(specialId);
        specialRole.setStatus("0");
        return specialRoleMapper.selectRoleList(specialRole);
    }

    /**
     * 查询用户所参与的专题
     *
     * @param userId 用户id
     * @return List<SpecialRoleUser>
     */
    @Override
    public List<SpecialRoleUser> selectUserId(Long userId) {
        return specialRoleUserMapper.selectUserId(userId);
    }


    /**
     * 根据用户查看详情信息
     *
     * @param userId 用户id
     * @return true||false
     */
    @Override
    public SpecialRoleUserSelectResVo selectUserSpecialBy(SpecialSelectByIn req) {
        SpecialRoleUserSelectResVo res = specialRoleUserMapper.selectUserSpecialBy(req);
        if (res != null) {
            if (LanguageUtils.isEn()) {
                res.setStatusFlag(Container.SPECIAL_ROLE_STATUS_MAP_EN.get(res.getStatus()));
            } else {
                res.setStatusFlag(Container.SPECIAL_ROLE_STATUS_MAP.get(res.getStatus()));
            }
        }
        return res;
    }

    /**
     * 添加专题角色用户信息
     *
     * @param req 专题角色用户
     * @return true||false
     */
    @Override
    public int insert(SpecialRoleUserVo req) {
        SpecialRoleUser specialRoleUser = new SpecialRoleUser();

        specialRoleUser.setUserId(req.getUserId());
        specialRoleUser.setSpecialId(req.getSpecialId());
        // 根据专题id和用户判断当前用户是否在专题中
        SpecialRoleUser specialRoleUsers = specialRoleUserMapper.select(specialRoleUser);
        if (specialRoleUsers != null) {
            throw new RuntimeException("当前专题内已有该用户，禁止重复添加");
        }
        specialRoleUser.setRoleId(req.getRoleId());
        specialRoleUser.setCreateBy(SecurityUtils.getUserId());
        specialRoleUser.setUpdateBy(SecurityUtils.getUserId());
        int res = specialRoleUserMapper.insert(specialRoleUser);
        if (res <= 0) {
            throw new ServiceException(MessageSource.M("OPERATE_ERROR"));
        }
        return res;
    }

    /**
     * 更新专题下的用户角色
     *
     * @param req 专题角色用户
     * @return true||false
     */
    @Override
    public int updateRole(SpecialRoleUserVo req) {
        SpecialRoleUser specialRoleUser = new SpecialRoleUser();
        specialRoleUser.setRoleId(req.getRoleId());
        specialRoleUser.setUserId(req.getUserId());
        specialRoleUser.setSpecialId(req.getSpecialId());
        specialRoleUser.setUpdateBy(SecurityUtils.getUserId());
        if (specialRoleMapper.selectRoleById(req.getRoleId()) == null) {
            throw new ServiceException("未查询到该角色信息");
        }
        return specialRoleUserMapper.updateRole(specialRoleUser);
    }

    /**
     * 更新专题下的用户状态
     *
     * @param req 专题角色用户
     * @return true||false
     */
    @Override
    public int updateStatus(SpecialRoleUserStatusVo req) {
        SpecialRoleUser specialRoleUser = new SpecialRoleUser();
        specialRoleUser.setRoleId(req.getRoleId());
        specialRoleUser.setUserId(req.getUserId());
        specialRoleUser.setSpecialId(req.getSpecialId());
        specialRoleUser.setStatus(req.getStatus());
        specialRoleUser.setUpdateBy(SecurityUtils.getUserId());
        if (specialRoleUserMapper.select(specialRoleUser) == null) {
            throw new ServiceException("专题中未查询到该用户信息");
        }
        return specialRoleUserMapper.updateStatus(specialRoleUser);
    }

    /**
     * 按照条件查询专题信息
     *
     * @param specialRoleUser 专题信息
     * @return true||false
     */
    @Override
    public PageMaster<SpecialRoleUserSelectResVo> selectList(SpecialRoleUserSelectVo specialRoleUser) {
        startPage(specialRoleUser.getPageNum(), specialRoleUser.getPageSize());
        List<SpecialRoleUserSelectResVo> specialRoleUserSelectResVos = specialRoleUserMapper.selectList(specialRoleUser);
        for (SpecialRoleUserSelectResVo s : specialRoleUserSelectResVos) {
            if (LanguageUtils.isEn()) {
                s.setStatusFlag(Container.SPECIAL_ROLE_STATUS_MAP_EN.get(s.getStatus()));
            } else {
                s.setStatusFlag(Container.SPECIAL_ROLE_STATUS_MAP.get(s.getStatus()));
            }
        }
        return new PageMaster<>(specialRoleUserSelectResVos);
    }
}
