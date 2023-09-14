package cn.staitech.anno.controller;

import cn.staitech.anno.domain.productseries.ProductSeries;
import cn.staitech.anno.service.ProductSeriesService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 品系
 *
 * @author wangfeng
 * @date 2023/09/10
 */
@Api(value = "品系", tags = "品系")
@RestController
@RequestMapping("/productseries")
@Slf4j
public class ProductSeriesController extends BaseController {
    @Resource
    private ProductSeriesService productSeriesService;

    /**
     * 品系列表 .
     */
    // @RequiresPermissions("anno:round:list")
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "品系列表", notes = "品系列表 - 王峰")
    @Log(title = "品系列表", menu = "品系", subMenu = "品系列表", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public R<List<ProductSeries>> list() throws ExecutionException, InterruptedException {
        List<ProductSeries> list = productSeriesService.list();
        return R.ok(list);
    }

}
