package cn.staitech.anno.controller;

import cn.staitech.anno.constant.ColorConstant;
import cn.staitech.anno.domain.color.Color;
import cn.staitech.anno.service.ColorService;
import cn.staitech.common.core.domain.R;
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
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author: wangfeng
 * @create: 2023-09-13 18:07:38
 * @Description: 颜色
 */
@Api(value = "颜色", tags = "颜色")
@RestController
@RequestMapping("/color")
@Slf4j
public class ColorController {

    @Resource
    private ColorService colorService;

    /**
     * 颜色类型列表 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "颜色列表", notes = "颜色列表 - 王峰")
    @Log(title = "颜色列表", menu = "颜色列表", subMenu = "颜色列表", businessType = BusinessType.QUERY)
    @GetMapping("/colorType")
    public R<Map<Integer, String>> colorType() {
        Map<Integer, String> map = ColorConstant.COLOR_TYPE;
        return R.ok(map);
    }

    /**
     * 颜色列表 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "颜色类型", notes = "颜色类型 - 王峰")
    @Log(title = "颜色类型", menu = "颜色类型", subMenu = "颜色类型", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public R<List<Color>> list() throws ExecutionException, InterruptedException {
        List<Color> list = colorService.list();
        return R.ok(list);
    }
}
