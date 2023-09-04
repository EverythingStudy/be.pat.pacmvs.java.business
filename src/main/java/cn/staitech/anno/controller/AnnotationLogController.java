package cn.staitech.anno.controller;

import cn.staitech.anno.domain.AnnotationLog;
import cn.staitech.anno.service.AnnotationLogService;
import cn.staitech.common.core.domain.R;
import io.lettuce.core.ScriptOutputType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
