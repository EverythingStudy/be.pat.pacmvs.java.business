package cn.staitech.annotation.controller;

import cn.staitech.annotation.utils.annotation.UndoRedoReq;
import cn.staitech.annotation.vo.anno.*;
import cn.staitech.common.core.domain.R;
import cn.staitech.annotation.domain.Annotation;
import cn.staitech.annotation.netty.message.AnnotationFeature;
import cn.staitech.annotation.service.AnnotationService;
import cn.staitech.annotation.utils.annotation.AnnotationMessageGenerator;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.locationtech.jts.geom.Geometry;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mugw
 * @version 1.0
 * @description 标注管理
 * @date 2025/5/21 14:33:43
 */
@Api(value = "标注管理")
@RestController
@RequestMapping("/annotation")
public class AnnotationController {

    @Resource
    private AnnotationService annotationService;

    @ApiOperation(value = "添加标注")
    @PostMapping("/insert")
    public R<String> addAnnotation(@Validated @RequestBody AnnotationAddReq annotationAddVo) throws Exception {
        AnnotationVo req = new AnnotationVo();
        BeanUtils.copyProperties(req, annotationAddVo);
        Annotation annotation = annotationService.addAnnotation(req);
        return R.ok(String.valueOf(annotation.getAnnotationId()));
    }

    @PostMapping("/getDistance")
    @ApiOperation(value = "获取间距")
    public R<AnnotationDistanceVo> getDistance(@Validated @RequestBody AnnotationDistanceReq req) throws Exception {
        return annotationService.getDistance(req);
    }


    @ApiOperation(value = "删除标注")
    @PostMapping("/delete")
    public R<String> deleteAnnotation(@RequestBody AnnotationVo req) throws Exception {
        return annotationService.deleteAnnotation(req.getAnnotationId());
    }

    @ApiOperation(value = "删除标注")
    @PostMapping("/deleteBySlide")
    public R<Boolean> deleteBySlide(@RequestBody List<Long> slideIds) throws Exception {
        return R.ok(annotationService.remove(Wrappers.<Annotation>lambdaQuery().in(Annotation::getSlideId, slideIds)));
    }

    @ApiOperation(value = "更新标注")
    @PostMapping("/update")
    public R<String> updateAnnotation(@Validated @RequestBody AnnotationUpdateVo req) throws Exception {
        return annotationService.updateAnnotation(req);
    }

    @ApiOperation(value = "填充轮廓")
    @PostMapping("/padding")
    public R<String> padding(@Validated @RequestBody AnnotationUpdateVo req) throws Exception {
        return annotationService.padding(req);
    }

    @ApiOperation(value = "复制/粘贴轮廓")
    @PostMapping("/stickup")
    public R<String> stickup(@Validated @RequestBody AnnotationUpdateVo req) throws Exception {
        return annotationService.stickup(req);
    }


    @ApiOperation(value = "轮廓合并预览")
    @PostMapping("/mergePreview")
    public R<Geometry> mergePreview(@RequestBody AnnotationMergePreviewReq req) throws Exception {
        List<Long> annotationIds = req.getMarkingIdList();
        return annotationService.mergePreview(annotationIds);
    }

    @ApiOperation(value = "获取GeoJson数据")
    @PostMapping("/selectLists")
    public R<List<AnnotationFeature>> selectLists(@Validated @RequestBody AnnotationReq req) throws Exception {
        LambdaQueryWrapper<Annotation> annotationLambda = Wrappers.<Annotation>lambdaQuery().eq(Annotation::getSlideId, req.getSlideId());
        if(null != req.getContourType()) {
            annotationLambda.eq(Annotation::getContourType, req.getContourType());
        }
        List<Annotation> annotations = annotationService.list();
        List<AnnotationFeature> resp = CollectionUtils.isEmpty(annotations) ? new ArrayList<>() : AnnotationMessageGenerator.generateFeatures(annotations);
        return R.ok(resp);
    }

    /**
     * @param req
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "合并、裁剪轮廓")
    @PostMapping("/updateOperation")
    public R<Geometry> annotationOperation(@Validated @RequestBody AnnotationOperationReq req) throws Exception {
        return annotationService.annotationOperation(req);
    }

    @ApiOperation(value = "批量操作")
    @PostMapping("/batch")
    public R<List<AnnotationBatchRespVo>> batch(@Validated @RequestBody AnnotationBatchReq req) throws Exception {
        return annotationService.batch(req);
    }

    @ApiOperation(value = "查询标签是否被使用", hidden = true)
    @PostMapping("/checkTagUsageStatus")
    public R<Boolean> checkTagUsageStatus(@RequestParam("id") Long id) throws Exception {
        long count = annotationService.count(Wrappers.<Annotation>lambdaQuery().eq(Annotation::getTagId, id));
        return R.ok(count > 0);
    }

    @ApiOperation(value = "检查是否存在用户的标注数据", hidden = true)
    @PostMapping("/checkUserOperation")
    public R<Long> checkUserOperation(@RequestBody CheckUserOperation req) throws Exception {
        long count = annotationService.count(Wrappers.<Annotation>lambdaQuery()
                .eq(Annotation::getCreateBy, req.getUserId())
                .in(Annotation::getSlideId, req.getSlideId()));
        return R.ok(count);
    }

    @ApiOperation(value = "查询切片标注数量", hidden = true)
    @PostMapping("/countAnnoBySlide/{slideId}")
    public R<Long> countAnnoBySlide(@PathVariable("slideId") Long slideId) throws Exception {
        long count = annotationService.count(Wrappers.<Annotation>lambdaQuery()
                .eq(Annotation::getSlideId, slideId));
        return R.ok(count);
    }

    @ApiOperation(value = "查询切片标注数量", hidden = true)
    @PostMapping("/countAnnoBySlides")
    public R<Long> countAnnoBySlides(@RequestBody List<Long> slideIds) throws Exception {
        Long count = annotationService.count(Wrappers.<Annotation>lambdaQuery()
                .in(Annotation::getSlideId, slideIds));
        return R.ok(count);
    }

    @ApiOperation(value = "撤销")
    @PostMapping("/undoAnnotation/{slideId}")
    public R<Boolean> undoAnnotation(@PathVariable Long slideId) throws Exception {
        return annotationService.undoAnnotation(UndoRedoReq.builder().slideId(slideId).userId(SecurityUtils.getUserId()).build());
    }

    @ApiOperation(value = "还原")
    @PostMapping("/redoAnnotation/{slideId}")
    public R<Boolean> redo(@PathVariable Long slideId) throws Exception {
        return annotationService.redoAnnotation(UndoRedoReq.builder().slideId(slideId).userId(SecurityUtils.getUserId()).build());
    }

    @ApiOperation(value = "清除撤销、还原操作")
    @PostMapping("/clear/{slideId}")
    public R<Boolean> clearUndoAndRedoStack(@PathVariable Long slideId) throws Exception {
        return annotationService.clearUndoAndRedoStack(UndoRedoReq.builder().slideId(slideId).userId(SecurityUtils.getUserId()).build());
    }

    @ApiOperation(value = "检查撤销、还原状态")
    @PostMapping("/checkUndoAndRedoStatus/{slideId}")
    public R<Boolean> checkUndoAndRedoStatus(@PathVariable Long slideId) throws Exception {
        return annotationService.checkUndoAndRedoStatus(UndoRedoReq.builder().slideId(slideId).userId(SecurityUtils.getUserId()).build());
    }

}
