package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.vo.project.InsertProjectVO;
import cn.staitech.anno.domain.vo.indicator.IndicatorGetVO;
import cn.staitech.anno.domain.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.domain.vo.statistic.StatisticIndicatorListInVO;
import cn.staitech.anno.domain.vo.statistic.StatisticIndicatorListOutVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicatorMapper {


    public Long addIndicatorsName(InsertProjectVO req);

    /**
     * 查询单个指标信息
     *
     * @param indicatorId 指标ID
     * @return 指标信息
     */
    public Indicator selectIndicatorById(Long indicatorId);


    /**
     * 查询指标列表
     *
     * @param indicator 指标信息
     * @return 公告集合
     */
    public List<Indicator> selectIndicatorList(Indicator indicator);


    /**
     * 新增指标
     *
     * @param indicator 指标信息
     * @return 结果
     */
    public int insertIndicator(Indicator indicator);

    /**
     * 修改
     *
     * @param indicator 指标信息
     * @return 结果
     */
    public int updateIndicator(IndicatorReviseVO indicator);

    /**
     * 删除
     *
     * @param indicatorId 指标id
     * @return 结果
     */
    public int delIndicator(Long indicatorId);

    /**
     * 查询指标列表
     *
     * @param projectIdList 项目ID数组
     * @return 结果
     */
    public List<StatisticIndicatorListOutVO> selectIndicatorStatisticList(StatisticIndicatorListInVO projectIdList);

    /**
     * 查询指标列表
     *
     * @param indicator
     * @return 结果
     */
    public List<Indicator> selectIndicator(Indicator indicator);

    /**
     * 根据病理id和名字查询信息
     *
     * @param indicator
     * @return 结果
     */
    public List<Indicator> selectIndicatorName(IndicatorGetVO indicator);

    /**
     * 查询所有的病理数量
     */
    public Integer selectIndicatorNum();

    /**
     * 查询专题数量
     */
    public Integer selectSpecial(Long indicatorId);

    // 2.0 新修改====================================

    /**
     * 查询指标列表 不分页
     *
     * @param
     * @return 结果
     */

    public List<Indicator> selectIndicatorInformation(Indicator indicator);
}
