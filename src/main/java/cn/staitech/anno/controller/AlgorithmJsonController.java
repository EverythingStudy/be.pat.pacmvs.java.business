package cn.staitech.anno.controller;


import cn.staitech.anno.domain.AlgorithmJson;
import cn.staitech.anno.domain.QuestionBank;
import cn.staitech.anno.domain.examineScore.SelectExaminationListVO;
import cn.staitech.anno.service.AlgorithmJsonService;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author gjt
 * @since 2023-10-17
 */
@Api(value = "算法考核", tags = "viewer页面")
@RestController
@RequestMapping("/algorithmJson")
public class AlgorithmJsonController {

    @Resource
    private AlgorithmJsonService algorithmJsonService;


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "查询切片下的算法json文件")
    @GetMapping("/selectList")
    public R<List<AlgorithmJson>> selectList(
            @NotNull(message = "参数异常,未传入切片id") @RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId) {
        QueryWrapper<AlgorithmJson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("slide_id",slideId).eq("selected_status","0").eq("del_flag","0");
        return R.ok(algorithmJsonService.list(queryWrapper));
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "考核比对")
    @GetMapping("/examineComparison")
    public void examineComparison(
            @NotNull(message = "参数异常,未传入id") @RequestParam(value = "algorithmJsonId") @ApiParam(name = "algorithmJsonId", value = "算法jsonId", required = true) Long algorithmJsonId) throws Exception {
        algorithmJsonService.examineComparison(algorithmJsonId);
    }


}

