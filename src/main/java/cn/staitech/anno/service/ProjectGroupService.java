package cn.staitech.anno.service;

import cn.staitech.anno.domain.projectgroup.in.RemoveProjectGroupIn;
import cn.staitech.anno.domain.projectgroup.out.ProjectGroupListOut;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;

/**
 * @Author wudi
 * @Date 2023/5/30 16:44
 * @desc 项目分组模块
 */
public interface ProjectGroupService {
    /**
     * @param projectId
     * @return 项目分组列表
     */
    PageResponse<ProjectGroupListOut> projectGroupList(Long projectId, int reasons, int pageNum, int pageSize);

    /**
     * @param req
     * @return 项目分组列表删除
     */
    R removeProjectGroup(RemoveProjectGroupIn req);

    /**
     * 清除项目分组切片
     *
     * @param req
     * @return
     */
    R cleanProjectGroup(RemoveProjectGroupIn req);

}
