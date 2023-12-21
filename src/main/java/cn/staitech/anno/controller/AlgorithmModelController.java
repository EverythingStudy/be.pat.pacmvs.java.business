package cn.staitech.anno.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.staitech.anno.domain.AlgorithmModel;
import cn.staitech.anno.service.AlgorithmModelService;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value = "算法模型", tags = "算法模型")
@RestController
@RequestMapping("/model")
@Slf4j
public class AlgorithmModelController {

    @Resource
    private AlgorithmModelService algorithmModelService;


    @ApiOperation(value = "算法模型列表")
    @PostMapping("/list")
    public R<List<AlgorithmModel>> getModelList() {
        List<AlgorithmModel> algorithmModelList=algorithmModelService.list();
        return R.ok(algorithmModelList);
    }
}
