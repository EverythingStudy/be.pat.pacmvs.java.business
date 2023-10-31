package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Group;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.RecentlyVisited;
import cn.staitech.anno.domain.project.ProjectExt;
import cn.staitech.anno.domain.project.ProjectPo;
import cn.staitech.anno.domain.project.in.OperateProjectIn;
import cn.staitech.anno.domain.project.in.ProjectListQueryIn;
import cn.staitech.anno.domain.project.in.ProjectRemoveIn;
import cn.staitech.anno.domain.project.out.*;
import cn.staitech.anno.domain.project.out.NavigationBarData;
import cn.staitech.anno.domain.project.out.NavigationBarDataOut;
import cn.staitech.anno.domain.projectgroup.ProjectGroup;
import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.enums.ReasonsEnum;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.service.ProjectExtService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.nacos.shaded.com.google.common.collect.ImmutableMap;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: wudi
 * @Date: 2023/5/29 18:05
 * @desc: 项目模块业务层
 */
@Service
public class ProjectExtServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectExtService {

    private static final Logger log = LoggerFactory.getLogger(ProjectExtServiceImpl.class);
    private static final String CHECK_FLAG = "1";
    @Resource
    private SpecialMapper specialMapper;
    @Resource
    private SystemDictMapper systemDictMapper;
    @Resource
    private ProjectExtMapper projectExtMapper;
    @Resource
    private ProjectGroupMapper projectGroupMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private RecentlyVisitedMapper recentlyVisitedMapper;

    /**
     * 获得系统、脏器下拉框
     *
     * @return
     */
    @Override
    public List<SystemDictOut> getSystemDictOld() {
        log.info("病理、脏器下拉框接口开始：");
        List<SystemDictOut> collect = systemDictMapper.selectSystemDict();
        //对象浅拷贝
        List<SystemDictOut> menuList = new ArrayList<>(collect);
        // 遍历两次data来组装带有children关联性的对象，如果找到子级就删除menuList的数据
        /*for (SystemDictOut entity : collect) {
            for (SystemDictOut entity2 : collect) {
                //如果本级id与数据的父id相同，就说明是子父级关系
                if (entity.getDictId().equals(entity2.getParentId())) {
                    entity.getChildrenList().add(entity2);
                    menuList.remove(entity2);
                }
            }
        }*/
        return menuList;
    }

    /**
     * @return
     */
    @Override
    public List<SystemDictOut> getSystemDict(Long dictType) {
        log.info("获得系统脏器下拉框接口开始");
        //一级下拉框查询查询
        if (Objects.isNull(dictType) || dictType == 0) {

            return systemDictMapper.selectFirst(0L);
        } else {
            return systemDictMapper.selectFirst(dictType);

        }
    }

    /**
     * 项目列表查询
     *
     * @param req
     * @return
     */
    @Override
    public PageResponse<ProjectListQueryOut> getProjectList(ProjectListQueryIn req) {
        log.info("项目列表接口查询开始：");
        //创建响应
        PageResponse resp = new PageResponse<>();
        resp.setPageNum(req.getPageNum());
        resp.setPageSize(req.getPageSize());
        //设置持久层入参
        ProjectExt project = new ProjectExt();
        BeanUtils.copyBeanProp(project, req);
        Map<String, Object> createTime = req.getCreateTimeParams();
        if (createTime.containsKey("beginTime")) {
            project.setBeginTime((Date) createTime.get("beginTime"));
        }
        if (createTime.containsKey("endTime")) {
            project.setEndTime((Date) createTime.get("endTime"));
        }

        //分页查询
        Page<ProjectExt> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<ProjectExt> projects = projectExtMapper.selectProjectList(project);
        //处理响应
        if (!CollectionUtils.isEmpty(projects)) {
            List<ProjectListQueryOut> content = projects.stream().map(e -> {
                ProjectListQueryOut projectListQueryOut = new ProjectListQueryOut();
                BeanUtils.copyBeanProp(projectListQueryOut, e);
                return projectListQueryOut;
            }).collect(Collectors.toList());
            resp.setList(content);
        }
        resp.setTotal(page.getTotal());
        resp.setPages(page.getPages());
        return resp;
    }

    /**
     * 项目编辑
     *
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R operateProject(OperateProjectIn req) {
        log.info("项目编辑接口开始：");

        Long userId = SecurityUtils.getUserId();
        //Long userId = 123L;
        //处理入参
        ProjectExt projectExt = new ProjectExt();
        BeanUtils.copyBeanProp(projectExt, req);

        //校验项目名称
        if (!checkProject(req, CHECK_FLAG)) {
            return R.fail(MessageSource.M("PROJECT_NAME_EXIST"));
        }
        //校验脏器
        if (!checkProject(req, null)) {
            return R.fail(MessageSource.M("VISCUS_CODE_EXIST"));
        }
        //新增
        if (ObjectUtils.isEmpty(req.getProjectId()) || req.getProjectId() == 0) {
            //交付校验
            LambdaQueryWrapper<Special> specialWrapper = new LambdaQueryWrapper<>();
            specialWrapper.eq(Special::getSpecialId, req.getSpecialId());
            specialWrapper.eq(Special::getDeliveryStatus, 0);
            specialWrapper.eq(Special::getDelFlag, 0);
            Integer integer = specialMapper.selectCount(specialWrapper);
            if (integer > 0) {
                return R.fail(MessageSource.M("SPECIAL_EXIST_NON_DELIVERY"));
            }
            //判断专题分组
            LambdaQueryWrapper<Group> groupWrapper = new LambdaQueryWrapper<>();
            Integer integer1 = groupMapper.selectCount(groupWrapper);
            if (integer1 <= 0) {
                return R.fail(MessageSource.M("SPECIAL_NOTEXIST_GROUP"));
            }

            projectExt.setProjectId(null);
            projectExt.setCreateBy(userId);
            projectExt.setCreateTime(new Date());
            projectExtMapper.insert(projectExt);

            projectGroupMapper.insertProjectGroupByGroup(projectExt);

        } else {
            projectExt.setUpdateBy(userId);
            projectExt.setUpdateTime(new Date());
            projectExtMapper.update(projectExt);
        }
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    /**
     * 项目详情接口
     *
     * @param projectId
     * @return 项目详情
     */
    @Override
    public ProjectInfoOut getProjectById(Long projectId) {
        log.info("项目详情接口开始：");
        ProjectExt projectExt = projectExtMapper.selectById(projectId);
        ProjectInfoOut resp = new ProjectInfoOut();
        if (!ObjectUtils.isEmpty(projectExt)) {
            BeanUtils.copyBeanProp(resp, projectExt);
        }
        return resp;
    }

    /**
     * 项目删除
     *
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R projectRemove(ProjectRemoveIn req) {
        log.info("项目删除接口开始：");
        //校验是否绑定切片
        int i = projectExtMapper.selectCountSlide(req.getProjectId());
        if (i > 0) {
            return R.fail(MessageSource.M("PROJECT_SLIDE_EXIST"));
        }

        Long userId = SecurityUtils.getUserId();
        //Long userId = 123l;
        //修改状态
        projectExtMapper.updateDelFlag(req.getProjectId(), userId);
        projectGroupMapper.updateDelFlag(req.getProjectId(), userId);
        // 删除最近访问表中数据
        QueryWrapper<RecentlyVisited> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", req.getProjectId());
        recentlyVisitedMapper.delete(queryWrapper);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    /**
     * 查询项目导航栏
     *
     * @param req
     * @return
     */
    @Override
    public NavigationBarQueryOut getNavigationBar(Long req) {
        log.info("导航栏查询接口开始：");
        //创建响应
        NavigationBarQueryOut resp = new NavigationBarQueryOut();
        List<NavigationBarData> date = projectExtMapper.selectProjectAll(req);
        date.forEach(e -> {
            List<Integer> integers = projectGroupMapper.selectReasonsList(e.getProjectId());
            List<NavigationBarDataOut> list = new ArrayList<>();
            integers.forEach(in -> {
                NavigationBarDataOut ret = new NavigationBarDataOut();
                ret.setReasons(in);
                ret.setReasonsDesc(ReasonsEnum.getEnumLabelByValue(in));
                list.add(ret);
            });
            e.setReasonsList(list);
        });

        resp.setRespList(date);
        resp.setProjectTotal(date.size());
        return resp;
    }

    /**
     * @param projectId
     * @return 组间报告
     */
    @Override
    public R<InterGroupReportOut> getInterGroupReport(Long projectId) {
        log.info("组间报告接口开始");
        //判断是否存在未完成分析的切片
        int i = projectExtMapper.countNotReady(projectId);
        if (i > 0) {
            return R.fail(MessageSource.M("PROJECT_NO_READY"));
        }
        //todo 数据拼接返回
        return R.ok();
    }

    /**
     * @param specialId
     * @return 一键创建权限
     */
    @Override
    public R<Boolean> getCreateStatus(Long specialId) {
        log.info("一键创建权限获取接口开始：");
        //判断是否交付
        LambdaQueryWrapper<Special> specialWrapper = new LambdaQueryWrapper<>();
        specialWrapper.eq(Special::getSpecialId, specialId);
        specialWrapper.eq(Special::getDeliveryStatus, 0);
        specialWrapper.eq(Special::getDelFlag, 0);
        Integer integer = specialMapper.selectCount(specialWrapper);
        if (integer > 0) {
            return R.ok(false);
        }
        //判断专题分组
        /*LambdaQueryWrapper<Group> groupWrapper = new LambdaQueryWrapper<>();
        groupWrapper.eq(Group::getSpecialId,specialId);
        groupWrapper.eq(Group::getDelFlag,0);
        Integer integer1 = groupMapper.selectCount(groupWrapper);
        if(integer1<=0){
            return R.ok(false);
        }*/
        //判断项目数
        /*LambdaQueryWrapper<ProjectPo> projectWrapper = new LambdaQueryWrapper<>();
        projectWrapper.eq(ProjectPo::getSpecialId,specialId);
        projectWrapper.eq(ProjectPo::getDelFlag,0);
        Integer integer2 = projectExtMapper.selectCount(projectWrapper);
        if(integer2>0){
            return R.ok(false);
        }*/

        return R.ok(true);
    }

    @Override
    public R<Boolean> getSpecialGroup(Long specialId) {

        LambdaQueryWrapper<Group> groupWrapper = new LambdaQueryWrapper<>();
/*        groupWrapper.eq(Group::getSpecialId, specialId);
        groupWrapper.eq(Group::getDelFlag, 0);*/
        Integer integer1 = groupMapper.selectCount(groupWrapper);
        if (integer1 <= 0) {
            return R.ok(false);
        }
        return R.ok(true);
    }

    /**
     * @param specialId
     * @return 是否已经点击自动创建
     */
    @Override
    public R<CreateStatusOut> getCreateSt(Long specialId) {
        log.info("是否已经点击自动创建接口开始：");
        CreateStatusOut ret = projectExtMapper.selectSpecial(specialId);
        return R.ok(ret);

    }

    /**
     * 根据用户id查询项目列表（包含下级分组）
     *
     * @param userId
     * @return
     */
    @Override
    public R queryProjectWithGroupByUserId(Long userId) {
        Map<Long, ProjectWithGroupsVO> resultProjectMap = new HashMap<>(16);
        //查询专题
        Map<String, Object> querySpecialParams = ImmutableMap.of("delFlag", "0", "userId", userId);
        Object o = querySpecialParams.get("delFlag");
        List<Special> specialList = specialMapper.selectByUserId(querySpecialParams);
        //构建项目查询条件
        List<Long> specialIds = new ArrayList<>();
        if (!specialList.isEmpty()) {
            specialList.forEach(s -> {
                specialIds.add(s.getSpecialId());
            });
            //查询项目
            Wrapper wrapper = Wrappers.query().eq("del_flag", 0).in("special_id", specialIds);
            List<ProjectPo> projectList = projectExtMapper.selectList(wrapper);

            //构建分组查询条件
            List<Long> projectIds = new ArrayList<>();
            if (!projectList.isEmpty()) {
                projectList.forEach(p -> {
                    //构建前端所需项目对象
                    ProjectWithGroupsVO vo = new ProjectWithGroupsVO();
                    BeanUtils.copyBeanProp(vo, p);
                    resultProjectMap.put(p.getProjectId(), vo);
                    projectIds.add(p.getProjectId());
                });
                //查询分组
                List<ProjectGroup> projectGroupList = projectGroupMapper.selectProjectGroupByProjectId(projectIds, "", null);
                if (!projectGroupList.isEmpty()) {
                    projectGroupList.forEach(g -> {
                        ProjectWithGroupsVO p = resultProjectMap.get(g.getProjectId());
                        if (p.getChildren() == null) {
                            List<ProjectGroup> children = new ArrayList<>();
                            children.add(g);
                            p.setChildren(children);
                        } else {
                            p.getChildren().add(g);
                        }
                    });
                }
            }
        }
        return R.ok(resultProjectMap.values());
    }

    /**
     * 根据用户id查询项目列表
     *
     * @param userId
     * @param projectName
     * @return
     */
    @Override
    public R<List<ProjectPo>> queryProjectByUserId(Long userId, String projectName, Long specialId) {
        //查询专题
        Map<String, Object> querySpecialParams = ImmutableMap.of("delFlag", "0", "userId", userId);
        Object o = querySpecialParams.get("delFlag");
        //构建项目查询条件
        List<Long> specialIds = new ArrayList<>();
        if (specialId == null) {
            List<Special> specialList = specialMapper.selectByUserId(querySpecialParams);
            if (!specialList.isEmpty()) {
                specialList.forEach(s -> {
                    specialIds.add(s.getSpecialId());
                });
            }
        } else {
            specialIds.add(specialId);
        }
        //查询项目
        QueryWrapper wrapper = Wrappers.query().eq("del_flag", 0).in("special_id", specialIds);
        if (projectName != null && !"".equals(projectName)) {
            wrapper.like("project_name", projectName);
        }
        List<ProjectPo> projectList = projectExtMapper.selectList(wrapper);
        return R.ok(projectList);
    }

    /**
     * 根据项目id查分组
     *
     * @param projectId
     * @param groupName
     * @return
     */
    @Override
    public R<List<ProjectGroup>> queryGroupByProjectId(Long projectId, String groupName, Long reasons) {
        //构建分组查询条件
        List<Long> projectIds = new ArrayList<>();
        List<ProjectGroup> projectGroupList = new ArrayList<>();
        //项目id为null则查询参与的所有专题下分组
        if (projectId == null) {
            Map<String, Object> querySpecialParams = ImmutableMap.of("delFlag", "0", "userId", SecurityUtils.getUserId());
            Object o = querySpecialParams.get("delFlag");
            List<Special> specialList = specialMapper.selectByUserId(querySpecialParams);
            //构建项目查询条件
            List<Long> specialIds = new ArrayList<>();
            if (!specialList.isEmpty()) {
                specialList.forEach(s -> {
                    specialIds.add(s.getSpecialId());
                });
                //查询项目
                Wrapper wrapper = Wrappers.query().eq("del_flag", 0).in("special_id", specialIds);
                List<ProjectPo> projectList = projectExtMapper.selectList(wrapper);
                //构建分组查询条件
                if (!projectList.isEmpty()) {
                    projectList.forEach(p -> {
                        //构建前端所需项目对象
                        ProjectWithGroupsVO vo = new ProjectWithGroupsVO();
                        BeanUtils.copyBeanProp(vo, p);
                        projectIds.add(p.getProjectId());
                    });
                    //查询分组
                    projectGroupList = projectGroupMapper.selectProjectGroupByProjectId(projectIds, groupName, reasons);

                }
            }
        } else {
            projectIds.add(projectId);
            //查询分组
            projectGroupList = projectGroupMapper.selectProjectGroupByProjectId(projectIds, groupName, reasons);
        }
        return R.ok(projectGroupList);
    }

    /**
     * 编辑校验
     *
     * @param req
     * @param flag 1-项目名称校验否则脏器校验
     * @return
     */
    private boolean checkProject(OperateProjectIn req, String flag) {
        ProjectExt projectExt = new ProjectExt();
        projectExt.setSpecialId(req.getSpecialId());
        if (!ObjectUtils.isEmpty(req.getProjectId())) {
            projectExt.setProjectId(req.getProjectId());
        }
        if (CHECK_FLAG.equals(flag)) {
            projectExt.setProjectName(req.getProjectName());
        } else {
            projectExt.setViscusCode(req.getViscusCode());
        }
        List<ProjectExt> projectExts = projectExtMapper.selectByProject(projectExt);
        if (CollectionUtils.isEmpty(projectExts)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean saveBatch(Collection<Project> entityList) {
        return super.saveBatch(entityList);
    }

}
