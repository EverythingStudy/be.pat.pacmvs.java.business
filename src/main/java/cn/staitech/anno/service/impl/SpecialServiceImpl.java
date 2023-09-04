package cn.staitech.anno.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.staitech.anno.constant.ProjectConstant;
import cn.staitech.anno.domain.Group;
import cn.staitech.anno.domain.project.ProjectExt;
import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.special.SpecialMenu;
import cn.staitech.anno.domain.special.SpecialReclaim;
import cn.staitech.anno.domain.special.SpecialRole;
import cn.staitech.anno.domain.special.SpecialRoleUser;
import cn.staitech.anno.domain.vo.special.*;
import cn.staitech.anno.enums.SpecialEnum;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.service.GroupService;
import cn.staitech.anno.service.SpecialMenuService;
import cn.staitech.anno.service.SpecialRoleService;
import cn.staitech.anno.service.SpecialService;
import cn.staitech.anno.utils.TimeUtils;
import cn.staitech.common.core.exception.ServiceException;
import cn.staitech.common.core.exception.auth.NotLoginException;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import cn.staitech.system.api.model.LoginUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.staitech.anno.constant.special.SpecialRoleConstant.*;
import static cn.staitech.anno.constant.special.SpecialRoleConstant.ANNOTATOR_MENU;
import static cn.staitech.anno.enums.SpecialEnum.del_flag_1;

import static cn.staitech.common.core.constant.SysRoleConstant.SPECIAL;
import static cn.staitech.common.core.utils.SysRoleUtil.getSort;

/**
 * @author gjt.
 * @data 2023/5/29 9:14
 */
@Service
@Slf4j
public class SpecialServiceImpl extends ServiceImpl<SpecialMapper, Special> implements SpecialService {
    @Resource
    private SpecialMenuService specialMenuService;
    @Resource
    private SpecialMapper specialMapper;

    @Resource
    private SpecialReclaimMapper specialReclaimMapper;

    @Resource
    private SubImageMapper subImageMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private SpecialRoleService specialRoleService;

    @Resource
    private SpecialRoleMapper specialRoleMapper;

    @Resource
    private SpecialRoleUserMapper specialRoleUserMapper;

    @Resource
    private GroupService groupService;

    @Resource
    private ProjectExtMapper projectExtMapper;

    @Resource
    private SlideMapper slideMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询专题阅片下的专题列表
     *
     * @return Special
     */
    @Override
    public List<SpecialResVo> selectSpecialReadFilm(SpecialSelectVo req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);
        Special special = new Special();
        org.springframework.beans.BeanUtils.copyProperties(req, special);
        special.setUserName(req.getCreateBy());
        special.setDelFlag(SpecialEnum.del_flag_0.value());
        // 判断当前用户是否为admin
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            special.setCreateBy(SecurityUtils.getUserId());
            special.setSruStatus(0L);
        }
        List<SpecialResVo> specialResVos = specialMapper.selectSpecialReadFilm(special);
        // 根据专题id查询项目数量
        for (SpecialResVo specialResVo : specialResVos) {
            int projectNum = projectMapper.selectProjectCount(specialResVo.getSpecialId());
            specialResVo.setProjectNum((long) projectNum);
        }
        return specialResVos;
    }

    /**
     * 根据主键查询详情
     *
     * @param specialId 专题id
     * @return Special
     */
    @Override
    public SpecialResVo selectSpecialId(Long specialId) {
        if (!Optional.ofNullable(specialId).isPresent()) {
            throw new ServiceException("专题不可为空");
        }
        return specialMapper.selectSpecialId(specialId);
    }

    /**
     * 根据主键查询详情
     *
     * @param specialId 专题id
     * @return
     */
    @Override
    public Special selectSpecialById(Long specialId) {
        return specialMapper.selectSpecialById(specialId);
    }

    /**
     * 查询专题信息
     *
     * @param special 专题信息
     * @return List<Special>
     */
    @Override
    public List<SpecialResVo> selectList(Special special) {
        List<SpecialResVo> specialResVos = specialMapper.selectList(special);
        // 根据专题id查询项目数量
        for (SpecialResVo specialResVo : specialResVos) {
            int projectNum = projectMapper.selectProjectCount(specialResVo.getSpecialId());
            specialResVo.setProjectNum((long) projectNum);
//            ProjectExt projectExt = new ProjectExt();
//            projectExt.setSpecialId(specialResVo.getSpecialId());
//            // 查询项目列表
//            int slideNum = 0;
//            List<ProjectExt> projects = projectExtMapper.selectProjectList(projectExt);
//            specialResVo.setProjectNum((long) projects.size());
//            for (ProjectExt projectExt1 : projects) {
//                // 查询项目下切片数量
//                slideNum += slideMapper.selectCheckNum(projectExt1.getProjectId());
//            }
            int slideNum = specialMapper.selectSpecialSlideCount(specialResVo.getSpecialId());
            specialResVo.setSlideNum((long) slideNum);
        }
        return specialResVos;
    }

    /**
     * 专题统计
     *
     * @param special 专题信息
     * @return List<Special>
     */
    @Override
    public List<SpecialStatisticsListVO> specialStatistics(SpecialStatisticsQueryVO special) {
        List<SpecialStatisticsListVO> specialStatistics;
        if (SpecialStatisticsQueryVO.isAdmin(special.getUserId())) {
            specialStatistics = specialMapper.specialStatisticsAdmin(special);
            specialStatistics.forEach(s -> {
                // 查询专题下的项目ID
                List<Long> projectId = specialMapper.queryProjectIdAdminBySpecialId(s.getSpecialId());
                getCompletionRate(s, projectId);
            });
        } else {
            // 非admin用户查询专题列表(包含项目总数)
            specialStatistics = specialMapper.specialStatistics(special);
            specialStatistics.forEach(s -> {
                SpecialStatisticsListVO build = SpecialStatisticsListVO.builder().userId(s.getUserId()).specialId(s.getSpecialId()).build();
                // 查询专题下的项目ID
                List<Long> projectId = specialMapper.queryProjectIdBySpecialId(build);
                getCompletionRate(s, projectId);
            });
        }
        return specialStatistics;
    }

    /**
     * 获取完成率
     *
     * @param s
     * @param projectId
     */
    private void getCompletionRate(SpecialStatisticsListVO s, List<Long> projectId) {
//        AtomicInteger projectIncomplete = new AtomicInteger();
        if (ObjectUtils.isEmpty(projectId)) {
            s.setProjectComplete(0);
            s.setProjectIncomplete(0);
            s.setSpecialCompletionRate("0" + "%");
        } else {
            // 统计未完成项目数
        	int incomplete = 0;
        	for(Long p :projectId){
                // 查询项目下所有切片的人工诊断状态
               /* List<Long> longs = specialMapper.queryDiagnosisByProjectId(p);
                if (ObjectUtils.isEmpty(longs)) {
                    projectIncomplete.incrementAndGet();
                } else {
                    projectIncomplete.addAndGet(longs.contains(0) ? 1 : 0);
                }*/
                //通过项目id查询所有切片（备注：已完成项目的定义是：当前项目下的所有切片均进行了人工诊断）
            	 List<SpecialSlideStatisticsVO> ssvoList = specialMapper.queryDiagnosisByProjectId(p);
            	 if(CollectionUtils.isNotEmpty(ssvoList)){
            		 for(SpecialSlideStatisticsVO vo: ssvoList){
            			 //人工诊断状态：0未诊断，1已诊断
            			 int diagnosis = vo.getDiagnosis();
            			 int totalCount = vo.getTotalCount();
            			 if(diagnosis == 0){
            				 if(totalCount > 0){
            					 incomplete++;
            					 log.info("专题名称1:"+s.getSpecialName()+" 未完成数量是："+incomplete);
            				 }
            			 }
            		 }
            	 }else{
            		 incomplete++;
            		 log.info("专题名称2:"+s.getSpecialName()+" 未完成数量是："+incomplete);
            	 }
        	}
        	log.info("专题名称3:"+s.getSpecialName()+" 未完成数量是："+incomplete);
            Integer total = s.getProjectTotal();
//            int incomplete = projectIncomplete.intValue();
            int complete = total - incomplete;

            s.setProjectComplete(complete);
            s.setProjectIncomplete(incomplete);

            // 完成率
            int completionRate = NumberUtil.div(String.valueOf(complete), String.valueOf(total), 2).multiply(BigDecimal.valueOf(100)).intValue();
            s.setSpecialCompletionRate(completionRate + "%");
        }
    }

    /**
     * 添加专题信息
     *
     * @param req 专题信息
     * @return List
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SpecialInsertVo req) {
        Special special = new Special();
        special.setSpecialNumber(req.getSpecialNumber());
        // 查询编号是否存在
        Special specialNumber = specialMapper.selectSpecialNumber(special);
        if (specialNumber != null) {
            throw new ServiceException("当前专题编号已存在,禁止重复添加");
        }
        Special specials = new Special();
        specials.setSpecialName(req.getSpecialName());
        Special specialName = specialMapper.selectSpecialNumber(specials);
        if (specialName != null) {
            throw new ServiceException("当前专题名称已存在,禁止重复添加");
        }
        special.setSpecialName(req.getSpecialName());
        BeanUtils.copyProperties(req, special);
        special.setCreateBy(SecurityUtils.getUserId());
        special.setUpdateBy(SecurityUtils.getUserId());
        int result = specialMapper.insert(special);
        if (result > 0) {
            // 专题添加成功后,专题用户表中添加专题负责人
            insertSpecialRole(special.getSpecialId());
            //刷新用户权限
            flushPrivileges();
            // 创建默认的分组
            insertGroup(special.getSpecialId());
        }
        return result;
    }

    /**
     * 创建专题后刷新权限
     * @param
     */
    private void flushPrivileges() {
        String token = SecurityUtils.getToken();
        Long userId = SecurityUtils.getUserId();
        if (token == null) {
            throw new NotLoginException("未提供token");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new NotLoginException("无效的token");
        }
        List<cn.staitech.system.api.domain.SpecialRole> specialRoles = new ArrayList<>();
        List<SpecialRoleUser> specialRoleUsers = specialRoleService.querySpecialRoleListByUserId(userId);

        specialRoleUsers.forEach(s -> {
            Set permsList = new HashSet<>();
            List<SpecialMenu> specialRolePerms = specialMenuService.querySpecialRolePermsByRoleId(s.getRoleId());

            // 管理员拥有所有权限
            if (SysUser.isAdmin(userId)) {
                permsList.add("*:*:*");
            } else {
                for (SpecialMenu role : specialRolePerms) {
                    permsList.add(role.getPerms());
                }
            }
            cn.staitech.system.api.domain.SpecialRole specialRole = cn.staitech.system.api.domain.SpecialRole.builder().specialId(s.getSpecialId()).roleId(s.getRoleId()).specialPermissions(permsList).build();
            specialRoles.add(specialRole);
        });

        loginUser.setSpecialRoleList(specialRoles);
        String userKey = ProjectConstant.LOGIN_TOKEN_KEY+loginUser.getToken();
        redisTemplate.opsForValue().set(userKey,loginUser,240l,TimeUnit.MINUTES);
    }

    /**
     * 更新专题信息
     *
     * @param req 专题信息
     * @return true||false
     */
    @Override
    public int update(SpecialUpdateVo req) {
        Special special = new Special();
        special.setSpecialNumber(req.getSpecialNumber());
        special.setSpecialId(req.getSpecialId());
        // 查询编号是否存在
        Special specialNumber = specialMapper.selectSpecialNumber(special);
        if (specialNumber != null) {
            throw new ServiceException("当前专题编号已存在,禁止重复添加");
        }
        Special specials = new Special();
        specials.setSpecialName(req.getSpecialName());
        specials.setSpecialId(req.getSpecialId());
        Special specialName = specialMapper.selectSpecialNumber(specials);
        if (specialName != null) {
            throw new ServiceException("当前专题名称已存在,禁止重复添加");
        }
        // 判断当前指标中的标签是否标注使用
        if(req.getIndicatorId() != null){
            // 查询专题详情
            Special specialBy = specialMapper.selectSpecialById(req.getSpecialId());
            if(!Objects.equals(specialBy.getIndicatorId(), req.getIndicatorId())){
                List<Special> specialList = specialMapper.selectSpecialCategoryList(req.getSpecialId());
                if(specialList.size() > 0){
                    throw new ServiceException("当前病理指标使用中,禁止取消关联");
                }
            }
        }
        BeanUtils.copyProperties(req, special);
        special.setUpdateBy(SecurityUtils.getUserId());
        specialMapper.update(special);
        return specialMapper.update(special);
    }

    /**
     * 更新专题交付状态
     */
    @Override
    public int updateDeliveryStatus(Special special) {
        return specialMapper.update(special);
    }

    /**
     * 删除专题表中信息
     *
     * @param req 专题信息
     * @return true||false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDelFlag(SpecialDeleteVo req) {
        Special special = new Special();
        special.setSpecialId(req.getSpecialId());
        special.setDelFlag(req.getDelFlag());
        special.setUpdateBy(SecurityUtils.getUserId());
        int result = specialMapper.updateDelFlag(special);
        // 如果更新状态成功且状态为1
        if (result > 0 && special.getDelFlag().equals(del_flag_1.value())) {
            // 添加专题回收表中
            SpecialReclaim specialReclaim = new SpecialReclaim();
            specialReclaim.setReclaimBy(SecurityUtils.getUserId());
            specialReclaim.setSpecialId(special.getSpecialId());
            specialReclaim.setExpireTime(TimeUtils.dateIncreases());
            // 查询项目数量
            int projectNum = projectMapper.selectProjectCount(req.getSpecialId());
            specialReclaim.setProjectNum((long) projectNum);
            // 查询切片数量
            int slideNum = subImageMapper.selectSpecialCounts(req.getSpecialId());
            specialReclaim.setSlideNum((long) slideNum);
            specialReclaimMapper.insert(specialReclaim);
        }
        return result;
    }

    /**
     * 更新专题状态
     *
     * @param req 专题信息
     * @return true||false
     */
    @Override
    public int updateStatus(SpecialStatusVo req) {
        Special special = new Special();
        special.setSpecialId(req.getSpecialId());
        special.setStatus(req.getStatus());
        special.setUpdateBy(SecurityUtils.getUserId());
        return specialMapper.updateStatus(special);
    }

    /**
     * 添加默认角色
     *
     * @param specialId
     */
    public void insertSpecialRole(Long specialId) {
        for (String i : ROLE_TYPE) {
            SpecialRole specialRole = SpecialRole.builder().specialId(specialId).roleName(i).createBy(SecurityUtils.getUserId()).build();

            // 设置角色编号
            List<SpecialRole> specialRoleList = specialRoleMapper.selectRoleListBySpecialId(specialId);
            if (specialRoleList.size() == 0) {
                specialRole.setRoleSort(SPECIAL);
            } else {
                String roleSortLatest = specialRoleList.get(specialRoleList.size() - 1).getRoleSort();
                String roleSort = getSort(roleSortLatest);
                specialRole.setRoleSort(roleSort);
            }

            //添加角色表中
            if (i.equals(RESPONSIBLE_ROLE)) {
                specialRole.setRoleKey(RESP);
                specialRole.setMenuIds(RESPONSIBLE_MENU);
            }
            if (i.equals(ANNOTATOR_ROLE)) {
                specialRole.setRoleKey(ANNO);
                specialRole.setMenuIds(ANNOTATOR_MENU);
            }
            if (i.equals(READER_ROLE)) {
                specialRole.setRoleKey(READ);
                specialRole.setMenuIds(READER_MENU);
            }

            specialRoleMapper.insertRole(specialRole);

            // 添加角色权限
            specialRoleService.insertRoleMenu(specialRole);

            // 创建者设置为专题负责人
            if (i.equals(RESPONSIBLE_ROLE)) {
                // 添加到专题角色用户表中
                SpecialRoleUser specialRoleUser = new SpecialRoleUser();
                specialRoleUser.setRoleId(specialRole.getRoleId());
                specialRoleUser.setSpecialId(specialId);
                specialRoleUser.setUserId(SecurityUtils.getUserId());
                specialRoleUser.setUpdateBy(SecurityUtils.getUserId());
                specialRoleUser.setCreateBy(SecurityUtils.getUserId());
                specialRoleUserMapper.insert(specialRoleUser);
            }
        }
    }

    /**
     * 专题下创建默认分组
     *
     * @param specialId 专题id
     * @return true || false
     */
    public boolean insertGroup(Long specialId) {
        for (int groupName = 1; groupName < 9; groupName++) {
            System.out.println(groupName);
            for (int gender = 0; gender < 2; gender++) {
                for (int reasons = 1; reasons < 3; reasons++) {
                    Group group = new Group();
                    group.setGroupName(String.valueOf(groupName));
                    group.setGender(gender);
                    group.setReasons(reasons);
                    group.setSpecialId(specialId);
                    group.setCreateBy(SecurityUtils.getUserId());
                    group.setDosage("0");
                    groupService.insertSelective(group);
                }
            }
        }
        return false;
    }


}
