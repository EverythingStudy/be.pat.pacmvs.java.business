//package cn.staitech.anno.controller;
//
//import cn.staitech.anno.domain.Annotation;
//import cn.staitech.anno.domain.PathologicalIndicatorCategoryLog;
//import cn.staitech.anno.domain.Slide;
//import cn.staitech.anno.domain.vo.AnnotationBroadcastVo;
//import cn.staitech.anno.domain.vo.PathologicalLogUpdateVo;
//import cn.staitech.anno.domain.vo.PathologicalLogVo;
//import cn.staitech.anno.domain.vo.SendAllMessageVo;
//import cn.staitech.anno.domain.vo.SlideAnnotationResultVo;
//import cn.staitech.anno.service.AnnotationService;
//import cn.staitech.anno.service.PathologicalIndicatorCategoryLogService;
//import cn.staitech.anno.service.SlideService;
//import cn.staitech.anno.utils.SendOneMessage;
//import cn.staitech.common.core.domain.R;
//import cn.staitech.common.log.annotation.Log;
//import cn.staitech.common.log.enums.BusinessType;
//import cn.staitech.common.security.annotation.RequiresPermissions;
//import cn.staitech.common.security.utils.SecurityUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.BeanUtils;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//
///**
// * viewer标注类别.
// */
//@Api(tags = "viewer标注类别")
//@RestController
//@RequestMapping("/pathologicalLog")
//public class PathologicalLogController {
//
//    @Resource
//    private PathologicalIndicatorCategoryLogService pathologicalLogService;
//
//    @Resource
//    private RabbitTemplate rabbitTemplate;
//
//    @Resource
//    private AnnotationService annotationService;
//
//    @Resource
//    private SlideService slideService;
//
//    @SuppressWarnings("checkstyle:MissingJavadocMethod")
//    @ApiOperation(value = "删除标注类别信息")
//    @PostMapping("/pathologicalLogDelete")
//    @Log(title = "标注类别", businessType = BusinessType.DELETE)
//    @RequiresPermissions("anno:pathologicalLog:category")
//    public R<String> pathologicalLogDelete(@Validated @RequestBody PathologicalLogVo record) throws Exception {
//
//        Annotation annotationBy = annotationService.selectAnnotationById(record.getAnnotationId());
//
//        if (annotationBy == null) {
//
//            return R.fail("该标注不存在");
//        }
//
//        String message = "该图像已完成标注,不可删除标注类别";
//
//        // 判断是否完成标注
//        Slide slide = slideService.selectSlideProcessFlag(annotationBy.getSlideId(), message);
//
//        PathologicalIndicatorCategoryLog pathologicalIndicatorCategoryLog = new PathologicalIndicatorCategoryLog();
//
//        BeanUtils.copyProperties(record, pathologicalIndicatorCategoryLog);
//
//        List<SlideAnnotationResultVo> resData = pathologicalLogService.slideAnnotationResultVo(annotationBy, slide);
//
//        rabbitTemplate.convertAndSend("SlideAnnotationResultExchange", "SlideAnnotationResultRouting", resData);
//
//        pathologicalLogService.deleteByAnnotationIdCateId(pathologicalIndicatorCategoryLog);
//
//        // 删除完成之后，更新标注表中的类别id为null
//
//        Long categoryId = null;
//
//        Annotation annotation = new Annotation();
//
//        annotation.setAnnotationId(record.getAnnotationId());
//
//        annotation.setCategoryId(categoryId);
//
//        // 更新标注表中信息
//        annotationService.updateAnnotation(annotation);
//
//        AnnotationBroadcastVo annotationBroadcastVo = annotationService.selectAnnotationBroadcastVo(
//                pathologicalIndicatorCategoryLog.getAnnotationId());
//
//        SendAllMessageVo sendAllMessageVo = SendOneMessage.sendOneMessages(200, "标注类别删除成功", "update",
//                annotationBroadcastVo, annotationBroadcastVo.getSlideId());
//
//        rabbitTemplate.convertAndSend("websocketDirectExchange", "websocketDirectRouting", sendAllMessageVo);
//
//        return R.ok("删除成功");
//    }
//
//    @SuppressWarnings("checkstyle:MissingJavadocMethod")
//    @ApiOperation(value = "新增标注类别信息")
//    @PostMapping("/pathologicalLogAdd")
//    @Log(title = "标注类别", businessType = BusinessType.INSERT)
//    @RequiresPermissions("anno:pathologicalLog:category")
//    public R<String> pathologicalLogAdd(@Validated @RequestBody PathologicalLogVo req) throws Exception {
//
//        // 查询标注表中信息
//        Annotation annotationBy = annotationService.selectAnnotationById(req.getAnnotationId());
//
//        if (annotationBy == null) {
//
//            return R.fail("该标注不存在");
//        }
//
//        String message = "该图像已完成标注,不可新增标注类别";
//
//        // 判断是否完成标注
//        final Slide slide = slideService.selectSlideProcessFlag(annotationBy.getSlideId(), message);
//
//        PathologicalIndicatorCategoryLog pathologicalIndicatorCategoryLog = new PathologicalIndicatorCategoryLog();
//
//        BeanUtils.copyProperties(req, pathologicalIndicatorCategoryLog);
//
//        // 标注记录表中添加信息
//        pathologicalLogService.insertSelective(pathologicalIndicatorCategoryLog);
//
//        Annotation annotation = new Annotation();
//
//        annotation.setAnnotationId(req.getAnnotationId());
//
//        annotation.setCategoryId(req.getCategoryId());
//
//        // 将categoryId更新至标注表中
//        annotationService.updateAnnotation(annotation);
//
//        AnnotationBroadcastVo annotationBroadcastVo = annotationService.selectAnnotationBroadcastVo(
//                pathologicalIndicatorCategoryLog.getAnnotationId());
//
//        List<SlideAnnotationResultVo> resData = pathologicalLogService.slideAnnotationResultVo(annotationBy, slide);
//
//        rabbitTemplate.convertAndSend("SlideAnnotationResultExchange", "SlideAnnotationResultRouting", resData);
//
//        SendAllMessageVo sendAllMessageVo = SendOneMessage.sendOneMessages(200, "标注类别添加成功", "update",
//                annotationBroadcastVo, annotationBroadcastVo.getSlideId());
//
//        rabbitTemplate.convertAndSend("websocketDirectExchange", "websocketDirectRouting", sendAllMessageVo);
//
//        return R.ok("标注类别添加成功");
//
//    }
//
//    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:WhitespaceAround"})
//    @ApiOperation(value = "修改标注类别接口")
//    @PostMapping("/updateByAnnoIdCateId")
//    @Log(title = "标注类别", businessType = BusinessType.UPDATE)
//    @RequiresPermissions("anno:pathologicalLog:category")
//    public R<String> updateByAnnoIdCateId(@Validated @RequestBody PathologicalLogUpdateVo req) throws Exception {
//
//        // 查询标注表中信息
//        Annotation annotationBy = annotationService.selectAnnotationById(req.getAnnotationId());
//
//        if (annotationBy == null) {
//
//            return R.fail("该标注不存在");
//        }
//
//        String message = "该图像已完成标注,不可修改标注类别";
//
//        // 判断是否完成标注
//        Slide slide = slideService.selectSlideProcessFlag(annotationBy.getSlideId(), message);
//
//        final List<SlideAnnotationResultVo> resData = pathologicalLogService.slideAnnotationResultVo(annotationBy,
//                slide);
//
//        // 删除记录表中与 annotation 有关的记录
//        pathologicalLogService.deleteByAnnoIdCateId(req.getAnnotationId());
//
//        // 判断 req.getCategoryIdList() 是否为空
//        if (!req.getCategoryIdList().isEmpty()) {
//            // 循环  List<categoryId>
//            for (Long category : req.getCategoryIdList()) {
//
//                PathologicalIndicatorCategoryLog pathologicalIndicatorCategoryLog = new PathologicalIndicatorCategoryLog();
//
//                pathologicalIndicatorCategoryLog.setAnnotationId(req.getAnnotationId());
//
//                pathologicalIndicatorCategoryLog.setCategoryId(category);
//
//                pathologicalIndicatorCategoryLog.setCreateBy(SecurityUtils.getUserId());
//
//                // 添加标注记录
//                pathologicalLogService.insertSelective(pathologicalIndicatorCategoryLog);
//
//                Annotation annotations = new Annotation();
//
//                annotations.setAnnotationId(req.getAnnotationId());
//
//                annotations.setCategoryId(category);
//
//                // 更新标注表中信息
//                annotationService.updateAnnotation(annotations);
//
//            }
//        } else {
//            Annotation annotations = new Annotation();
//
//            annotations.setAnnotationId(req.getAnnotationId());
//
//            annotations.setCategoryId(null);
//
//            // 更新标注表中信息
//            annotationService.updateAnnotation(annotations);
//        }
//
//        AnnotationBroadcastVo annotationBroadcastVo = annotationService.selectAnnotationBroadcastVo(
//                req.getAnnotationId());
//
//        SendAllMessageVo sendAllMessageVo = SendOneMessage.sendOneMessages(200, "标注类别添加成功", "update",
//                annotationBroadcastVo, annotationBroadcastVo.getSlideId());
//
//        rabbitTemplate.convertAndSend("websocketDirectExchange", "websocketDirectRouting", sendAllMessageVo);
//
//        List<SlideAnnotationResultVo> resData1 = pathologicalLogService.slideAnnotationResultVo(annotationBy, slide);
//
//        rabbitTemplate.convertAndSend("SlideAnnotationResultExchange", "SlideAnnotationResultRouting", resData);
//
//        rabbitTemplate.convertAndSend("SlideAnnotationResultExchange", "SlideAnnotationResultRouting", resData1);
//
//        return R.ok("修改完成");
//
//    }
//
//}
