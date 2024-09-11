package cn.staitech.anno.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;

import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.annotation.CategoryVO;
import cn.staitech.anno.vo.annotation.LabelListVO;
import cn.staitech.anno.vo.annotation.LabelVO;
import cn.staitech.anno.vo.indicator.PathologicalIndicatorCategoryVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangfeng .
 * @Date 2023/9/14 17:28
 * @desc 配置标签
 */
@Slf4j
@Api(tags = "标签接口")
@RestController
@RequestMapping("/category")
public class PathologicalController {
	@Resource
	private PathologicalIndicatorCategoryService pathologicalIndicatorCategoryService;


	/**
	 * 配置标签-新增标签.
	 * 新增标签接口：[POST]	/anno/category/add
	 * 5个参数
	 * 结构编号	structureId
	 * RGB	rgb
	 * HEX	hex
	 * 图层顺序 orderNumber
	 * 结构指标ID	indicatorId
	 */
	@ApiOperation(value = "标签添加接口", notes = "wangfeng")
	@RequiresPermissions(value = {"project:pathology:tabadd", "project:pathology:tabdefine"}, logical = Logical.OR)
	@Log(title = "配置标签-新增标签", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	public R<String> add(@Validated @RequestBody PathologicalIndicatorCategoryVO vo) {
		return pathologicalIndicatorCategoryService.add(vo);
	}


	/**
	 * 配置标签-标签列表 .
	 */
	@ApiOperation(value = "获取标注类别列表接口", notes = "wangfeng")
	@RequiresPermissions("project:pathology:tablist")
	@Log(title = "配置标签-标签列表", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.QUERY)
	@PostMapping("/all")
	public R<PageMaster<LabelListVO>> list(@RequestBody LabelVO labelVO) {
		PageHelper.startPage(labelVO.getPageNum(), labelVO.getPageSize()).setReasonable(true);
		//获取病理指标下的标注类别
		List<LabelListVO> categoryList = pathologicalIndicatorCategoryService.selectByIndicator(labelVO);
		PageMaster<LabelListVO> pageMaster = new PageMaster<>(categoryList);
		return R.ok(pageMaster);
	}


	/**
	 * 标签详细 .
	 */
	@ApiOperation(value = "获取标签详情接口", notes = "wangfeng")
	@GetMapping(value = "/details")
	public R<PathologicalIndicatorCategory> getInfo(
			@RequestParam @ApiParam(name = "categoryId", value = "标注类别id", required = true) Long categoryId) {
		return pathologicalIndicatorCategoryService.getInfo(categoryId);
	}

	/**
	 * 配置标签-编辑 .
	 */
	@ApiOperation(value = "标注类别修改接口", notes = "wangfeng")
	@RequiresPermissions("project:pathology:tabedit")
	@Log(title = "配置标签-编辑", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	public R<String> edit(@Validated @RequestBody PathologicalIndicatorCategory category) {
		return pathologicalIndicatorCategoryService.edit(category);
	}


	/**
	 * 配置标签-删除 .
	 */
	@ApiOperation(value = "标签删除接口", notes = "ZMJ")
	@PostMapping("/del")
	public R<String> del(@RequestBody CategoryVO categoryVO) {
		return pathologicalIndicatorCategoryService.del(categoryVO);
	}


}
