package cn.staitech.anno.controller;

import cn.staitech.anno.domain.BlurImage;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.image.ImageStatus;
import cn.staitech.anno.vo.image.in.*;
import cn.staitech.anno.vo.image.out.ImageListOutVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    /**
     * 切片状态列表 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "切片状态列表", notes = "切片状态列表")
    @Log(title = "切片状态列表", menu = "切片状态列表", subMenu = "切片状态列表", businessType = BusinessType.QUERY)
    @PostMapping("/status")
    public R<List<ImageStatus>> status() {
        return R.ok(imageService.status());
    }


    /**
     * 切片列表 - 原始切片 .
     */
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
     * 切片列表 - 原始切片 .
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "切片列表", notes = "切片列表 - 王峰")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    @Log(title = "查询切片列表", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.QUERY)
    @PostMapping("/eyeList")
    public R<PageMaster<ImageListOutVO>> eyeList(@Validated @RequestBody ImageListVO image) throws ExecutionException, InterruptedException {
        image.setBizType(7);
        PageMaster<ImageListOutVO> page = imageService.selectList(image);
        return R.ok(page);
    }


    /**
     * 单个切片详细信息 .
     */
    @SneakyThrows
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "单个切片", notes = "单个切片 - 王峰")
    @Log(title = "查询单个切片", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.QUERY)
    @GetMapping("/{imageId}")
    public R<Image> selectById(@PathVariable("imageId") @ApiParam(value = "图像ID") Long imageId) {
        Image image = imageService.selectById(imageId);
        return R.ok(image);
    }

    /**
     * 删除（根据ID 批量删除）
     *
     * @param request 主键ID列表(不能为 null 以及 empty)
     */
    @ApiOperationSupport(author = "wangfeng")
    @Log(title = "删除切片", menu = "切片管理", subMenu = "原始切片", businessType = BusinessType.DELETE)
    @ApiOperation(value = "批量删除切片-物理删除")
    @RequiresPermissions(value = {"section:ophthalmology:del", "section:ophthalmology:remove", "projectConfig:spliceImgConfig:batchDelete", "section:slices:del", "section:slices:remove"}, logical = Logical.OR)
    @PostMapping("/deleteBatchIds")
    public R<List<Long>> deleteBatchIds(@Validated @RequestBody ImageBatchIdsVO request) throws Exception {
        List<Long> data = imageService.deleteBatchIds(request);
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
        request.setUpdateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
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
    @RequiresPermissions(value = {"section:slices:edit", "section:forecast:edit", "section:ophthalmology:edit"}, logical = Logical.OR)
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
    
    @ApiOperation(value = "清晰度更正/清晰度还原")
    @RequiresPermissions(value = {"section:slices:correct", "section:slices:restore"}, logical = Logical.OR)
    @PostMapping("/clarityProcessing")
    public R clarityProcessing(@Validated @RequestBody ResultCorrectionIn req) {
    	imageService.clarityProcessing(req);
        return R.ok();
    }
    
    @ApiOperation(value = "切片预览（AI矩形区域轮廓）")
    @PostMapping("/imagePreview")
    public R<String> imagePreview(@Validated @RequestBody ImagePreviewIn req) {
    	BlurImage blurImage =imageService.imagePreview(req);
    	String dataStr = "";
    	if(null != blurImage){
    		String jsonPath = blurImage.getJsonPath();
    		try (BufferedReader br = new BufferedReader(new FileReader(jsonPath))) {
    			String line;
    			while ((line = br.readLine()) != null) {
//    				System.out.println(line); // 输出文件的每一行
    				dataStr = line;
    			}
    		} catch (IOException e) {
    			System.err.format("Error reading file: %s%n", e.getMessage());
    		}finally {
				
			}
    	}
        return R.ok(dataStr);
    }
}
