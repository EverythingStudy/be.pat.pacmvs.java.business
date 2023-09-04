package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.vo.*;
import cn.staitech.anno.domain.vo.diagnosis.SpecialDiagnosisAddVo;
import cn.staitech.anno.domain.vo.image.ProjectStatisticsVo;
import cn.staitech.anno.domain.vo.image.SlideReportSummaryVo;
import cn.staitech.anno.domain.vo.image.SlideReportVo;
import cn.staitech.anno.domain.vo.statistic.StatisticSlideListInVO;
import cn.staitech.anno.domain.vo.statistic.StatisticSlideListOutVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 切片 数据层
 *
 * @author staitech
 */
public interface SlideMapper  extends BaseMapper<Slide> {

    /**
     * 更新切片表人工标注数及标注状态
     *
     * @param examinationListVo
     * @return
     */
    int updateSlideHumanAnnotationQuantity(ExaminationListVO examinationListVo);

    /**
     * 根据项目ID列表查询图像列表
     *
     * @param projectIdList 项目ID列表
     * @return
     */
    List<StatisticSlideListOutVO> selectSlideListByProjectIdList(StatisticSlideListInVO projectIdList);

    /**
     * 通过切片ID查询切片信息
     *
     * @param slideId
     * @return
     */
    Slide selectByPrimaryKey(Long slideId);

    /**
     * 逻辑删除删除
     *
     * @param slideId
     * @return 结果
     */
    int deleteProjectImage(Long slideId);

    /**
     * 获取项目信息
     *
     * @param projectId 项目id
     * @return 结果
     */
    List<Slide> getProjectInformation(Long projectId);

    /**
     * 根据项目id删除信息
     *
     * @param projectId 项目id
     * @return 结果
     */
    int deleteProjectInformation(Long projectId);

    /**
     * 项目id 和图片id 查询是否存在
     *
     * @param
     * @return 结果
     */
    List<Slide> selectImageExist(Slide slide);

    /**
     * 通过项目id 查询审核过的数量
     *
     * @param projectId
     * @return 结果
     */
    Integer selectCheckNum(Long projectId);

    /**
     * 通过项目id 和状态查询
     *
     * @param slideSelectVo
     * @return 结果
     */
    List<ProjectListOutVO> selectByStatus(SlideSelectVO slideSelectVo);

    /**
     * 项目批量添加图片
     *
     * @param slideList
     * @return
     */
    int insertSlide(@Param("slideList") List<Slide> slideList);

    /**
     * 项目批量添加图片
     *
     * @param slide
     * @return
     */
    int updateDescription(Slide slide);
    
    /**
     * 项目批量添加图片
     *
     * @param slide
     * @return
     */
    int updateSlideTime(Slide slide);


    /**
     * 根据项目、分组及图像更新切片关系表
     * @param slideList
     * @return
     */
    int updateBatchByCondition(@Param("slideList") List<Slide> slideList);

    /**
     * 根据项目、分组及图像更新切片诊断状态
     * @param diagnosisList
     * @return
     */
    int updateBatchBySpecialDiagnosis(@Param("diagnosisList") List<SpecialDiagnosisAddVo> diagnosisList);

    /**
     * 查询组内切片报表摘要
     * @param params
     * @return
     */
    SlideReportSummaryVo selectSlideByProjectAndGroup(@Param("params") Map params);

    /**
     * 组内切片报表分页查询
     * @param params
     * @return
     */
    IPage<SlideReportVo> pageSlideWithSubImage(@Param("page") Page page, @Param("params") Map params);

    /**
     * 项目内切片统计
     * @param params
     * @return
     */
    IPage<ProjectStatisticsVo> pageSlideStatisticsByProject(@Param("page") Page page, @Param("params") Map params);

    /**
     * 切片报表分页查询
     * @param page
     * @param params
     * @return
     */
    IPage<SlideReportVo> pageSlideStatistics(@Param("page") Page page, @Param("params") Map params);

    /**
     * 脏器病变报告查询
     * @param params
     * @return
     */
    List<Map<String,Object>> queryVisceraLesionRpt(@Param("params") Map params);

}