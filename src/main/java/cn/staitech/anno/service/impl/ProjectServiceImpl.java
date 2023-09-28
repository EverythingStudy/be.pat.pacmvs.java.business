package cn.staitech.anno.service.impl;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.constant.ColorConstant;
import cn.staitech.anno.constant.ProjectConstant;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.SlideAnnotationResult;
import cn.staitech.anno.domain.image.in.ImageAllVO;
import cn.staitech.anno.domain.vo.*;
import cn.staitech.anno.domain.vo.statistic.StatisticProjectListOutVO;
import cn.staitech.anno.mapper.ProjectMapper;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.service.ProjectTypeService;
import cn.staitech.anno.service.SpeciesService;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.staitech.common.security.utils.SecurityUtils.isAdmin;

/**
 * 项目 服务层实现
 *
 * @author staitech
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private ProjectTypeService projectTypeService;
    @Resource
    private SpeciesService speciesService;

/*    @Resource
    private ProductSeriesService productSeriesService;*/

    /**
     * 根据主键查询项目详情
     *
     * @param projectId 项目ID
     * @return 项目信息
     */
    @Override
    public Project selectPrimKey(Long projectId) {
        return projectMapper.selectPrimKey(projectId);
    }

    @Override
    public int updateProjectDescription(Project project) {
        return projectMapper.updateProjectDescription(project);
    }

    /**
     * 查询项目信息
     *
     * @param projectId 项目ID
     * @return 项目信息
     */
    @Override
    public ProjectListVO selectProjectById(Long projectId) {
        ProjectListVO project = projectMapper.selectProjectById(projectId);
        // 项目类型
        Map<String, String> projectTypeMap = projectTypeService.selectMap();
        // 种属
        Map<Long, String> sepeciesMap = speciesService.selectMap();

        // 项目类型
        if (projectTypeMap.containsKey(project.getProjectType())) {
            project.setProjectTypeName(projectTypeMap.get(project.getProjectType()));
        }
        // 种属
        if (sepeciesMap.containsKey(project.getSpeciesId())) {
            project.setSpeciesName(sepeciesMap.get(project.getSpeciesId()));
        }

        project.setColorTypeName(ColorConstant.COLOR_TYPE.get(project.getColorType()));
        project.setStatusName(ProjectConstant.PROJECT_STATUS.get(project.getStatus()));
        return project;
    }

    /**
     * 查询项目列表
     *
     * @param project 项目信息
     * @return 项目集合
     */
    @Override
    public List<ProjectListVO> selectProjectList(Project project) {
        // 项目列表
        List<ProjectListVO> projectList = projectMapper.selectProjectList(project);

        // 项目类型
        Map<String, String> projectTypeMap = projectTypeService.selectMap();

        // 品系
        // Map<Integer, String> productSeriesMap = productSeriesService.selectMap();

        for (ProjectListVO obj : projectList) {
            // 项目类型
            if (projectTypeMap.containsKey(obj.getProjectType())) {
                obj.setProjectTypeName(projectTypeMap.get(obj.getProjectType()));
            }
            // 种属
            obj.setSpeciesName(MapConstant.getSpeciesName(obj.getSpeciesId()));

            // 品系
/*            if (productSeriesMap.containsKey(obj.getProductSeriesId())) {
                obj.setProductSeries(productSeriesMap.get(obj.getProductSeriesId()));
            }*/

            obj.setColorTypeName(ColorConstant.COLOR_TYPE.get(obj.getColorType()));
            obj.setStatusName(ProjectConstant.PROJECT_STATUS.get(obj.getStatus()));
        }
        return projectList;
    }

    /**
     * 新增项目
     *
     * @param project 项目信息
     * @return 结果
     */
    @Override
    public int insertProject(Project project) {
        return projectMapper.insertProject(project);
    }

    /**
     * 修改项目
     *
     * @param project 项目信息
     * @return 结果
     */
    @Override
    public int updateProject(Project project) {
        return projectMapper.updateProject(project);
    }

    /**
     * 删除项目对象
     *
     * @param projectId 项目ID
     * @return 结果
     */
    @Override
    public int deleteProjectById(Long projectId) {
        return projectMapper.deleteProjectById(projectId);
    }

    /**
     * 查询指标项目名称和id
     *
     * @param IndicatorId 指标ID
     * @return 结果
     */

    @Override
    public List<Project> selectProjectInfo(Long IndicatorId) {
        return projectMapper.selectProjectInfo(IndicatorId);
    }

    @Override
    public Project selectProjectByName(String projectName) {
        return projectMapper.selectProjectByName(projectName);
    }

    /**
     * 查询项目统计列表
     *
     * @param project 项目统计信息
     * @return 项目统计集合
     */
    @Override
    public List<StatisticProjectListOutVO> selectProjectStatisticList(Project project) {
        if (!isAdmin(SecurityUtils.getUserId())) {
            project.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        }
        return projectMapper.selectProjectStatisticList(project);
    }

    /*public List<StatisticProjectListOutVO> selectProjectStatisticList(Project project) {
        if (isAdmin(SecurityUtils.getUserId())) {
            return projectMapper.selectProjectStatisticList(project);
        } else if (SecurityUtils.getLoginUser().getRoles().contains("system")) {
            project.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
            return projectMapper.selectProjectStatisticList(project);
        } else {
            return projectMapper.selectProjectStatisticListExt(SecurityUtils.getUserId());
        }
    }*/

    /**
     * 根据id和项目名称查询信息
     *
     * @param project
     * @return
     */
    @Override
    public List<Project> selectProjectName(ProjectGetVO project) {
        return projectMapper.selectProjectName(project);
    }

    /**
     * 根据切片和状态获取状态信息
     *
     * @param slideCategoryProcessFlag
     * @return
     */
    @Override
    public SlideAnnotationResult selectUserMessage(SlideCategoryProcessFlagVO slideCategoryProcessFlag) {
        return projectMapper.selectUserMessage(slideCategoryProcessFlag);
    }

    /**
     * 根据切片id信息
     *
     * @param slideId
     * @return
     */
    @Override
    public List<SlideAnnotationResult> selectMessageBySlideId(Long slideId) {
        return projectMapper.selectMessageBySlideId(slideId);
    }

    /**
     * 根据切片id信息
     *
     * @param slideCategoryProcessFlag
     * @return
     */
    @Override
    public SlideAnnotationResult selectByCategoryId(SlideCategoryProcessFlagVO slideCategoryProcessFlag) {
        return projectMapper.selectByCategoryId(slideCategoryProcessFlag);
    }

    /**
     * 获取图像列表
     *
     * @param
     * @return
     */
    @Override
    public List<ImageMessageVO> getImageList(ImageAllVO imageAll) {
        return projectMapper.getImageList(imageAll);
    }

    /**
     * 修改标注中的，categoryId
     *
     * @param projectId
     * @return
     */
    @Override
    public int updateCategory(Long projectId) {
        return projectMapper.updateCategory(projectId);
    }

    /**
     * 根据slideId删除关系表中的信息
     *
     * @param slideId
     * @return
     */
    @Override
    public int deleteBySlideId(Long slideId) {
        return projectMapper.deleteBySlideId(slideId);
    }

    /**
     * 根据条件查询数量
     *
     * @param annotation
     * @return
     */
    @Override
    public AnnotationsAddVO selectSumByProjectId(AnnotationsAddVO annotation) {
        return projectMapper.selectSumByProjectId(annotation);
    }

    /**
     * 查询标注数量和
     *
     * @param slideCategoryProcessFlagVO
     * @return
     */
    @Override
    public Integer selectCategorySum(SlideCategoryProcessFlagVO slideCategoryProcessFlagVO) {
        return projectMapper.selectCategorySum(slideCategoryProcessFlagVO);
    }

    /**
     * 更新人工标注数
     *
     * @param project
     * @return
     */
    @Override
    public int updateProjectHumanAnnotationQuantity(ProjectAnnotationVO project) {
        return projectMapper.updateProjectHumanAnnotationQuantity(project);
    }

    /**
     * 更新项目
     */
    @Override
    public int updateProjectIndicator(Project project) {
        return projectMapper.updateProjectIndicator(project);
    }

    /**
     * 根据projectId，状态，imageName 查询
     */
    @Override
    public List<ProjectListVO> selectProjectDetails(ProjectInforImageVO projectInforImageVO) {
        return projectMapper.selectProjectDetails(projectInforImageVO);
    }

    /**
     * 查询切片中的除了unLabel，的标注总数
     */
    @Override
    public Integer selectCategoryTotal(SlideCategoryProcessFlagVO slideCategoryProcessFlagVO) {
        return projectMapper.selectCategoryTotal(slideCategoryProcessFlagVO);
    }

    /**
     * 更新项目状态
     */
    @Override
    public Integer updateProjectStatus(ProjectStatusVO projectStatusVO) {
        return projectMapper.updateProjectStatus(projectStatusVO);
    }

    /**
     * 查询切片的标注总数
     */
    @Override
    public Integer selectAnnotationNumber(Long slideId) {
        return projectMapper.selectAnnotationNumber(slideId);
    }

    /**
     * 查询项目创建者
     */
    @Override
    public List<Project> selectProjectCreateBy() {
        return projectMapper.selectProjectCreateBy();
    }

    /**
     * 查询所有未添加项目角色的项目ID
     *
     * @return 项目ID列表
     */
    @Override
    public List<ProjectIdListVO> selectProjectIdNotInProjectRole() {
        return projectMapper.selectProjectIdNotInProjectRole();
    }

    /**
     * 根据projectId 和 createBy查询slideId
     */
    @Override
    public List<AnnotationsAddVO> selectSlideId(ProjectInforImageVO projectInforImageVO) {
        return projectMapper.selectSlideId(projectInforImageVO);
    }

    /**
     * 根据projectId查询 用户
     */
    @Override
    public List<ProjectListVO> selectProjectTagger(Long projectId) {
        return projectMapper.selectProjectTagger(projectId);
    }
}
