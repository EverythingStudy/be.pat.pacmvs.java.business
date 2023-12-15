package cn.staitech.anno.service;

import cn.staitech.anno.domain.Annotation;
import cn.staitech.anno.domain.SlideViewer;
import cn.staitech.anno.vo.annotation.AnnotationBroadcastVO;
import cn.staitech.anno.vo.annotation.AnnotationJsonVO;
import cn.staitech.anno.vo.annotation.AnnotationPageVO;
import cn.staitech.anno.vo.annotation.AnnotationStateVO;

import java.util.List;

/**
 * 标注 服务层  .
 *
 * @author staitech
 */

public interface AnnotationService {

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
    int insertAnnotationList(List<Annotation> annotationList);

    /**
     * 分页查询 .
     *
     * @param annotationPageVo 分页信息
     * @return 分页后的数据
     */
    List<AnnotationBroadcastVO> annotationUserCategory(AnnotationPageVO annotationPageVo);

    /**
     * 标注详情查询 .
     *
     * @param annotationId 标注id
     * @return 标注详情数据
     */
    AnnotationBroadcastVO annoUserCategory(Long annotationId);

    /**
     * 查询标注的数量 .
     *
     * @param slideId 切片id
     * @return 数量
     */
    int annotationCount(Long slideId);

    /**
     * .
     *
     * @param annotationId 标注id
     * @return
     */
    Annotation selectAnnotationById(Long annotationId);

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
     * @throws Exception 添加事务
     */
    int updateAnnotation(Annotation annotation);

    /**
     * 修改标注描述 .
     *
     * @param annotation 修改标注描述
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


    /**
     * 查询图像信息 .
     *
     * @param slideId 切片id
     */
    SlideViewer selectImageBySlideId(Long slideId);

    /**
     * 修改标注状态 .
     *
     * @param viewerAnnotationVo 标注状态
     */
    int updateViewerAnnotation(AnnotationStateVO viewerAnnotationVo);


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
     * 通过项目ID，用户ID，删除标注 .
     *
     * @param annotation 标注
     * @return 结果
     */
    int deleteAnnotationByProjectIdAndUserId(Annotation annotation);

    /**
     * 切片id查询unable以外标注 .
     *
     * @param slideId 切片id
     * @return 结果
     */
    Integer selectAnnotationCount(Long slideId);

    /**
     * 通过项目ID，用户ID，查询标注 .
     *
     * @param annotation 标注
     * @return 结果
     */
    List<Annotation> selectAnnotationByProjectIdAndUserId(Annotation annotation);

    /**
     * 通过切片id查询所有标注 .
     *
     * @param slideId 切片id
     * @return 结果
     */
    List<Annotation> selectSlideBy(Long slideId);

    /**
     * 查询当前用户在项目中的权限 .
     *
     * @param projectId 项目id
     * @return 结果
     */
    boolean getPermission(Long projectId, String permission) throws Exception;

    /**
     * 查询已导入的标注
     *
     * @param annotation
     * @return 标注列表
     */
    List<Annotation> queryUploadAnnotation(Annotation annotation);
}