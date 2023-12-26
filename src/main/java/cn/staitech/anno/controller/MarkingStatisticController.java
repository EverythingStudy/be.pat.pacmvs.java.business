package cn.staitech.anno.controller;

import cn.staitech.anno.domain.MarkingStatistic;
import cn.staitech.anno.service.MarkingStatisticService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.marking.MarkingStatisticSelectVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


/**
 * 智能标注-标注统计-用户标签统计
 *
 * @author wangfeng
 * @since 2023-12-16
 */
@Api(value = "智能标注-标注统计-用户标签统计", tags = "智能标注-标注统计-用户标签统计")
@RestController
@RequestMapping("/markingStatistic")
public class MarkingStatisticController {

    @Resource
    private MarkingStatisticService markingStatisticService;

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "智能标注-标注统计-用户标签统计-列表页")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    @Log(title = "智能标注-标注统计-用户标签统计-列表页", menu = "智能标注", subMenu = "标注统计", businessType = BusinessType.QUERY)
    @PostMapping("/list")
    public R<PageMaster<MarkingStatistic>> list(@Validated @RequestBody MarkingStatisticSelectVO selectVO) {
        PageMaster pageMaster = new PageMaster<>(markingStatisticService.selectMarkingStatistic(selectVO));
        return R.ok(pageMaster);
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "智能标注-标注统计-用户标签统计-excel导出")
    @Log(title = "智能标注-标注统计-用户标签统计-excel导出", menu = "智能标注", subMenu = "标注统计", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(@Validated @RequestBody MarkingStatisticSelectVO selectVO, HttpServletResponse response) throws Exception {
        markingStatisticService.execlExport(selectVO, response);
    }
}
