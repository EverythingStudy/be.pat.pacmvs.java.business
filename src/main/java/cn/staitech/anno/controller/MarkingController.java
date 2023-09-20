package cn.staitech.anno.controller;


import cn.hutool.core.io.IoUtil;
import cn.staitech.anno.constant.R.ResponseConstant;
import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.viewAddIn;
import cn.staitech.anno.domain.vo.marking.out.MarkingSelectListVo;
import cn.staitech.anno.project.domain.DownTask;
import cn.staitech.anno.project.service.DownTaskService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.common.core.domain.R;

import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
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
    private DownTaskService downTaskService;

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取标注列表")
    @GetMapping("/intelligentEvaluation/selectListBy")
    public R<List<MarkingSelectListVo>> selectListBy(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) throws Exception {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail("参数异常");
        }
        return R.ok(markingService.selectList(slideId));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取GeoJson数据")
    @GetMapping("/intelligentEvaluation/selectLists")
    public R<List<Features>> selectLists(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) throws Exception {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail("参数异常");
        }
        return R.ok(markingService.selectLists(slideId));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "添加标注")
    @PostMapping("/intelligentAnno/insert")
    public R<Long> add(@Validated @RequestBody viewAddIn req) throws Exception {
        Long markingId = markingService.insert(req);
        return R.ok(markingId, ResponseConstant.OPERATE_SUCCEED);
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "删除标注")
    @ApiImplicitParams({@ApiImplicitParam(name = "markingId", value = "标注id", required = true, dataType = "Long", paramType = "query")})
    @DeleteMapping("/intelligentAnno/delete")
    public R<String> del(@RequestParam(value = "marking_id") @ApiParam(name = "marking_id", value = "标注id", required = true) Long marking_id) throws Exception {
        markingService.delete(marking_id);
        return R.ok("操作成功");
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "更新标注")
    @PutMapping("/intelligentAnno/update")
    public R<Long> update(@Validated @RequestBody MarkingUpdateIn req) throws Exception {
        markingService.update(req);
        return R.ok(req.getMarking_id(), ResponseConstant.OPERATE_SUCCEED);
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "websocket接口", hidden = true)
    @GetMapping("/getWebsocketPort")
    public R<String> getWebsocketPort() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return R.fail(ResponseConstant.OPERATE_ERROR);
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String localAdd = request.getLocalAddr();
        return R.ok("ws://" + localAdd + ":" + 9999 + "/");
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "json导出", hidden = true)
    @GetMapping("/jsonExport")
    public R<String> getWebsocketPort(
            @RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId
    ) throws Exception {
        return R.ok(markingService.slideJsonExport(slideId));
    }

    @Log(title = "标注测量excel导出", businessType = BusinessType.EXPORT)
    @ApiOperation(value = "标注测量excel导出")
    @GetMapping("/export")
    public void export(
            @RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) throws Exception {
        markingService.execlExport(slideId);
    }

    @ApiOperation(value = "下载目录文件")
    @GetMapping("/downTaskByCode")
    public void downTaskByCode(@RequestParam("code") @ApiParam(name = "code", value = "下载任务编码", required = true) String code)throws Exception{

        markingService.downTaskByCode(code);
    }

}

