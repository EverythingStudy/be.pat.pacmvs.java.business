package cn.staitech.anno.service;

import java.util.List;

import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.indicator.IndicatorAddVO;
import cn.staitech.anno.vo.indicator.IndicatorGetVO;
import cn.staitech.anno.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.vo.indicator.IndicatorVO;
import cn.staitech.common.core.domain.R;

public interface IndicatorService {

    /**
     * 添加病例指标
     *
     * @param indicator 添加的字段信息
     * @return 结果
     */
	R<String> insertIndicator(IndicatorAddVO indicator);

    /**
     * 展示病例指标
     *
     * @param indicator 查询的条件
     * @return 结果
     */
    PageMaster<Indicator> selectIndicatorList(Indicator indicator, Integer pageNum, Integer pageSize);


    /**
     * 展示病例指标
     *
     * @param indicator 查询的条件
     * @return 结果
     */
    List<Indicator> selectIndicatorList1(Indicator indicator);

    /**
     * 展示病例指标详情
     *
     * @param indicatorId 病例指标id
     * @return 结果
     */
    Indicator selectIndicatorsById(Long indicatorId);

    /**
     * 修改
     *
     * @param indicator 病例指标id
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
     * @param indicator
     * @return 结果
     */
    List<Indicator> selectIndicator(Indicator indicator);
    
    R<IndicatorVO> getInfo(Long indicatorId);

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
     * 查询专题在项目表中的数量
     */
    Integer selectIndicatorCountInProject(Long indicatorId);


    Integer selectIndicatorCountByIndicator(Indicator indicator);

    /**
     * 查询指标列表
     *
     * @param
     * @return 结果
     */
    List<Indicator> selectIndicatorInformation(Indicator indicator);

    List<PathologicalIndicatorCategory> speciesCategory(String species);

    int saveCheck(IndicatorAddVO req,Long organizationId);
    
    R<Integer> edit(IndicatorReviseVO req);
    
    R<String> delIndicator(IndicatorGetVO req);

}
