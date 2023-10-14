package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.projectgroup.ProjectGroup;
import cn.staitech.anno.domain.projectgroup.in.RemoveProjectGroupIn;
import cn.staitech.anno.domain.projectgroup.out.ProjectGroupListOut;
import cn.staitech.anno.mapper.ProjectGroupMapper;
import cn.staitech.anno.service.ProjectGroupService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author wudi
 * @Date 2023/5/30 16:44
 * @desc 项目分组模块
 */
@Service
public class ProjectGroupServiceImpl implements ProjectGroupService {
    private static final Logger log = LoggerFactory.getLogger(ProjectExtServiceImpl.class);

    @Resource
    private ProjectGroupMapper projectGroupMapper;

    /**
     * @param projectId
     * @return 项目分组列表
     */
    @Override
    public PageResponse<ProjectGroupListOut> projectGroupList(Long projectId, int reasons, int pageNum, int pageSize) {
        log.info("项目分组列表查询接口开始：");
        //创建响应
        PageResponse resp = new PageResponse<>();
        resp.setPageNum(pageNum);
        resp.setPageSize(pageSize);

        Page<SysUser> page = PageHelper.startPage(pageNum, pageSize);
        List<ProjectGroup> projectGroups = projectGroupMapper.selectProjectGroupList(projectId, reasons);

        //处理响应
        if (!CollectionUtils.isEmpty(projectGroups)) {
            List<ProjectGroupListOut> content = projectGroups.stream().map(e -> {
                ProjectGroupListOut respDate = new ProjectGroupListOut();
                BeanUtils.copyBeanProp(respDate, e);
                return respDate;
            }).collect(Collectors.toList());
            resp.setList(content);
        }
        resp.setTotal(page.getTotal());
        resp.setPages(resp.getPages());
        return resp;

    }

    /**
     * @param req
     * @return
     * @desc 项目分组删除
     */
    @Override
    public R removeProjectGroup(RemoveProjectGroupIn req) {
        log.info("项目分组列表删除接口开始：");
        //校验是否存在切片
        int i = projectGroupMapper.selectCountSlide(req.getProjectId(), req.getGroupId());

        if (i > 0) {
            return R.fail(MessageSource.M("PROJECT_GROUP_SLIDE_EXIST"));
        }

        Long userId = SecurityUtils.getUserId();
        //Long userId = 123l;
        //修改为删除状态
        projectGroupMapper.updateState(req.getProjectGroupId(), userId);
        return R.ok();
    }

    /**
     * 清除项目分组切片
     *
     * @param req
     * @return
     */
    @Override
    public R cleanProjectGroup(RemoveProjectGroupIn req) {
        log.info("清除项目分组切片接口开始：");

        int i = projectGroupMapper.selectProcessSlide(req.getProjectId(), req.getGroupId());
        if (i > 0) {
            return R.fail(MessageSource.M("PROJECT_SLIDE_RUNNING"));
        }

        projectGroupMapper.updateStateSlide(req.getGroupId(), req.getProjectId());

        return R.ok();
    }
}
