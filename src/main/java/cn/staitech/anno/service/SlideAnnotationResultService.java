package cn.staitech.anno.service;

import cn.staitech.anno.domain.SlideAnnotationResult;
import cn.staitech.anno.domain.vo.ProjectAnnotationVO;
import cn.staitech.anno.domain.vo.ProjectInForImageVO;

import java.util.List;

/**
 * 切片标注关联 服务层 .
 *
 * @author: YL
 * @email: yangl@staitech.cn
 */
public interface SlideAnnotationResultService {

    /**
     * 通过切片ID、更新者ID、标注类别查询
     *
     * @param slideAnnotationResult
     * @return
     */
    SlideAnnotationResult selectById(SlideAnnotationResult slideAnnotationResult);

    /**
     * 根据项目ID统计项目标注数量
     *
     * @param slide
     * @return
     */
    ProjectAnnotationVO selectProjectHumanAnnotationQuantity(Long slide);

    /**
     * 根据切片ID、更新者ID、标注类别统计标注数量
     *
     * @param slideAnnotationResult
     * @return
     */
    Integer selectSum(SlideAnnotationResult slideAnnotationResult);

    /**
     * 添加标注
     *
     * @param slideAnnotationResult
     * @return
     */
    int insertBatch(SlideAnnotationResult slideAnnotationResult);

    /**
     * 更新标注
     *
     * @param slideAnnotationResult
     * @return
     */
    int updateBatch(SlideAnnotationResult slideAnnotationResult);

    /**
     * 通过切片ID统计人工标注数
     *
     * @param slideId
     * @return
     */
    Integer selectSumBySlideId(Long slideId);

    /**
     * 查询用户
     *
     * @param projectInforImageVO
     * @return
     */
    List<SlideAnnotationResult> selectUpdateBy(ProjectInForImageVO projectInforImageVO);
}