package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.vo.*;
import cn.staitech.anno.domain.vo.statistic.StatisticIndicatorListInVO;
import cn.staitech.anno.domain.vo.statistic.StatisticIndicatorListOutVO;
import cn.staitech.anno.mapper.IndicatorMapper;
import cn.staitech.anno.service.IndicatorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorServicelmpl implements IndicatorService {
    
    @Resource
    private IndicatorMapper indicatorMapper;

    
    
    /**
     * 添加病例指标
     *
     * @param indicator 添加的字段信息
     * @return 结果
     */
    @Override
    public int insertIndicator(Indicator indicator) {
        return indicatorMapper.insertIndicator(indicator);
    }
    
    /**
     * 展示病例指标
     *
     * @param indicator 查询的条件
     * @return 结果
     */
    @Override
    public List<Indicator> selectIndicatorList(Indicator indicator) {
        return indicatorMapper.selectIndicatorList(indicator);
    }
    
    /**
     * 展示病例指标详情
     *
     * @param indicatorId 病例指标id
     * @return 结果
     */
    @Override
    public Indicator selectIndicatorsById(Long indicatorId) {
        return indicatorMapper.selectIndicatorById(indicatorId);}
    
    /**
     * 修改
     *
     * @param indicator 病例指标id
     * @return 结果
     */
    @Override
    public int updateIndicator(IndicatorReviseVO indicator) {
        return indicatorMapper.updateIndicator(indicator);
    }
    
    /**
     * 删除
     *
     * @param indicatorId 指标id
     * @return 结果
     */
    @Override
    public int delIndicator(Long indicatorId) {
        return indicatorMapper.delIndicator(indicatorId);
    }
    
    /**
     * 展示指定的统计病例指标列表
     *
     * @param projectIdList 项目ID数组
     * @return 结果
     */
    @Override
    public List<StatisticIndicatorListOutVO> selectIndicatorStatisticList(StatisticIndicatorListInVO projectIdList) {
        
        return indicatorMapper.selectIndicatorStatisticList(projectIdList);
    }
    
    
    /**
     * 查询指标列表
     *
     * @param
     * @return 结果
     */
    @Override
    public List<Indicator> selectIndicatorInformation() {
        return indicatorMapper.selectIndicatorInformation();
    }
    
    /**
     * 查询指标列表
     *
     * @param indicator
     * @return 结果
     */
    @Override
    public List<Indicator> selectIndicator(Indicator indicator) {
        return indicatorMapper.selectIndicator(indicator);
    }
    
    /**
     * 根据病理id和名字查询信息
     *
     * @param indicator
     * @return 结果
     */
    @Override
    public List<Indicator> selectIndicatorName(IndicatorGetVO indicator) {
        return indicatorMapper.selectIndicatorName(indicator);
    }

    /**
     * 查询所有的病理数量
     * */
    @Override
    public Integer selectIndicatorNum(){
        return indicatorMapper.selectIndicatorNum();
    }

    /**
     * 查询专题数量
     * */
    @Override
    public Integer selectSpecial(Long indicatorId){
        return indicatorMapper.selectSpecial(indicatorId);
    }

}
