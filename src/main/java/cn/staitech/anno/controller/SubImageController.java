package cn.staitech.anno.controller;

import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.vo.image.SubImageVo;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.service.SubImageService;
import cn.staitech.anno.utils.LocaleMessageSourceUtil;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 图像 信息操作处理 .
 *
 * @author staitech
 */
@Api(value = "组内切片图像管理模块接口", tags = "项目配置")
@RestController
@RequestMapping("/subImage")
@Slf4j
public class SubImageController extends BaseController {

    @Resource
    private SubImageService subImageService;
    @Resource
    private SlideService slideService;

    @ApiOperation(value = "查询切片操作")
    @PostMapping("/getSubImageByPage")
    public R<PageMaster<SubImageVo>> getSubImageByPage(@RequestBody Map params) {
        return subImageService.pageSubImage(params);
    }

    @ApiOperation(value = "查询切片列表")
    @PostMapping("/querySubImageByGroup")
    public R<List<SubImageVo>> querySubImageByGroup(@RequestBody Map params) {
        return subImageService.querySubImageByGroup(params);
    }

    @ApiOperation(value = "查询切片详情")
    @GetMapping("/{imageId}")
    public R<SubImage> getSubImageByImageId(@PathVariable("imageId") @ApiParam(value = "图像ID") Long imageId) {
        SubImage subImage = subImageService.getById(imageId);
        return R.ok(subImage);
    }

    @ApiOperation(value = "查询切片详情")
    @GetMapping("getSubImageBySlideId/{slideId}")
    public R<SubImage> getSubImageBySlideId(@PathVariable("slideId") @ApiParam(value = "切片ID") Long slideId) {
        Slide slide = slideService.selectById(slideId);
        SubImage subImage = null;
        if (slide!=null){
            subImage = subImageService.getById(slide.getImageId());
        }
        return R.ok(subImage);
    }


    @Log(title = "专题配置",menu = "专题配置",subMenu = "项目配置",businessType = BusinessType.INSERT)
    @ApiOperation(value = "添加切片操作")
    @PostMapping("/add")
    public R addSubImage(@RequestBody SubImage subImage){
        boolean flag = subImageService.saveOrUpdate(subImage);
        R r = R.fail(subImage);
        if (flag){
            r = R.ok(subImage);
        }
        return r;
    }


    @GetMapping("/i18n")
    public String i18n(){
        //String welcome = messageSourceUtil.getMessage("welcome");
        String welcome = LocaleMessageSourceUtil.getMessage("welcome");
        System.out.println(welcome);
        return welcome;
    }
}