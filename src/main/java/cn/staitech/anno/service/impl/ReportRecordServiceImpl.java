package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ReportRecord;
import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.vo.ProjectAllVO;
import cn.staitech.anno.domain.reportrecord.ReportRecordAllVO;
import cn.staitech.anno.domain.reportrecord.ReportRecordExportVO;
import cn.staitech.anno.domain.reportrecord.ReportRecordSingleVO;
import cn.staitech.anno.domain.reportrecord.ReportRecordViewVO;
import cn.staitech.anno.mapper.ReportRecordMapper;
import cn.staitech.anno.service.ReportRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 报告 服务层实现
 *
 * @author zmj
 */
@Service
public class ReportRecordServiceImpl implements ReportRecordService {
    @Resource
    private ReportRecordMapper reportRecordMapper;

    /**
     * 添加数据
     */
    @Override
    public int insertSelective(ReportRecord record) {
        return reportRecordMapper.insertSelective(record);
    }


    /**
     * 查询单条数据
     */
    @Override
    public List<ReportRecordAllVO> selectByPrimaryKey(ReportRecordViewVO recordViewVO) {
        return reportRecordMapper.selectByPrimaryKey(recordViewVO);
    }


    /**
     * 更新数据
     */
    @Override
    public int updateByPrimaryKeySelective(ReportRecord record) {
        return reportRecordMapper.updateByPrimaryKeySelective(record);
    }


    /**
     * 查询专题下的所有项目
     */
    @Override
    public List<ProjectAllVO> selectProjectList(ProjectAllVO projectAllVO) {
        return reportRecordMapper.selectProjectList(projectAllVO);
    }

    /**
     * 条件查新切片编号
     */
    @Override
    public List<ReportRecordExportVO> selectSlideNumber(ReportRecordSingleVO recordSingleVO) {
        return reportRecordMapper.selectSlideNumber(recordSingleVO);
    }

    /**
     * 查询单条报告数据
     */
    @Override
    public ReportRecordAllVO selectReport(Long reportId) {
        return reportRecordMapper.selectReport(reportId);
    }


    /**
     * 查询专题下的图片
     */
    @Override
    public List<SubImage> selectImage(Long specialId) {
        return reportRecordMapper.selectImage(specialId);
    }


}
