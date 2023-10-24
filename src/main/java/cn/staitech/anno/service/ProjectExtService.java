package cn.staitech.anno.service;

import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.po.ProjectPo;
import cn.staitech.anno.domain.project.in.OperateProjectIn;
import cn.staitech.anno.domain.project.in.ProjectListQueryIn;
import cn.staitech.anno.domain.project.in.ProjectRemoveIn;
import cn.staitech.anno.domain.project.out.*;
import cn.staitech.anno.domain.projectgroup.ProjectGroup;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProjectExtService extends IService<Project> {
    /**
     * 获得系统脏器下拉框
     *
     * @return
     */
    List<SystemDictOut> getSystemDictOld();

    /**
     * 获得系统脏器下拉框
     *
     * @return
     */
    List<SystemDictOut> getSystemDict(Long dictType);


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
     * 项目删除
     *
     * @param req
     * @return
     */
    R projectRemove(ProjectRemoveIn req);

    /**
     * 项目导航栏
     *
     * @param req
     * @return
     */
    NavigationBarQueryOut getNavigationBar(Long req);

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

    /**
     * @param projectId
     * @return 组间报告
     */
    R<InterGroupReportOut> getInterGroupReport(Long projectId);

    /**
     * @param specialId
     * @return 是否存在一键创建权限
     */
    R<Boolean> getCreateStatus(Long specialId);

    /**
     * @param specialId
     * @return 是否存在分组
     */
    R<Boolean> getSpecialGroup(Long specialId);

    /**
     * @param specialId
     * @return 是否存在一键创建权限
     */
    R<CreateStatusOut> getCreateSt(Long specialId);

    /**
     * @param specialId
     * @return 一键创建项目
     */
    R autoCreateProject(Long specialId);

    /**
     * 修改专题
     *
     * @param specialId
     */
    void changeSpecial(Long specialId);


}
