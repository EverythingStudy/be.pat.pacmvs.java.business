package cn.staitech.anno.controller;

import cn.staitech.anno.domain.image.in.ImageBatchIdsVO;
import cn.staitech.anno.domain.image.in.ImageListFindIn;
import cn.staitech.anno.domain.image.in.ImageUpdateVO;
import cn.staitech.anno.domain.image.out.ImageListFindOut;
import cn.staitech.anno.service.ImageService;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * @author lif
 */
@Api(value = "切片管理", tags = "切片管理")
@RestController
@RequestMapping("/study")
public class ImageController {

    @Resource
    private ImageService imageService;

    /**
     * 获取原始切片的分页列表
     *
     * @param findIn 包含用于查找切片的搜索条件
     * @return 包含原始切片列表的分页响应
     */
    @ApiOperation(value = "原始切片列表", notes = "原始切片列表")
    @Log(title = "查询切片列表", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.QUERY)
    @PostMapping("/pageList")
    public R<PageResponse<ImageListFindOut>> getStudyPageList(@RequestBody ImageListFindIn findIn) throws ParseException {
        return R.ok(imageService.findImageList(findIn));
    }

    /**
     * 批量删除切片
     *
     * @param request 包含要删除的切片ID列表
     * @return 包含已删除切片ID列表的响应
     * @throws Exception 如果删除过程中发生错误
     */
    @Log(title = "删除切片", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.DELETE)
    @ApiOperation(value = "批量删除切片-物理删除")
    @RequiresPermissions(value = {"section:ophthalmology:del", "section:ophthalmology:remove", "projectConfig:spliceImgConfig:batchDelete", "section:slices:del", "section:slices:remove"}, logical = Logical.OR)
    @PostMapping("/deleteBatchIds")
    public R<List<Long>> deleteBatchIds(@Validated @RequestBody ImageBatchIdsVO request) throws Exception {
        return R.ok();
    }

    /**
     * 更新切片信息
     *
     * @param request 包含要更新的切片信息
     * @return 表示更新操作结果的响应
     * @throws Exception 如果更新过程中发生错误
     */
    @Log(title = "编辑切片信息", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "编辑切片信息")
    @RequiresPermissions(value = {"section:slices:edit", "section:forecast:edit", "section:ophthalmology:edit"}, logical = Logical.OR)
    @PostMapping("/update")
    public R update(@Validated @RequestBody ImageUpdateVO request) throws Exception {
        return R.ok();
    }

}
