package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Measure;
import cn.staitech.anno.vo.measurevo.MeasureJsonVO;
import cn.staitech.anno.vo.measurevo.MeasureSelectVO;

import java.util.List;

/**
 * 标注日志表 数据层 .
 *
 * @author staitech
 */
public interface MeasureMapper {

    /**
     * 新增测量
     *
     * @param measure 测量数据
     * @return 结果
     */
    Long insertMeasure(Measure measure);

    /**
     * 查询Measure中point(点)以外的具体数据
     *
     * @param measure 测量数据
     * @return 结果
     */
    List<MeasureSelectVO> selectMeasureBy(Measure measure);

    List<MeasureJsonVO> selectMeasureJson(Measure measure);


    /**
     * 查询Measure中point(点)数量
     *
     * @param measure 测量数据
     * @return 结果
     */
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