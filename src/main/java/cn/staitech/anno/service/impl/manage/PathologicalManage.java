package cn.staitech.anno.service.impl.manage;

import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.vo.ProjectListVO;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.service.SlideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ROI 无属性标注
 */
@Slf4j
@Service
public class PathologicalManage {
    @Resource
    private SlideService slideService;
    @Resource
    private ProjectService projectService;

    @Resource
    private PathologicalIndicatorCategoryService pathologicalIndicatorCategoryService;

    /**
     * 根据slideId查询所有标注类别
     * @param slideId 标注ID
     * @return 标注类别列表
     */
    public List<PathologicalIndicatorCategory> list(Long slideId)  {
        //查询切片是否存在
        Slide slide = slideService.selectById(slideId);

        if (slide == null) {
            return null;
        }

        //查询projectId
        Long projectId = slide.getProjectId();
        //查询项目信息
        ProjectListVO project = projectService.selectProjectById(projectId);
        //查询标注类别
        List<PathologicalIndicatorCategory> list = pathologicalIndicatorCategoryService.selectIndicatorIdAll(project.getIndicatorId());

        return list;
    }
    
}





