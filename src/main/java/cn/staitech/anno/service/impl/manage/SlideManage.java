package cn.staitech.anno.service.impl.manage;

import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.CacheUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class SlideManage {
    @Resource
    private ProjectService projectService;

    @Resource
    private SlideService slideService;

    /**
     * 项目批量添加图片
     */
    public int insertProjectImage(List<Slide> slideList, Long projectId) {
        slideService.insertSlide(slideList);
        Project project = new Project();
        project.setUpdateBy(SecurityUtils.getUserId());
        project.setProjectId(projectId);
        //更新项目表数据（更新项目表图片数量）
        projectService.updateProject(project);
        //重新缓存项目数据
        CacheUtils.ProjectCache(new Project());
        return 1;
    }
}
