package cn.staitech.anno.service;

import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.ProjectPo;
import cn.staitech.anno.vo.project.in.OperateProjectIn;
import cn.staitech.anno.vo.project.in.ProjectListQueryIn;
import cn.staitech.anno.vo.project.out.ProjectInfoOut;
import cn.staitech.anno.vo.project.out.ProjectListQueryOut;
import cn.staitech.anno.vo.projectgroup.ProjectGroup;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProjectExtService extends IService<Project> {

    /**
     * 项目列表查询
     *
     * @param req
     * @return
     */
    PageResponse<ProjectListQueryOut> getProjectList(ProjectListQueryIn req);

    /**
     * 项目编辑
     *
     * @param req
     * @return
     */
    R operateProject(OperateProjectIn req);

    /**
     * 获得项目详情
     *
     * @param projectId
     * @return
     */
    ProjectInfoOut getProjectById(Long projectId);

    /**
     * 根据用户id查询项目列表（包含下级分组）
     *
     * @return
     */
    R queryProjectWithGroupByUserId(Long userId);

    /**
     * 根据用户id查询项目列表
     *
     * @return
     */
    R<List<ProjectPo>> queryProjectByUserId(Long userId, String projectName, Long specialId);

    /**
     * 根据项目信息查分组
     *
     * @param projectId
     * @param groupName
     * @return
     */
    R<List<ProjectGroup>> queryGroupByProjectId(Long projectId, String groupName, Long reasons);

}
