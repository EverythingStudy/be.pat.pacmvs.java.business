package cn.staitech.anno.controller;

import cn.staitech.anno.service.MarkingExamineService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.in.UpdateOperationIn;
import cn.staitech.anno.vo.geojson.out.BatchResult;
import cn.staitech.anno.vo.marking.MarkingExamineInsertVO;
import cn.staitech.anno.vo.marking.MarkingExamineList;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.uuid.UUID;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gjt
 * @since 2023-09-25
 */
@Api(value = "标注考核viewer页面", tags = "标注考核viewer页面")
@RestController
@RequestMapping("/markingExamine")
public class MarkingExamineController {

    @Resource
    private MarkingExamineService markingExamineService;


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取GeoJson数据")
    @GetMapping("/selectList")
    public R<List<Features>> selectList(@RequestParam(value = "questionProjectId") @ApiParam(name = "questionProjectId", value = "题库项目", required = true) Long questionProjectId, @RequestParam(value = "createBy") @ApiParam(name = "createBy", value = "答题者", required = true) Long createBy) throws Exception {
        return R.ok(markingExamineService.selectLists(questionProjectId, createBy));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取切片原始GeoJson数据")
    @GetMapping("/selectQuestionMarkingList")
    public R<JSONArray> selectQuestionMarkingList(@RequestParam(value = "questionId") @ApiParam(name = "questionId", value = "题库id", required = true) Long questionId) throws Exception {
        return R.ok(markingExamineService.selectQuestionMarkingList(questionId));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "添加标注")
    @PostMapping("/insert")
    public R<Long> add(@Validated @RequestBody MarkingExamineInsertVO req) throws Exception {
        req.setTraceId(UUID.fastUUID().toString());
        req.setIsBatch(false);
        Long markingId = markingExamineService.insert(req);
        return R.ok(markingId, MessageSource.M("OPERATE_SUCCEED"));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "删除标注")
    @ApiImplicitParams({@ApiImplicitParam(name = "markingId", value = "标注id", required = true, dataType = "Long", paramType = "query")})
    @DeleteMapping("/delete")
    public R<String> del(@RequestParam(value = "marking_id") @ApiParam(name = "marking_id", value = "标注id", required = true) Long marking_id) throws Exception {
        markingExamineService.delete(marking_id);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "更新标注")
    @PutMapping("/update")
    public R<Long> update(@Validated @RequestBody MarkingExamineInsertVO req) throws Exception {
        markingExamineService.update(req);
        return R.ok(req.getMarking_id(), MessageSource.M("OPERATE_SUCCEED"));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "合并、裁剪轮廓")
    @PutMapping("/updateOperation")
    public R<JSONObject> updateOperation(@Validated @RequestBody UpdateOperationIn req) throws Exception {
        JSONObject geoJson = markingExamineService.updateOperation(req);
        return R.ok(geoJson, MessageSource.M("OPERATE_SUCCEED"));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "合并、裁剪轮廓校验")
    @PutMapping("/operationCheck")
    public R<Double> operationCheck(@Validated @RequestBody UpdateOperationIn req) throws Exception {
        double percentage = markingExamineService.operationCheck(req);
        return R.ok(percentage, MessageSource.M("OPERATE_SUCCEED"));
    }

    /**
     * 批量操作
     *
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "批量操作")
    @PostMapping("/batch")
    public R<List<BatchResult>> batch(@Validated @RequestBody MarkingExamineList list) {
        if (CollectionUtils.isEmpty(list.getList())) {
            return R.fail(MessageSource.M("ARGUMENT_INVALID"));
        }
        List<BatchResult> result = markingExamineService.batch(list.getList());
        return R.ok(result, MessageSource.M("OPERATE_SUCCEED"));
    }
}
