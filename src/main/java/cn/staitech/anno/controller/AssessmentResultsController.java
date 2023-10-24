package cn.staitech.anno.controller;


import cn.staitech.anno.domain.AssessmentResults;
import cn.staitech.anno.domain.assessmentResults.AssessmentResultsQueryIn;
import cn.staitech.anno.service.AssessmentResultsService;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gjt
 * @since 2023-10-17
 */
@Api(value = "算法考核", tags = "考核结果")
@RestController
@RequestMapping("/assessmentResults")
public class AssessmentResultsController {

    @Resource
    private AssessmentResultsService assessmentResultsService;

    @ApiOperation(value = "考核结果-分页查询")
    @PostMapping("/pageList")
    public R<PageResponse<AssessmentResults>> page(@Validated @RequestBody AssessmentResultsQueryIn in) {
        return R.ok(assessmentResultsService.selectPageList(in));
    }

}

