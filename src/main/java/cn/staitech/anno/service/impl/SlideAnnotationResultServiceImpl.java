package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.SlideAnnotationResult;
import cn.staitech.anno.domain.vo.ProjectAnnotationVO;
import cn.staitech.anno.domain.vo.ProjectInForImageVO;
import cn.staitech.anno.mapper.SlideAnnotationResultMapper;
import cn.staitech.anno.service.SlideAnnotationResultService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 切片标注关联 服务层实现
 *
 * @author: YL
 * @email: yangl@staitech.cn
 */
@Service
public class SlideAnnotationResultServiceImpl implements SlideAnnotationResultService {
    @Resource
    private SlideAnnotationResultMapper slideAnnotationResultMapper;

    /**
     * 通过切片ID、更新者ID、标注类别查询
     *
     * @param slideAnnotationResult
     * @return
     */
    @Override
    public SlideAnnotationResult selectById(SlideAnnotationResult slideAnnotationResult) {
        return slideAnnotationResultMapper.selectById(slideAnnotationResult);
    }

    /**
     * 根据项目ID统计项目标注数量
     *
     * @param slide
     * @return
     */
    @Override
    public ProjectAnnotationVO selectProjectHumanAnnotationQuantity(Long slide) {
        return slideAnnotationResultMapper.selectProjectHumanAnnotationQuantity(slide);
    }

    /**
     * 根据切片ID、更新者ID、标注类别统计标注数量
     *
     * @param slideAnnotationResult
     * @return
     */
    @Override
    public Integer selectSum(SlideAnnotationResult slideAnnotationResult) {
        return slideAnnotationResultMapper.selectSum(slideAnnotationResult);
    }

    /**
     * 添加标注
     *
     * @param slideAnnotationResult
     * @return
     */
    @Override
    public int insertBatch(SlideAnnotationResult slideAnnotationResult) {
        return slideAnnotationResultMapper.insertBatch(slideAnnotationResult);
    }

    /**
     * 更新标注
     *
     * @param slideAnnotationResult
     * @return
     */
    @Override
    public int updateBatch(SlideAnnotationResult slideAnnotationResult) {
        return slideAnnotationResultMapper.updateBatch(slideAnnotationResult);
    }

    /**
     * 通过切片ID统计人工标注数
     *
     * @param slideId
     * @return
     */
    @Override
    public Integer selectSumBySlideId(Long slideId) {
        return slideAnnotationResultMapper.selectSumBySlideId(slideId);
    }

    /**
     * 查询用户
     *
     * @param projectInforImageVO
     * @return
     */
    @Override
    public List<SlideAnnotationResult> selectUpdateBy(ProjectInForImageVO projectInforImageVO) {
        return slideAnnotationResultMapper.selectUpdateBy(projectInforImageVO);
    }
}
