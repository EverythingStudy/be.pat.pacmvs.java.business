package cn.staitech.anno.controller;

import static cn.staitech.common.security.utils.SecurityUtils.isAdmin;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.indicator.IndicatorAddVO;
import cn.staitech.anno.vo.indicator.IndicatorGetVO;
import cn.staitech.anno.vo.indicator.IndicatorListVO;
import cn.staitech.anno.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.vo.indicator.IndicatorVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;

/**
 * @author wangfeng
 * @Date 2023/09/14 15:50
 * @desc 结构指标
 */
@Api(value = "结构指标接口", tags = "结构指标")
@RestController
@RequestMapping("/indicator")
public class IndicatorController extends BaseController {
	@Resource
	private IndicatorService indicatorService;

	/**
	 * 添加结构指标 2.0SAAS .
	 */
	@SneakyThrows
	@RequiresPermissions(value = {"project:pathology:define", "project:pathology:add"}, logical = Logical.OR)
	@ApiOperation(value = "添加结构指标", notes = "wangfeng")
	@Log(title = "添加结构指标", menu = "结构指标", subMenu = "结构指标", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	public R<String> add(@Validated @RequestBody IndicatorAddVO req) {
		return indicatorService.insertIndicator(req);
	}

	/**
	 * 获取结构指标列表 .
	 */
	@ApiOperation(value = "查询结构指标接口", notes = "wangfeng")
	@RequiresPermissions("project:pathology:query")
	@Log(title = "结构指标列表", menu = "结构指标", subMenu = "结构指标", businessType = BusinessType.QUERY)
	@PostMapping("/allIndicator")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
		@ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
	public R<PageMaster<Indicator>> list1(@RequestBody IndicatorListVO indicatorListVO) {
		Indicator indicator = new Indicator();
		BeanUtils.copyProperties(indicatorListVO, indicator);
		//20231107wd_机构
		indicator.setOrganizationId(indicatorListVO.getOrganizationId());

		if (indicatorListVO.getOrganizationId() == null || indicatorListVO.getOrganizationId() < 1) {
			if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
				indicator.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
			}
		}
		PageMaster<Indicator> pageMaster = indicatorService.selectIndicatorList(indicator, indicatorListVO.getPageNum(), indicatorListVO.getPageSize());
		return R.ok(pageMaster);
	}

	/**
	 * 根据指标id获取详细信息 .
	 */
	@ApiOperation(value = "获取病例指标详情接口", notes = "ZMJ")
	@GetMapping(value = "/details")
	public R<IndicatorVO> getInfo(@RequestParam @ApiParam(name = "indicatorId", value = "病理指标id", required = true) Long indicatorId) {
		return indicatorService.getInfo(indicatorId);
	}


	/**
	 * 病理指标删除接口 .
	 */
	@ApiOperation(value = "病理指标删除接口", notes = "ZMJ")
	@RequiresPermissions("project:pathology:remove")
	@Log(title = "病理指标删除接口", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.UPDATE)
	@PostMapping("/del")
	public R<String> delIndicator(@RequestBody IndicatorGetVO indicatorGetVO) {
		return indicatorService.delIndicator(indicatorGetVO);
	}


	/**
	 * 病理指标修改接口 .
	 */
	@ApiOperation(value = "病理指标修改接口", notes = "ZMJ")
	@RequiresPermissions("project:pathology:edit")
	@Log(title = "病理指标修改接口", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.UPDATE)
	@PutMapping("/edit")
	public R<Integer> edit(@Validated @RequestBody IndicatorReviseVO req) {
		return indicatorService.edit(req);
	}


	/**
	 * 关联病理指标列表 2.0SAAS .
	 */
	@ApiOperation(value = "关联病理指标列表", notes = "wangfeng")
	@GetMapping("/getIndicatorList")
	public R<List<Indicator>> getIndicatorList(@Validated @RequestParam String speciesId) {
		clearPage();
		Indicator indicator = new Indicator();
		indicator.setSpeciesId(speciesId);
		if (!isAdmin(SecurityUtils.getUserId())) {
			indicator.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
		}
		List<Indicator> list = indicatorService.selectIndicatorInformation(indicator);
		return R.ok(list);
	}

	@SneakyThrows
	@ApiOperation(value = "添加结构指标-New")
	@Log(title = "添加结构指标", menu = "结构指标", subMenu = "结构指标", businessType = BusinessType.INSERT)
	@PostMapping("/save")
	public R<String> save(@Validated @RequestBody IndicatorAddVO req) {
		return indicatorService.insertIndicator(req);
	}
}
