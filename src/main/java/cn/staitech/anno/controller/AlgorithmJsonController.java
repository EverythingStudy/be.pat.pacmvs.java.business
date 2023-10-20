package cn.staitech.anno.controller;


import cn.staitech.anno.domain.AlgorithmJson;
import cn.staitech.anno.domain.algorithmJson.in.SelectGeoJson;
import cn.staitech.anno.domain.algorithmJson.out.SelectGeoJsonList;
import cn.staitech.anno.service.AlgorithmJsonService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.common.core.domain.R;
import cn.staitech.system.api.RemoteLabelService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author gjt
 * @since 2023-10-17
 */
@Api(value = "算法考核-viewer页面", tags = "算法考核-viewer页面")
@RestController
@RequestMapping("/algorithmJson")
public class AlgorithmJsonController {

    @Resource
    private AlgorithmJsonService algorithmJsonService;


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取标注数据")
    @PostMapping("/getGeoJson")
    public R<JSONObject> getGeoJson(@RequestBody SelectGeoJson selectGeoJson)throws Exception {
        return R.ok(algorithmJsonService.getGeoJson(selectGeoJson));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "查询切片下的算法json文件（下拉框）")
    @GetMapping("/selectList")
    public R<List<AlgorithmJson>> selectList(
            @RequestParam(value = "algorithmAssessmentId") @ApiParam(name = "algorithmAssessmentId", value = "算法考核id", required = true) Long algorithmAssessmentId) {
        QueryWrapper<AlgorithmJson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("algorithm_assessment_id",algorithmAssessmentId).eq("selected_status","0").eq("del_flag","0").eq("json_type","1");
        return R.ok(algorithmJsonService.list(queryWrapper));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "对比json列表")
    @GetMapping("/contrastJsonList")
    public R<List<AlgorithmJson>> contrastJsonList(
            @RequestParam(value = "algorithmAssessmentId") @ApiParam(name = "algorithmAssessmentId", value = "算法考核id", required = true) Long algorithmAssessmentId) {
        QueryWrapper<AlgorithmJson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("algorithm_assessment_id",algorithmAssessmentId).eq("del_flag","0");
        return R.ok(algorithmJsonService.list(queryWrapper));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "考核比对")
    @PostMapping("/examineComparison")
    public R<String> examineComparison(
            @RequestParam(value = "algorithmJsonId") @ApiParam(name = "algorithmJsonId", value = "算法jsonId", required = true) Long algorithmJsonId) throws Exception {
        algorithmJsonService.examineComparison(algorithmJsonId);
        return R.ok(MessageSource.M("OPERATE_SUCCEED"));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "查询切片下标签和用户")
    @PostMapping("/selectUserAndLabelList")
    public R<SelectGeoJsonList> selectUserAndLabelList(@RequestBody SelectGeoJson selectGeoJson) throws Exception {
        return R.ok(algorithmJsonService.selectUserAndLabelList(selectGeoJson));
    }




}

