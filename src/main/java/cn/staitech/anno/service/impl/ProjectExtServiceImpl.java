package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Project;
import cn.staitech.anno.mapper.ProjectExtMapper;
import cn.staitech.anno.mapper.ProjectMapper;
import cn.staitech.anno.service.ProjectExtService;
import cn.staitech.anno.vo.project.ProjectExt;
import cn.staitech.anno.vo.project.in.ProjectListQueryIn;
import cn.staitech.anno.vo.project.out.ProjectInfoOut;
import cn.staitech.anno.vo.project.out.ProjectListQueryOut;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.utils.bean.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: wudi
 * @Date: 2023/5/29 18:05
 * @desc: 项目模块业务层
 */
@Service
public class ProjectExtServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectExtService {

    private static final Logger log = LoggerFactory.getLogger(ProjectExtServiceImpl.class);

    @Resource
    private ProjectExtMapper projectExtMapper;

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


    @Override
    public boolean saveBatch(Collection<Project> entityList) {
        return super.saveBatch(entityList);
    }

}
