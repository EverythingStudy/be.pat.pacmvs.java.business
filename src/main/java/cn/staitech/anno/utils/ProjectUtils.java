package cn.staitech.anno.utils;

import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.service.*;
import cn.staitech.anno.vo.eyeslide.EyeProjectSlideIn;
import cn.staitech.anno.vo.eyeslide.EyeProjectSlideOut;
import cn.staitech.anno.vo.eyeslide.EyeSlideIn;
import cn.staitech.anno.vo.image.out.ImageListOutVO;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectLabelIn;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectLabelOut;
import cn.staitech.anno.vo.project.ProjectDelVO;
import cn.staitech.anno.vo.project.ProjectStatusVO;
import cn.staitech.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目工具类
 */
@Component
public class ProjectUtils {

    public static ProjectUtils projectUtils;
    @Resource
    private IndicatorService indicatorService;
    @Resource
    private ProjectService projectService;
    @Resource
    private SlideService slideService;
    @Resource
    private SlideAnnotationResultService slideAnnotationResultService;
    @Resource
    private PathologicalIndicatorCategoryService pathologicalIndicatorCategoryService;

    /**
     * 分页
     *
     * @param projectInforImageVO
     * @return
     */
    public static ProjectDelVO paging(EyeProjectSlideIn projectInforImageVO) {
        int pageNum = projectInforImageVO.getPageNum();
        int pageSize = projectInforImageVO.getPageSize();
        boolean flag = false;
        if (pageNum > 0) {
            pageNum--;
            flag = true;
        }
        List<EyeProjectSlideOut> result = new ArrayList<>();
        ProjectDelVO projectDelVO = new ProjectDelVO();
        projectDelVO.setPageNum(pageNum);
        projectDelVO.setPageSize(pageSize);
        projectDelVO.setFlag(flag);
        projectDelVO.setResult(result);
        return projectDelVO;
    }

    /**
     * 更新项目中的更新时间
     *
     * @param projectId
     */
    public static void updateProjectStatus(Long projectId) {
        ProjectStatusVO projectStatusVO = new ProjectStatusVO();
        projectStatusVO.setProjectId(projectId);
        projectStatusVO.setUpdateBy(SecurityUtils.getUserId());
        projectUtils.projectService.updateProjectStatus(projectStatusVO);
    }

    /**
     * 更新切片时间
     *
     * @param slideId
     */
    public static void updateSlideTime(Long slideId) {
        Slide slide = new Slide();
        slide.setSlideId(slideId);
        slide.setUpdateBy(SecurityUtils.getUserId());
        projectUtils.slideService.updateSlideTime(slide);
    }

    /**
     * 眼科--分页
     */
    public static ProjectDelVO pagingEye(EyeSlideIn projectInforImageVO) {
        int pageNum = projectInforImageVO.getPageNum();
        int pageSize = projectInforImageVO.getPageSize();
        boolean flag = false;
        if (pageNum > 0) {
            pageNum--;
            flag = true;
        }
        List<ImageListOutVO> result = new ArrayList<>();
        ProjectDelVO projectDelVO = new ProjectDelVO();
        projectDelVO.setPageNum(pageNum);
        projectDelVO.setPageSize(pageSize);
        projectDelVO.setFlag(flag);
        projectDelVO.setResults(result);
        return projectDelVO;
    }



    /**
     * 标签统计--分页
     */
    public static ProjectDelVO pagingLabel(ProjectLabelIn projectLabelIn) {
        int pageNum = projectLabelIn.getPageNum();
        int pageSize = projectLabelIn.getPageSize();
        boolean flag = false;
        if (pageNum > 0) {
            pageNum--;
            flag = true;
        }
        List<ProjectLabelOut> result = new ArrayList<>();
        ProjectDelVO projectDelVO = new ProjectDelVO();
        projectDelVO.setPageNum(pageNum);
        projectDelVO.setPageSize(pageSize);
        projectDelVO.setFlag(flag);
        projectDelVO.setResultList(result);
        return projectDelVO;
    }

    @PostConstruct
    public void init() {
        projectUtils = this;
        projectUtils.indicatorService = this.indicatorService;
        projectUtils.projectService = this.projectService;
        projectUtils.slideService = this.slideService;
        projectUtils.slideAnnotationResultService = this.slideAnnotationResultService;
        projectUtils.pathologicalIndicatorCategoryService = this.pathologicalIndicatorCategoryService;
    }
}
