package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.SlidePrediction;
import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.vo.diagnosis.SpecialDiagnosisAddVo;
import cn.staitech.anno.vo.examination.ExaminationListVO;
import cn.staitech.anno.vo.eyeslide.EyeProjectSlideOut;
import cn.staitech.anno.vo.eyeslide.EyeSaveSlide;
import cn.staitech.anno.vo.eyeslide.EyeSlideIn;
import cn.staitech.anno.vo.eyeslide.ProjectSlideOut;
import cn.staitech.anno.vo.image.out.ImageListOutVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvGetVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvListVO;
import cn.staitech.anno.vo.project.ProjectListOutVO;
import cn.staitech.anno.vo.project.ProjectStatisticsVO;
import cn.staitech.anno.vo.slide.*;
import cn.staitech.anno.vo.statistic.StatisticSlideListInVO;
import cn.staitech.anno.vo.statistic.StatisticSlideListOutVO;
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
public interface SlideMapper extends BaseMapper<Slide> {

    /**
     * 更新切片表人工标注数及标注状态
     *
     * @param examinationListVo
     * @return
     */
    int updateSlideHumanAnnotationQuantity(ExaminationListVO examinationListVo);

    Review selectReview(Long slideId);

    /**
     * 根据切片生成文件目录
     *
     * @param params
     * @return
     */
    SlideFileName slideReviewFileName(Long slideId);

    SlideFileName slideFileName(Long slideId);


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

    List<SlideRes> selectImageList(Long projectId);

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
     * 通过项目id查询切片的数量
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
     *
     * @param slideList
     * @return
     */
    int updateBatchByCondition(@Param("slideList") List<Slide> slideList);

    /**
     * 根据项目、分组及图像更新切片诊断状态
     *
     * @param diagnosisList
     * @return
     */
    int updateBatchBySpecialDiagnosis(@Param("diagnosisList") List<SpecialDiagnosisAddVo> diagnosisList);

    /**
     * 查询组内切片报表摘要
     *
     * @param params
     * @return
     */
    SlideReportSummaryVO selectSlideByProjectAndGroup(@Param("params") Map params);

    /**
     * 组内切片报表分页查询
     *
     * @param params
     * @return
     */
    IPage<SlideReportVO> pageSlideWithSubImage(@Param("page") Page page, @Param("params") Map params);

    /**
     * 项目内切片统计
     *
     * @param params
     * @return
     */
    IPage<ProjectStatisticsVO> pageSlideStatisticsByProject(@Param("page") Page page, @Param("params") Map params);

    /**
     * 切片报表分页查询
     *
     * @param page
     * @param params
     * @return
     */
    IPage<SlideReportVO> pageSlideStatistics(@Param("page") Page page, @Param("params") Map params);

    /**
     * 脏器病变报告查询
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> queryVisceraLesionRpt(@Param("params") Map params);


    //-------------------------------

    /**
     * 切片报表分页查询
     *
     * @param imageCsvGetVO
     * @return
     */
    List<ImageCsvListVO> pageImageCsvListVOList(ImageCsvGetVO imageCsvGetVO);

    List<ImageCsvListVO> pageImageCsvListVOList1(ImageCsvGetVO imageCsvGetVO);

    /**
     * 切片报表分页查询(单个)
     *
     * @param slideId 切片id
     * @return
     */
    SlideSelectBy pageImageCsvListVOBy(Long slideId);

//    /**
//     * 查询专题编号
//     * */
//    List<TopicIdName>topicList(Long organizationId);

    /**
     * 眼科选择图片查询
     * */
    List<ImageListOutVO> eyeSlideList(EyeSlideIn eyeSlideIn);

    /**
     * 删除文件夹下的图片
     * */
    int updateByPrimaryKeySelective(SlidePrediction slidePrediction);

    /**
     * 眼科项目图片
     * */
    List<EyeProjectSlideOut>eyeProjectSlide(EyeProjectSlideOut eyeProjectSlideOut);

    /**
     * 查询项目文件夹
     * */
    List<EyeProjectSlideOut> eyeProjectFolder(EyeProjectSlideOut eyeProjectSlideOut);

    /**
     * 眼科-查询是否有算法结果
     * */
    int algorithmResult(Long projectId);

    /**
     * 眼科-查询要添加的数据
     * */
    List<ProjectSlideOut> eyeFolder(EyeSaveSlide eyeSaveSlide);

    /**
     * 眼科-查询图片信息
     * */
    List<Image> eyeFolderSlide(Long folderId);

    /**
     * 眼科-添加slide
     * */
    int eyeInsertSlide(Slide slide);

    /**
     * 眼科添加碎片
     * */
    int eyeInsert(List<SlidePrediction> slideList);

    /**
     * 眼科-更新文件夹状态
     * */
    int eyeUpdateFolder(Slide slide);

    /**
     * 更新添加主图
     * */
    int eyeUpdateMainImage(SlidePrediction slidePrediction);
}