package cn.staitech.anno.controller;

import cn.staitech.anno.domain.AlgorithmProjectType;
import cn.staitech.anno.service.AlgorithmProjectTypeService;
import cn.staitech.common.core.domain.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gjt
 * @since 2023-10-26
 */
@Api(value = "算法考核-算法考核页面", tags = "算法考核-算法考核页面")
@RestController
@RequestMapping("/algorithmProjectType")
public class AlgorithmProjectTypeController {

    @Resource
    private AlgorithmProjectTypeService algorithmProjectTypeService;


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取数据列表")
    @GetMapping("/selectList")
    public R<List<AlgorithmProjectType>> selectList() throws Exception {
        return R.ok(algorithmProjectTypeService.list());
    }

}

