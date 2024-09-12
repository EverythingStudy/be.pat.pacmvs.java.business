package cn.staitech.anno.controller;

import cn.staitech.anno.domain.image.Image;
import cn.staitech.anno.service.ImageService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 切片管理-预测图片
 *
 * @author wangfeng
 * @date 2023/09/10
 */
@Api(value = "预测图片", tags = "预测图片")
@RestController
@RequestMapping("/forecastImage")
@Slf4j
public class ForecastImageController extends BaseController {
    @Resource
    private ImageService imageService;

    /**
     * 单个切片详细信息 .
     */
    @SneakyThrows
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "单个切片", notes = "单个切片 - 王峰")
    @Log(title = "查询单个切片", menu = "切片管理", subMenu = "预测图片", businessType = BusinessType.QUERY)
    @GetMapping("/{imageId}")
    public R<Image> selectById(@PathVariable("imageId") @ApiParam(value = "图像ID") Long imageId) {
        Image image = imageService.selectById(imageId);
        return R.ok(image);
    }

}
