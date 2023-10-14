package cn.staitech.anno.controller;

import cn.staitech.anno.domain.AnnotationLog;
import cn.staitech.anno.service.AnnotationLogService;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(value = "标注日志接口", tags = "标注日志页面")
@RestController
@Validated
@RestControllerAdvice
@RequestMapping("/annotationLog")
public class AnnotationLogController {

    @Resource
    private AnnotationLogService annotationLogService;

    @ApiOperation(value = "标注详情接口", hidden = true)
    @PostMapping("/annotationLog")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "annotationId", value = "标注id", required = true, dataType = "Long", paramType = "query")})
    public R<List<AnnotationLog>> annotationLog(
            @RequestParam @ApiParam(name = "annotationId", value = "标注id", required = true) Long annotationId) {
        List<AnnotationLog> annotationLog = annotationLogService.selectAnnotationLogById(annotationId);
        return R.ok(annotationLog);
    }

}
