package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Annotation;
import cn.staitech.anno.domain.SlideViewer;
import cn.staitech.anno.domain.vo.AnnotationBroadcastVO;
import cn.staitech.anno.domain.vo.AnnotationJsonVO;
import cn.staitech.anno.domain.vo.AnnotationPageVO;
import cn.staitech.anno.domain.vo.AnnotationStateVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标注表表 数据层 .
 *
 * @author staitech
 */
public interface AnnotationMapper {

    /**
     * 关闭tb_annotation索引 .
     */
    int openItIndex();

    /**
     * 关闭tb_annotation索引 .
     */
    int shutDownIndex();

    /**
     * 查询标注Roi .
     *
     * @param slideId 切片id
     * @return True || false
     */
    List<Annotation> selectAnnotationRoi(Long slideId);

    /**
     * 批量删除RoI标注 .
     *
     * @param slideId 切片id
     * @return True || false
     */
    int batchDeleteRoi(Long slideId);

    /**
     * 批量插入标注信息 .
     *
     * @param annotationList 标注信息
     * @return True || false
     */
    int insertAnnotationList(@Param("annotationList") List<Annotation> annotationList);

    /**
     * 查询全部标注详细信息 .
     *
     * @param annotationPageVo 分页信息
     * @return 标注信息
     */
    List<AnnotationBroadcastVO> annotationUserCategory(AnnotationPageVO annotationPageVo);

    /**
     * 查询标注详细信息 .
     *
     * @param annotationId 标注id
     * @return 标注信息
     */
    AnnotationBroadcastVO annoUserCategory(Long annotationId);

    /**
     * 查询标注信息 .
     *
     * @param annotationId 标注id
     * @return 图像信息
     */
    Annotation selectAnnotationById(Long annotationId);

    /**
     * 查询标注表中的数量 .
     *
     * @param slideId 切片id
     * @return 标注数量
     */
    int annotationCount(Long slideId);

    /**
     * 新增标注 .
     *
     * @param annotation 新增标注
     * @return 结果
     */
    int insertAnnotation(Annotation annotation);

    /**
     * 修改标注 .
     *
     * @param annotation 修改标注
     * @return 结果
     */
    int updateAnnotation(Annotation annotation);

    /**
     * 修改标注描述 .
     *
     * @param annotation 修改标注
     * @return 结果
     */
    int updateAnnotationDescription(Annotation annotation);

    /**
     * 删除标注 .
     *
     * @param annotationId 删除标注
     * @return 结果
     */
    int deleteAnnotationById(Long annotationId);

    List<Annotation> selectSlideBy(Long slideId);

    List<Annotation> selectAnnotationByProjectIdAndUserId(Annotation annotation);

    int deleteAnnotationByProjectIdAndUserId(Annotation annotation);

    Integer selectAnnotationCount(Long slideId);

    /**
     * 查询图像信息 .
     *
     * @param slideId 切片id
     * @return
     */
    SlideViewer selectImageBySlideId(Long slideId);

    /**
     * 修改标注状态 .
     *
     * @param viewerAnnotationVo 标注状态信息
     * @return
     */
    int updateViewerAnnotation(AnnotationStateVO viewerAnnotationVo);

    /**
     * 通过切片id查询标注信息 .
     *
     * @param slideId
     * @return
     */
    List<AnnotationJsonVO> selectAnnotationJson(Long slideId);

    /**
     * 根据projectId 查询用户 .
     *
     * @param projectId 项目id
     * @return
     */
    List<Annotation> selectByProjectId(Long projectId);

    /**
     * 根据annotation 查询标注信息 .
     *
     * @param annotation 查询条件
     * @return
     */
    List<Annotation> selectByCondition(Annotation annotation);

    /**
     * 查询已导入的标注
     *
     * @param annotation
     * @return 标注列表
     */
    List<Annotation> queryUploadAnnotation(Annotation annotation);
}