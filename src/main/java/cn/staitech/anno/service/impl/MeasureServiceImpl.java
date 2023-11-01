package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Measure;
import cn.staitech.anno.mapper.MeasureMapper;
import cn.staitech.anno.service.MeasureService;
import cn.staitech.anno.vo.measurevo.MeasureJsonVO;
import cn.staitech.anno.vo.measurevo.MeasureSelectVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标注 服务层实现 .
 *
 * @author staitech
 */
@Service
public class MeasureServiceImpl implements MeasureService {


    @Resource
    private MeasureMapper measureMapper;


    @Override
    public Long insertMeasure(Measure measure) {
        return measureMapper.insertMeasure(measure);
    }

    @Override
    public List<MeasureSelectVO> selectMeasureBy(Measure Measure) {
        return measureMapper.selectMeasureBy(Measure);
    }

    @Override
    public List<MeasureJsonVO> selectMeasureJson(Measure Measure) {
        return measureMapper.selectMeasureJson(Measure);
    }

    @Override
    public int selectMeasureCount(Measure measure) {
        return measureMapper.selectMeasureCount(measure);
    }

    /**
     * 删除测量
     *
     * @param measureId 测量id
     * @return boolean
     */
    @Override
    public int deleteMeasureById(Long measureId) {
        return measureMapper.deleteMeasureById(measureId);
    }

    /**
     * 更新测量
     *
     * @param measure 测量数据
     * @return boolean
     */
    @Override
    public int updateMeasure(Measure measure) {
        return measureMapper.updateMeasure(measure);
    }

    /**
     * 主键查询详情信息
     *
     * @param measureId 测量id
     * @return Measure
     */
    @Override
    public Measure selectMeasureById(Long measureId) {
        return measureMapper.selectMeasureById(measureId);
    }


    /**
     * 删除用户在当前页面的测量信息
     *
     * @param measure 测量数据
     * @return boolean
     */
    @Override
    public int deleteMeasureBatch(Measure measure) {
        return measureMapper.deleteMeasureBatch(measure);
    }


}
