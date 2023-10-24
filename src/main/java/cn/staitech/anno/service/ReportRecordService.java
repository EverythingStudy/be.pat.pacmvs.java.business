package cn.staitech.anno.service;

import cn.staitech.anno.domain.ReportRecord;
import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.vo.ProjectAllVO;
import cn.staitech.anno.domain.vo.reportRecord.ReportRecordAllVO;
import cn.staitech.anno.domain.vo.reportRecord.ReportRecordExportVO;
import cn.staitech.anno.domain.vo.reportRecord.ReportRecordSingleVO;
import cn.staitech.anno.domain.vo.reportRecord.ReportRecordViewVO;

import java.util.List;

public interface ReportRecordService {

    /**
     * 添加数据
     */
    int insertSelective(ReportRecord record);

    /**
     * 查询单条数据
     */
    List<ReportRecordAllVO> selectByPrimaryKey(ReportRecordViewVO recordViewVO);


    /**
     * 更新数据
     */
    int updateByPrimaryKeySelective(ReportRecord record);


    /**
     * 查询专题下的所有项目
     */
    List<ProjectAllVO> selectProjectList(ProjectAllVO projectAllVO);

    /**
     * 条件查新切片编号
     */
    List<ReportRecordExportVO> selectSlideNumber(ReportRecordSingleVO recordSingleVO);

    /**
     * 查询单条报告数据
     */
    ReportRecordAllVO selectReport(Long reportId);

    /**
     * 查询专题下的图片
     */
    List<SubImage> selectImage(Long specialId);


}
