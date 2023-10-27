package cn.staitech.anno.service;


import cn.staitech.anno.domain.Measure;
import cn.staitech.anno.domain.vo.measurevo.MeasureJsonVO;
import cn.staitech.anno.domain.vo.measurevo.MeasureSelectVO;

import java.util.List;

/**
 * 标注日志 服务层  .
 *
 * @author staitech
 */

public interface MeasureService {

    /**
     * 新增测量
     *
     * @param Measure 新增测量
     * @return 结果
     */
    Long insertMeasure(Measure measure);

    /**
     * 查询测量数据
     *
     * @param Measure 查询测量数据
     * @return 结果
     */
    List<MeasureSelectVO> selectMeasureBy(Measure measure);

    List<MeasureJsonVO> selectMeasureJson(Measure measure);


    int selectMeasureCount(Measure measure);

    /**
     * 删除测量
     *
     * @param measureId 测量id
     * @return 结果
     */
    int deleteMeasureById(Long measureId);

    /**
     * 更新测量
     *
     * @param measure 测量数据
     * @return 结果
     */
    int updateMeasure(Measure measure);

    /**
     * 主键查询详情信息
     *
     * @param measureId 测量数据
     * @return 结果
     */
    Measure selectMeasureById(Long measureId);

    /**
     * 删除用户在当前页面的测量信息
     *
     * @param measure 测量数据
     * @return boolean
     */
    int deleteMeasureBatch(Measure measure);


}