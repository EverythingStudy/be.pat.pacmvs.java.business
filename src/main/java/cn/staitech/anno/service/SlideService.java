package cn.staitech.anno.service;

import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.vo.ExaminationListVO;
import cn.staitech.anno.domain.vo.ProjectListOutVO;
import cn.staitech.anno.domain.vo.SlideSelectVO;
import cn.staitech.anno.domain.vo.image.ProjectStatisticsVo;
import cn.staitech.anno.domain.vo.image.SlideReportSummaryVo;
import cn.staitech.anno.domain.vo.image.SlideReportVo;
import cn.staitech.anno.domain.vo.imageCsv.ImageCsvGetPagerVO;
import cn.staitech.anno.domain.vo.imageCsv.ImageCsvGetVO;
import cn.staitech.anno.domain.vo.imageCsv.ImageCsvListVO;
import cn.staitech.anno.domain.vo.slideVo.AddSlideVO;
import cn.staitech.anno.domain.vo.statistic.StatisticSlideListInVO;
import cn.staitech.anno.domain.vo.statistic.StatisticSlideListOutVO;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 切片 服务层 .
 *
 * @author staitech
 */
public interface SlideService extends IService<Slide> {

    /**
     * 查询单条切片详情
     *
     * @param slideId
     * @return
     */
    Slide selectById(Long slideId);

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
     * 逻辑删除
     *
     * @param slideIdList
     * @return
     */
    boolean deleteProjectImage(Long[] slideIdList) throws Exception;

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
     */
    int insertSlide(@Param("slideList") List<Slide> slideList);

    /**
     * 更新切片描述
     *
     * @param slide
     * @return
     */
    int updateDescription(Slide slide);

    Boolean markIsNotFinish(Long slideId);

    /**
     * 更新切片时间
     *
     * @param slide
     * @return
     */
    int updateSlideTime(Slide slide);

    /**
     * 根据项目、分组及图像更新切片关系表
     *
     * @param slideList
     * @return
     */
    int updateBatchByCondition(List<Slide> slideList);

    /**
     * 查询组内切片报表摘要
     *
     * @param params
     * @return
     */
    R<SlideReportSummaryVo> querySlideByProjectAndGroup(Map params);

    /**
     * 组内切片报表分页查询
     *
     * @param params
     * @return
     */
    R<PageMaster<SlideReportVo>> pageSlideWithSubImage(Map params);


    /**
     * 项目内切片统计
     *
     * @param params
     * @return
     */
    R<PageMaster<ProjectStatisticsVo>> pageSlideStatisticsByProject(Map params);

    /**
     * 切片统计
     *
     * @param params
     * @return
     */
    R<PageMaster<SlideReportVo>> pageSlideStatistics(Map params);

    void jsonExport(List<Long> slideList, Long projectId, Integer status) throws Exception;


    // =========================

    /**
     * 选片 - 添加标注切片 .
     *
     * @param addSlideVO
     * @return
     */
    boolean addAnnoSlidesBatch(AddSlideVO addSlideVO);

    /**
     * 批量删除切片
     *
     * @param slideIds
     * @return
     */
    int delSlidesBatch(List<Long> slideIds);

    /**
     * 查询标注项目切片列表
     */
    List<ImageCsvListVO> pageSlides(ImageCsvGetVO imageCsvGetVO);

    public List<ImageCsvListVO> pageSlides1(ImageCsvGetVO request);

    /**
     * 查询评审项目切片列表
     */
    PageMaster<ImageCsvListVO> pageReviewRoundSSlides(ImageCsvGetPagerVO imageCsvGetPagerVO);


    /**
     * 查询切片、图片信息接口
     * @param slideId 切片id
     * @return
     */
    ImageCsvListVO pageImageCsvListVOBy(Long slideId);

}