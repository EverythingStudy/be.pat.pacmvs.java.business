package cn.staitech.anno.service;

import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.vo.annotation.LabelListVO;
import cn.staitech.anno.vo.annotation.LabelVO;
import cn.staitech.anno.vo.pathologicalIndicatorCategory.PathologicalIndicatorCategoryOutVo;
import cn.staitech.anno.vo.statistic.StatisticCategoryListInVO;
import cn.staitech.anno.vo.statistic.StatisticCategoryListOutVO;

import java.text.ParseException;
import java.util.List;


public interface PathologicalIndicatorCategoryService {
    /**
     * 添加标签信息
     *
     * @param Pathological 标签ID
     * @return 标签信息
     */
    int insertSelective(PathologicalIndicatorCategory Pathological);

    /**
     * 修改标签信息
     *
     * @param Pathological 标签ID
     * @return 标签信息
     */
    String updateByPrimaryKeySelective(PathologicalIndicatorCategory Pathological);
    
    /**
     * 修改标签信息(结构、标注、考核同步)
     *
     * @param Pathological 标签ID
     * @return 标签信息
     */
    String updateByPrimaryKeySelective2(PathologicalIndicatorCategory Pathological);

    /**
     * 删除标签信息
     *
     * @param categoryId 标签ID
     * @return 标签信息
     */
    String deleteByPrimaryKey(Long categoryId);

    /**
     * 查询标签详细信息
     *
     * @param categoryId 标签ID
     * @return 标签信息
     */
    PathologicalIndicatorCategory selectByPrimaryKey(Long categoryId);

    /**
     * 根据病理指标id获取全部信息
     *
     * @param indicatorId 病理指标ID
     * @return 标签信息
     */
    List<PathologicalIndicatorCategory> selectIndicatorIdAll(Long indicatorId);

    /**
     * 根据病理指标id删除信息
     *
     * @param indicatorId 病理指标ID
     * @return 标签信息
     */
    int delIndicatorCategory(Long indicatorId);

    PathologicalIndicatorCategory selectCategoryAll(Long CategoryId);

    /**
     * 获取标注类别统计列表
     *
     * @param indicatorProjectIdList 病例指标id列表、项目id列表
     * @return 结果
     */
    List<StatisticCategoryListOutVO> selectAnnotationCategoryStatisticList(StatisticCategoryListInVO indicatorProjectIdList);

    /**
     * 条件查询标注类别
     *
     * @param Pathological 病例指标id 或 颜色 或 标注类别名称
     * @return 结果
     */
    List<PathologicalIndicatorCategory> selectIndicatorMessage(PathologicalIndicatorCategory Pathological);


    /**
     * 条件查询标注类别-用于修改
     *
     * @param Pathological 病例指标id 或 颜色 或 标注类别名称
     * @return 结果
     */
    List<PathologicalIndicatorCategory> selectIndicatorMessageForUpdate(PathologicalIndicatorCategory Pathological);

    /**
     * 通过项目id统计标注类别
     *
     * @param projectId
     * @return
     */
    List<StatisticCategoryListOutVO> selectByProjectId(Long projectId);

    /**
     * 通过项目ID查询标注类别
     *
     * @param projectId 项目ID
     * @return 标注类别列表
     */
    List<PathologicalIndicatorCategory> selectCategoryByProjectId(Long projectId);

    /**
     * 查询病例指标下的标注类别数量
     */
    Long selectCategoryNumber(Long indicatorId);

    /**
     * 根据projectId查询标注类别
     */
    List<PathologicalIndicatorCategory> selectProjectCategory(Long projectId);

    /**
     * 根据indicatorId查询标注类别（不包含unLabel）
     */
    List<LabelListVO> selectByIndicator(LabelVO labelVO);

    /**
     * 根据项目id查询标签列表
     *
     * @param projectId 项目id
     */
    List<PathologicalIndicatorCategoryOutVo> selectprojectList(Long projectId);

    List<PathologicalIndicatorCategoryOutVo> selectProjectListFilter(Long projectId);

    /**
     * 查询标签在标注中的使用数量
     */
    Integer selectLabelNum(Long categoryId);
    
    /**
     * 查询标签所属脏器系统内已有标签数量
     */
    Integer selectLabelNumByStructureId(String structureId);
    
    void handlerCouponsUserStatusTimeOutToExpired(List<Long> list) throws ParseException;
}
