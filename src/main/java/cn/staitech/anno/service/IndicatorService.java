package cn.staitech.anno.service;

import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.vo.indicator.IndicatorGetVO;
import cn.staitech.anno.domain.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.domain.vo.statistic.StatisticIndicatorListInVO;
import cn.staitech.anno.domain.vo.statistic.StatisticIndicatorListOutVO;

import java.util.List;

public interface IndicatorService {

    /**
     * 添加病例指标
     *
     * @param indicator 添加的字段信息
     * @return 结果
     */
    public int insertIndicator(Indicator indicator);

    /**
     * 展示病例指标
     *
     * @param indicator 查询的条件
     * @return 结果
     */
    public List<Indicator> selectIndicatorList(Indicator indicator);

    /**
     * 展示病例指标详情
     *
     * @param indicatorId 病例指标id
     * @return 结果
     */
    public Indicator selectIndicatorsById(Long indicatorId);

    /**
     * 修改
     *
     * @param indicator 病例指标id
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
     * 展示指定的统计病例指标列表
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

//    /**
//     * 更新病理表数据
//     *
//     * @param project, project1
//     * @return 结果
//     */
//    public int updateIndicatorMessage(PorjectVO project,ProjectListVO project1);

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
     * 查询指标列表
     *
     * @param
     * @return 结果
     */
    public List<Indicator> selectIndicatorInformation(Indicator indicator);

}
