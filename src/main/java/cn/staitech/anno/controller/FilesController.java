package cn.staitech.anno.controller;

import cn.staitech.anno.constant.ImageConstant;
import cn.staitech.anno.domain.files.Files;
import cn.staitech.anno.domain.files.in.FilesListVO;
import cn.staitech.anno.service.FilesService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 切片信息管理-切片信息信息表、原始切片信息
 *
 * @author wangfeng
 * @date 2023/09/10
 */
@Api(value = "切片信息信息表、原始切片信息", tags = "切片信息信息表、原始切片信息")
@RestController
@RequestMapping("/files")
@Slf4j
public class FilesController extends BaseController {
    @Resource
    private FilesService filesService;

    /**
     * 切片信息列表 .
     */
    // @RequiresPermissions("anno:files:list")
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "文件列表", notes = "文件列表 - 王峰")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    @Log(title = "查询文件列表", menu = "文件管理", subMenu = "查询文件列表", businessType = BusinessType.QUERY)
    @PostMapping("/list")
    public R<PageMaster<Files>> list(@Validated @RequestBody FilesListVO files) throws ExecutionException, InterruptedException {
        PageMaster<Files> page = filesService.selectList(files);
        return R.ok(page);
    }

    /**
     * 单个文件详细信息 .
     */
    @SneakyThrows
    // @RequiresPermissions("anno:files:selectbyid")
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "单个文件详情", notes = "单个文件详情 - 王峰")
    @Log(title = "查询单个文件详情", menu = "单个文件详情", subMenu = "文件信息", businessType = BusinessType.QUERY)
    @GetMapping("/{imageId}")
    public R<Files> selectById(@PathVariable("fileId") @ApiParam(value = "图像ID") Long fileId) {
        Files files = filesService.getById(fileId);
        return R.ok(files);
    }

    /**
     * 删除单个文件 .
     */
    @SneakyThrows
    // @RequiresPermissions("anno:files:delete")
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "删除单个文件记录", menu = "删除单个文件记录", subMenu = "删除单个文件记录", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除单个文件记录")
    @GetMapping("/deleteById/{imageId}")
    @Transactional
    public R deleteById(@PathVariable("fileId") @ApiParam(value = "图像ID") Long fileId) {
        Files files = new Files();
        files.setFilesId(fileId);
        if (filesService.updateById(files)) {
            return R.ok(ImageConstant.OPERATE_SUCCEED);
        }
        return R.fail(ImageConstant.IMAGE_USING_FORBID_DELETE);
    }

    /**
     * 编辑单个文件 .
     *
     * @param files
     * @return
     * @throws Exception
     */
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "编辑单个文件", menu = "编辑单个文件", subMenu = "编辑单个文件", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "编辑单个文件")
    @PostMapping("/update")
    public R updateBatchIds(@Validated @RequestBody Files files) {
        if (filesService.save(files)) {
            return R.ok(ImageConstant.OPERATE_SUCCEED);
        }
        return R.fail(ImageConstant.OPERATE_ERROR);
    }
}
