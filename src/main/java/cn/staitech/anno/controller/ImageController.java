package cn.staitech.anno.controller;

import cn.staitech.anno.constant.ImageConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.image.in.ImageBatchIdsVO;
import cn.staitech.anno.domain.image.in.ImageListVO;
import cn.staitech.anno.domain.image.in.ImageTopicBatchIdsVO;
import cn.staitech.anno.domain.image.in.ImageTopicVO;
import cn.staitech.anno.domain.image.out.ImageListOutVO;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 图像 信息操作处理
 *
 * @author wangfeng
 * @date 2023/06/01
 */
@Api(value = "切片列表（原图像管理）", tags = "切片列表（原图像管理）")
@RestController
@RequestMapping("/image")
@Slf4j
public class ImageController extends BaseController {
    @Resource
    private ImageService imageService;

    /**
     * 切片列表 .
     */
    // @RequiresPermissions("anno:image:list")
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "切片列表", notes = "切片列表 - 王峰")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    @Log(title = "查询切片列表", menu = "切片管理", subMenu = "切片列表", businessType = BusinessType.QUERY)
    @PostMapping("/list")
    public R<PageMaster<ImageListOutVO>> list(@Validated @RequestBody ImageListVO image) {
        PageMaster<ImageListOutVO> page = imageService.selectList(image);
        return R.ok(page);
    }

    /**
     * 单个切片详细信息 .
     */
    @SneakyThrows
    // @RequiresPermissions("anno:image:selectbyid")
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "单个切片", notes = "单个切片 - 王峰")
    @Log(title = "查询单个切片", menu = "切片管理", subMenu = "切片列表", businessType = BusinessType.QUERY)
    @GetMapping("/{imageId}")
    public R<Image> selectById(@PathVariable("imageId") @ApiParam(value = "图像ID") Long imageId) {
        Image image = imageService.selectById(imageId);
        return R.ok(image);
    }

    /**
     * 删除单个切片 .
     */
    @SneakyThrows
    // @RequiresPermissions("anno:image:delete")
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "删除", menu = "切片管理", subMenu = "切片管理", businessType = BusinessType.DELETE)
    @ApiOperation(value = "逻辑删除单个切片")
    @GetMapping("/deleteById/{imageId}")
    @Transactional
    public R deleteById(@PathVariable("imageId") @ApiParam(value = "图像ID") Long imageId) {

        int deleteImageById = imageService.updateDeleteFlagById(imageId);
        if (deleteImageById > 0) {
            return R.ok(ImageConstant.OPERATE_SUCCEED);
        }
        return R.fail(ImageConstant.IMAGE_USING_FORBID_DELETE);
    }


    /**
     * 删除（根据ID 批量删除）
     *
     * @param request 主键ID列表(不能为 null 以及 empty)
     */
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "删除切片", menu = "切片管理", subMenu = "切片列表", businessType = BusinessType.DELETE)
    @ApiOperation(value = "逻辑批量删除切片")
    @PostMapping("/deleteBatchIds")
    public R<List<Long>> updateDeleteFlagBatchIds(@Validated @RequestBody ImageBatchIdsVO request) {
        Long uid = SecurityUtils.getLoginUser().getUserid();
        request.setUpdateBy(uid);
        List<Long> data = imageService.updateDeleteFlagBatchIds(request);
        return R.ok(data, ImageConstant.OPERATE_SUCCEED);
    }

    /**
     * 批量添加专题
     *
     * @param request 主键ID列表(不能为 null 以及 empty)
     */
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "批量分专题", menu = "切片管理", subMenu = "切片列表", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "批量添加专题")
    @PostMapping("/updateBatchIds")
    public R updateBatchIds(@Validated @RequestBody ImageTopicBatchIdsVO request) {
        Long uid = SecurityUtils.getLoginUser().getUserid();
        request.setUpdateBy(uid);
        int result = imageService.updateBatchIds(request);
        if (result > 0) {
            return R.ok(ImageConstant.OPERATE_SUCCEED);
        }
        return R.fail(ImageConstant.OPERATE_ERROR);
    }

    /**
     * 单个图像添加专题 - 没有专题则添加新专题
     *
     * @param request 主键ID列表(不能为 null 以及 empty)
     */
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "批量分专题", menu = "切片管理", subMenu = "切片列表", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "单个图像添加专题")
    @PostMapping("/updateTopic")
    public R updateBatchIds(@Validated @RequestBody ImageTopicVO request) throws Exception {

        int result = imageService.updateTopic(request);
        if (result > 0) {
            return R.ok(ImageConstant.OPERATE_SUCCEED);
        }
        return R.fail(ImageConstant.OPERATE_ERROR);
    }
}
