package cn.staitech.anno.controller;


import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.ViewAddIn;
import cn.staitech.anno.domain.vo.marking.out.MarkingSelectListVo;
import cn.staitech.anno.domain.vo.marking.out.SlideSelectBy;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


/**
 * @author gjt
 * @since 2023-09-14
 */
@Api(value = "viewer", tags = "viewer页面")
@RestController
@RequestMapping("/marking")
public class MarkingController {

    @Resource
    private MarkingService markingService;

    @Resource
    private SlideService slideService;

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取标注列表")
    @GetMapping("/intelligentEvaluation/selectListBy")
    public R<List<MarkingSelectListVo>> selectListBy(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) throws Exception {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail(MessageSource.M("ARGUMENT_INVALID"));
        }
        return R.ok(markingService.selectList(slideId));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取GeoJson数据")
    @GetMapping("/intelligentEvaluation/selectLists")
    public R<List<Features>> selectLists(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) throws Exception {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail(MessageSource.M("ARGUMENT_INVALID"));
        }
        return R.ok(markingService.selectListBy(slideId));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "添加标注")
    @PostMapping("/intelligentAnno/insert")
    public R<String> add(@Validated @RequestBody ViewAddIn req) throws Exception {
        String markingId = markingService.insert(req);
        return R.ok(markingId, MessageSource.M("OPERATE_SUCCEED"));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "删除标注")
    @ApiImplicitParams({@ApiImplicitParam(name = "markingId", value = "标注id", required = true, dataType = "Long", paramType = "query")})
    @DeleteMapping("/intelligentAnno/delete")
    public R<String> del(@RequestParam(value = "marking_id") @ApiParam(name = "marking_id", value = "标注id", required = true) String marking_id) throws Exception {
        markingService.delete(marking_id);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "更新标注")
    @PutMapping("/intelligentAnno/update")
    public R<String> update(@Validated @RequestBody MarkingUpdateIn req) throws Exception {
        markingService.update(req);
        return R.ok(req.getMarking_id(), MessageSource.M("OPERATE_SUCCEED"));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "websocket接口")
    @GetMapping("/getWebsocketPort")
    public R<String> getWebsocketPort() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return R.fail(MessageSource.M("OPERATE_ERROR"));
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String localAdd = request.getLocalAddr();
        return R.ok("ws://" + localAdd + ":" + 9999 + "/");
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "json导出", hidden = true)
    @GetMapping("/jsonExport")
    public R<String> getWebsocketPort(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId) throws Exception {
        return R.ok(markingService.slideJsonExport(slideId));
    }

    @Log(title = "标注测量excel导出", businessType = BusinessType.EXPORT)
    @ApiOperation(value = "标注测量excel导出")
    @GetMapping("/export")
    public void export(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) throws Exception {
        markingService.execlExport(slideId);
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "查询切片、图片详情接口")
    @GetMapping("/slideInfo")
    public R<SlideSelectBy> slideInfo(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "标注id", required = true) Long slideId) throws Exception {
        return R.ok(slideService.pageImageCsvListVOBy(slideId));
    }

    @ApiOperation(value = "删除页面标注")
    @DeleteMapping("/batchDelete")
    @ApiImplicitParams({@ApiImplicitParam(name = "slideId", value = "切片id", required = true, dataType = "Long", paramType = "query")})
    public R<String> batchDeleteRoi(@RequestParam @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId) {
        markingService.batchDelete(slideId);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

}

