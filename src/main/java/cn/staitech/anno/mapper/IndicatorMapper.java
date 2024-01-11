package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.vo.indicator.IndicatorGetVO;
import cn.staitech.anno.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.vo.project.InsertProjectVO;
import cn.staitech.anno.vo.statistic.StatisticIndicatorListInVO;
import cn.staitech.anno.vo.statistic.StatisticIndicatorListOutVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicatorMapper {


    Long addIndicatorsName(InsertProjectVO req);

    /**
     * 查询单个指标信息
     *
     * @param indicatorId 指标ID
     * @return 指标信息
     */
    Indicator selectIndicatorById(Long indicatorId);


    /**
     * 查询指标列表
     *
     * @param indicator 指标信息
     * @return 公告集合
     */
    List<Indicator> selectIndicatorList(Indicator indicator);


    /**
     * 新增指标
     *
     * @param indicator 指标信息
     * @return 结果
     */
    int insertIndicator(Indicator indicator);

    /**
     * 修改
     *
     * @param indicator 指标信息
     * @return 结果
     */
    int updateIndicator(IndicatorReviseVO indicator);

    /**
     * 删除
     *
     * @param indicatorId 指标id
     * @return 结果
     */
    int delIndicator(Long indicatorId);

    /**
     * 查询指标列表
     *
     * @param projectIdList 项目ID数组
     * @return 结果
     */
    List<StatisticIndicatorListOutVO> selectIndicatorStatisticList(StatisticIndicatorListInVO projectIdList);

    /**
     * 查询指标列表
     *
     * @param indicator
     * @return 结果
     */
    List<Indicator> selectIndicator(Indicator indicator);

    /**
     * 根据病理id和名字查询信息
     *
     * @param indicator
     * @return 结果
     */
    List<Indicator> selectIndicatorName(IndicatorGetVO indicator);

    /**
     * 查询所有的病理数量
     */
    Integer selectIndicatorNum();

    // 2.0 新修改====================================

    /**
     * 查询指标在项目表中的记录数量
     */
    Integer selectIndicatorCountInProject(Long indicatorId);

    Integer selectIndicatorCountByIndicator(Indicator indicator);


    /**
     * 查询指标列表 不分页
     *
     * @param
     * @return 结果
     */

    List<Indicator> selectIndicatorInformation(Indicator indicator);
}
