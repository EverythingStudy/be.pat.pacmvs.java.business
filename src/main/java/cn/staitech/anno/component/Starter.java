package cn.staitech.anno.component;

import cn.staitech.anno.domain.vo.ProjectIdListVO;
import cn.staitech.anno.service.ProjectRoleService;
import cn.staitech.anno.service.ProjectService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 项目重新部署，启动时只执行一次
 * @author wangfeng
 */
@Component
public class Starter {

    /*@Resource
    ProjectService projectService;*/

    @Resource
    ProjectRoleService projectRoleService;

    @PostConstruct
    public void init(){
        addProjectRoles();
    }

    /**
     * 为未添加项目角色的历史数据填写默认的3个角色
     */
    public void addProjectRoles(){
       /* // 查询所有未添加项目角色的项目ID
        List<ProjectIdListVO> projectIdList = projectService.selectProjectIdNotInProjectRole();
        for (ProjectIdListVO projectId : projectIdList) {
            // 添加项目默认3个角色，createBy = 1, admin
            projectRoleService.addProjectRoles(projectId.getProjectId(),1L);
        }*/
    }
}

