package cn.staitech.anno.service;

import cn.staitech.anno.domain.Project;
import cn.staitech.anno.vo.project.in.ProjectListQueryIn;
import cn.staitech.anno.vo.project.out.ProjectInfoOut;
import cn.staitech.anno.vo.project.out.ProjectListQueryOut;
import cn.staitech.common.core.domain.PageResponse;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProjectExtService extends IService<Project> {

    /**
     * 项目列表查询
     *
     * @param req
     * @return
     */
    PageResponse<ProjectListQueryOut> getProjectList(ProjectListQueryIn req);

    /**
     * 获得项目详情
     *
     * @param projectId
     * @return
     */
    ProjectInfoOut getProjectById(Long projectId);
}
