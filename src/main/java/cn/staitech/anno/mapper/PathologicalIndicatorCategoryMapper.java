package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.vo.annotation.LabelListVO;
import cn.staitech.anno.vo.annotation.LabelVO;
import cn.staitech.anno.vo.geojson.GeoLabel;
import cn.staitech.anno.vo.statistic.StatisticCategoryListInVO;
import cn.staitech.anno.vo.statistic.StatisticCategoryListOutVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PathologicalIndicatorCategoryMapper extends BaseMapper<PathologicalIndicatorCategory> {

    /**
     * 根据主键删除信息
     *
     * @param categoryId
     * @return
     */
    int deleteByPrimaryKey(Long categoryId);

    /**
     * 添加标签信息
     *
     * @param record
     * @return
     */
    int insertSelective(PathologicalIndicatorCategory record);

    /**
     * 根据主键查询单挑标签信息
     *
     * @param categoryId
     * @return
     */
    PathologicalIndicatorCategory selectByPrimaryKey(Long categoryId);

    /**
     * 修改标签信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PathologicalIndicatorCategory record);

    /**
     * 根据病理指标id获取全部信息
     *
     * @param indicatorId
     * @return
     */
    List<PathologicalIndicatorCategory> selectIndicatorIdAll(Long indicatorId);

    /**
     * 根据病理指标id获取列表
     *
     * @param indicatorId
     * @return
     */
    List<GeoLabel> selectIndicatorIdList(Long indicatorId);

    GeoLabel selectGeoLabel(Long categoryId);

    /**
     * 根据病理指标id删除标注类别
     *
     * @param indicatorId
     * @return
     */
    int delIndicatorCategory(Long indicatorId);

    @SuppressWarnings("checkstyle:ParameterName")
    PathologicalIndicatorCategory selectCategoryAll(Long CategoryId);

    /**
     * 获取标注类别统计列表
     *
     * @param indicatorProjectIdList 病例指标id列表、项目id列表
     * @return 标注集合
     */
    List<StatisticCategoryListOutVO> selectAnnotationCategoryStatisticList(
            StatisticCategoryListInVO indicatorProjectIdList);

    /**
     * 条件查询标注类别
     *
     * @param Pathological 病例指标id 或 颜色 或 标注类别名称
     * @return 结果
     */
    List<PathologicalIndicatorCategory> selectIndicatorMessage(PathologicalIndicatorCategory Pathological);


    /**
     * 条件查询标注类别-用于修改，排除自己
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
     * 查询所有指标（除标注区域外）
     *
     * @param projectId
     * @return
     */
    List<PathologicalIndicatorCategory> selectProjectListFilter(Long indicatorId);

    /**
     * 查询标签在标注中的使用数量
     */
    Integer selectLabelNum(Long categoryId);

    /**
     * 根据项目和结构编码查询详情数据
     */
    PathologicalIndicatorCategory selectProjectAndNumber(@Param("projectId") Long projectId, @Param("number") String number);

    String selectCategoryById(@Param("split") String[] split);
}