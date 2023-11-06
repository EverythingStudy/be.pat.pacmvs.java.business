package cn.staitech.anno.controller;

import cn.staitech.anno.domain.AlgorithmModel;
import cn.staitech.anno.service.AlgorithmModelService;
import cn.staitech.common.core.domain.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "算法模型", tags = "算法模型")
@RestController
@RequestMapping("/model")
@Slf4j
public class AlgorithmModelController {

    @Resource
    private AlgorithmModelService algorithmModelService;


//    @ApiOperationSupport(author = "zmj")
//    @ApiOperation(value = "病理病变列表")
//    @PostMapping("/list")
//    public R<List<AlgorithmModel>> getModelList() {
//        List<AlgorithmModel> algorithmModelList=algorithmModelService.selectByPrimaryKey();
//        return R.ok(algorithmModelList);
//    }



}
