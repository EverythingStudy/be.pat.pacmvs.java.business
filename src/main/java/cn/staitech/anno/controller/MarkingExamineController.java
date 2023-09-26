package cn.staitech.anno.controller;


import cn.staitech.anno.constant.R.ResponseConstant;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.viewAddIn;
import cn.staitech.anno.service.MarkingExamineService;
import cn.staitech.common.core.domain.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *
 * @author gjt
 * @since 2023-09-25
 */
@Api(value = "标注考核viewer页面", tags = "标注考核viewer页面")
@RestController
@RequestMapping("/markingExamine")
public class MarkingExamineController {

    @Resource
    private MarkingExamineService markingExamineService;


//    @ApiOperationSupport(author = "gjt")
//    @ApiOperation(value = "获取GeoJson数据")
//    @GetMapping("/selectList")
//    public R<List<Features>> selectList(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) throws Exception {
//        if (!Optional.ofNullable(slideId).isPresent()) {
//            return R.fail("参数异常");
//        }
//        return R.ok(markingExamineService.selectLists(slideId));
//    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "添加标注")
    @PostMapping("/insert")
    public R<Long> add(@Validated @RequestBody viewAddIn req) throws Exception {
        Long markingId = markingExamineService.insert(req);
        return R.ok(markingId, ResponseConstant.OPERATE_SUCCEED);
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "删除标注")
    @ApiImplicitParams({@ApiImplicitParam(name = "markingId", value = "标注id", required = true, dataType = "Long", paramType = "query")})
    @DeleteMapping("/delete")
    public R<String> del(@RequestParam(value = "marking_id") @ApiParam(name = "marking_id", value = "标注id", required = true) Long marking_id) throws Exception {
        markingExamineService.delete(marking_id);
        return R.ok("操作成功");
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "更新标注")
    @PutMapping("/update")
    public R<Long> update(@Validated @RequestBody MarkingUpdateIn req) throws Exception {
        markingExamineService.update(req);
        return R.ok(req.getMarking_id(), ResponseConstant.OPERATE_SUCCEED);
    }




}

