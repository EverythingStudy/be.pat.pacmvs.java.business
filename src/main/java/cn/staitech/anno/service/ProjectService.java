package cn.staitech.anno.service;

import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.SlideAnnotationResult;
import cn.staitech.anno.domain.image.in.ImageAllVO;
import cn.staitech.anno.domain.vo.*;
import cn.staitech.anno.domain.vo.statistic.StatisticProjectListOutVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 项目 服务层
 *
 * @author staitech
 */
public interface ProjectService extends IService<Project> {

    /**
     * 根据主键查询项目详情
     *
     * @param projectId 项目ID
     * @return 项目信息
     */
    Project selectPrimKey(Long projectId);

    /**
     * 更新标注描述
     *
     * @param project 项目ID
     * @return true | false
     */
    int updateProjectDescription(Project project);

    /**
     * 查询项目信息
     *
     * @param projectId 项目ID
     * @return 项目信息
     */
    public ProjectListVO selectProjectById(Long projectId);

    /**
     * 查询项目列表
     *
     * @param project 项目信息
     * @return 项目集合
     */
    public List<ProjectListVO> selectProjectList(Project project);

    /**
     * 新增项目
     *
     * @param project 项目信息
     * @return 结果
     */
    public int insertProject(Project project);

    /**
     * 修改项目
     *
     * @param project 项目信息
     * @return 结果
     */
    public int updateProject(Project project);

    /**
     * 删除项目信息
     *
     * @param projectId 项目ID
     * @return 结果
     */
    public int deleteProjectById(Long projectId);

    /**
     * 查询指标项目名称和id
     *
     * @param indicatorId 指标ID
     * @return 结果
     */

    public List<Project> selectProjectInfo(Long indicatorId);

    Project selectProjectByName(String projectName);

    /**
     * 查询项目统计列表
     *
     * @param project 项目统计信息
     * @return 项目统计集合
     */

    public List<StatisticProjectListOutVO> selectProjectStatisticList(Project project);

    /**
     * 根据id和项目名称称查询信息
     *
     * @param project
     * @return
     */
    public List<Project> selectProjectName(ProjectGetVO project);

    /**
     * 根据切片和状态获取状态信息
     *
     * @param slideCategoryProcessFlag
     * @return
     */
    public SlideAnnotationResult selectUserMessage(SlideCategoryProcessFlagVO slideCategoryProcessFlag);

    /**
     * 根据切片id信息
     *
     * @param slideId
     * @return
     */
    public List<SlideAnnotationResult> selectMessageBySlideId(Long slideId);

    /**
     * 根据切片id信息
     *
     * @param slideCategoryProcessFlag
     * @return
     */
    public SlideAnnotationResult selectByCategoryId(SlideCategoryProcessFlagVO slideCategoryProcessFlag);

    /**
     * 获取图像列表
     *
     * @param image
     * @return
     */
    public List<ImageMessageVO> getImageList(ImageAllVO image);

    /**
     * 获取图像列表
     *
     * @param projectId
     * @return
     */
    public int updateCategory(Long projectId);

    /**
     * 根据slideId删除关系表中的信息
     *
     * @param slideId
     * @return
     */
    public int deleteBySlideId(Long slideId);

    /**
     * 根据条件查询数量
     *
     * @param annotation
     * @return
     */
    public AnnotationsAddVO selectSumByProjectId(AnnotationsAddVO annotation);

    /**
     * 查询标注数量和
     *
     * @param slideCategoryProcessFlagVO
     * @return
     */
    public Integer selectCategorySum(SlideCategoryProcessFlagVO slideCategoryProcessFlagVO);

    /**
     * 更新人工标注数
     *
     * @param project
     * @return
     */
    int updateProjectHumanAnnotationQuantity(ProjectAnnotationVO project);

    /**
     * 更新项目
     */
    public int updateProjectIndicator(Project project);

    /**
     * 根据projectId，状态，imageName 查询
     */
    public List<ProjectListVO> selectProjectDetails(ProjectInforImageVO projectInforImageVO);

    /**
     * 查询切片中的除了unLabel，的标注总数
     */
    public Integer selectCategoryTotal(SlideCategoryProcessFlagVO slideCategoryProcessFlagVO);

    /**
     * 更新项目状态
     */
    public Integer updateProjectStatus(ProjectStatusVO projectStatusVO);

    /**
     * 查询切片的标注总数
     */
    public Integer selectAnnotationNumber(Long slideId);

    /**
     * 查询项目创建者
     */
    public List<Project> selectProjectCreateBy();

    /**
     * 查询所有未添加项目角色的项目ID
     *
     * @return 项目ID列表
     */
    public List<ProjectIdListVO> selectProjectIdNotInProjectRole();

    /**
     * 根据projectId 和 createBy查询slideId
     */
    public List<AnnotationsAddVO> selectSlideId(ProjectInforImageVO projectInforImageVO);

    /**
     * 根据projectId查询 用户
     */
    public List<ProjectListVO> selectProjectTagger(Long projectId);

}
