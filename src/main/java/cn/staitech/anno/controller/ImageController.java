package cn.staitech.anno.controller;

import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.image.in.*;
import cn.staitech.anno.domain.image.out.ImageListOutVO;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
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
import java.util.concurrent.ExecutionException;

/**
 * 切片管理-切片信息表、原始切片
 *
 * @author wangfeng
 * @date 2023/09/10
 */
@Api(value = "切片信息表、原始切片", tags = "切片信息表、原始切片")
@RestController
@RequestMapping("/image")
@Slf4j
public class ImageController extends BaseController {
    @Resource
    private ImageService imageService;

    @Resource
    private SlideService slideService;

    /**
     * 切片列表 .
     * 原始切片
     * 57
     * 1
     * section:slices:query
     */
    //@RequiresPermissions("section:slices:query")
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "切片列表", notes = "切片列表 - 王峰")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    @Log(title = "查询切片列表", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.QUERY)
    @PostMapping("/list")
    public R<PageMaster<ImageListOutVO>> list(@Validated @RequestBody ImageListVO image) throws ExecutionException, InterruptedException {
        image.setBizType(1);
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
    @Log(title = "查询单个切片", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.QUERY)
    @GetMapping("/{imageId}")
    public R<Image> selectById(@PathVariable("imageId") @ApiParam(value = "图像ID") Long imageId) {
        Image image = imageService.selectById(imageId);
        return R.ok(image);
    }

    /**
     * 删除单个切片 .
     */
    @SneakyThrows
    @RequiresPermissions("section:slices:remove")
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "删除", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.DELETE)
    @ApiOperation(value = "逻辑删除单个切片")
    @GetMapping("/deleteById/{imageId}")
    @Transactional(rollbackFor = Exception.class)
    public R deleteById(@PathVariable("imageId") @ApiParam(value = "图像ID") Long imageId) {
        if (imageId > 0) {
            Slide slide = new Slide();
            slide.setImageId(imageId);
            // 查切片表中有没有绑定此图片
            if (slideService.selectImageExist(slide).size() > 0) {
                int deleteImageById = imageService.updateDeleteFlagById(imageId);
                if (deleteImageById > 0) {
                    return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
                }
            }
        }
        return R.fail(MessageSource.M("IMAGE_USING_FORBID_DELETE"));
    }


    /**
     * 删除（根据ID 批量删除）
     *
     * @param request 主键ID列表(不能为 null 以及 empty)
     */
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "删除切片", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.DELETE)
    @ApiOperation(value = "逻辑批量删除切片")
    @PostMapping("/deleteBatchIds")
    public R<List<Long>> updateDeleteFlagBatchIds(@Validated @RequestBody ImageBatchIdsVO request) {
        Long uid = SecurityUtils.getUserId();
        request.setUpdateBy(uid);
        List<Long> data = imageService.updateDeleteFlagBatchIds(request);
        return R.ok(data, MessageSource.M("OPERATE_SUCCEED"));
    }

    /**
     * 批量添加专题
     *
     * @param request 主键ID列表(不能为 null 以及 empty)
     */
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "批量分专题", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "批量添加专题")
    @PostMapping("/updateBatchIds")
    public R updateBatchIds(@Validated @RequestBody ImageTopicBatchIdsVO request) {
        Long uid = SecurityUtils.getUserId();
        request.setUpdateBy(uid);
        int result = imageService.updateBatchIds(request);
        if (result > 0) {
            return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
        }
        return R.fail(MessageSource.M("OPERATE_ERROR"));
    }

    /**
     * 单个图像添加专题 - 没有专题则添加新专题
     *
     * @param request 主键ID列表(不能为 null 以及 empty)
     */
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "批量分专题", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "单个图像添加专题")
    @PostMapping("/update")
    public R update(@Validated @RequestBody ImageUpdateVO request) throws Exception {

        int result = imageService.updateById(request);
        if (result > 0) {
            return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
        }
        return R.fail(MessageSource.M("OPERATE_ERROR"));
    }


    /**
     * 选片列表 .
     * <p>
     * 标注类项目：
     * 查询条件：切片编号、专题号、状态、项目ID
     * 回显字段：缩略图、专题号、切片编号、图片大小、机构、上传时间、添加状态
     * <p>
     * 评审轮次切片列表-选片：
     * 查询条件：切片编号、专题号、状态、项目ID、roundReviewId
     * 回显字段：缩略图、专题号、切片编号、轮次、图片大小、机构、上传时间、添加状态
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "切片列表", notes = "切片列表 - 王峰")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    @Log(title = "选片列表", menu = "项目管理", subMenu = "选片列表", businessType = BusinessType.QUERY)
    @PostMapping("/choiceList")
    public R<PageMaster<ImageListOutVO>> choiceList(@Validated @RequestBody ImageTopicVO image) throws ExecutionException, InterruptedException {
        PageMaster<ImageListOutVO> page = imageService.choiceList(image);
        return R.ok(page);
    }

}
