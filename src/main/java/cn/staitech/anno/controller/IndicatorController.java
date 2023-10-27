package cn.staitech.anno.controller;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.vo.indicator.*;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import io.swagger.annotations.*;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;


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
    @Resource
    private ProjectService projectService;
    @Resource
    private PathologicalIndicatorCategoryService pathologicalService;

    /**
     * 添加结构指标 2.0SAAS .
     */
    @SneakyThrows
    @ApiOperation(value = "添加结构指标", notes = "wangfeng")
    @Log(title = "添加结构指标", menu = "结构指标", subMenu = "结构指标", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<String> add(@Validated @RequestBody IndicatorAddVO req) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        Indicator indicator = new Indicator();
        indicator.setSpeciesId(req.getSpeciesId());
        indicator.setOrganId(req.getOrganId());

        // 查询结构指标是否存在
        List<Indicator> indicatorList = indicatorService.selectIndicator(indicator);
        if (!indicatorList.isEmpty()) {
            return R.fail(MessageSource.M("INDICATOR_EXIST"));
        }

        indicator.setIndicatorName(MapConstant.getOrgan(req.getSpeciesId().toString().concat(req.getOrganId().toString())));
        indicator.setIndicatorNameEn(MapConstant.getOrganEn(req.getSpeciesId().toString().concat(req.getOrganId().toString())));
        indicator.setNumber(indicator.getSpeciesId().toString().concat(indicator.getOrganId().toString()));
        indicator.setCreateBy(sysUser.getUserId());

        //添加结构指标
        indicatorService.insertIndicator(indicator);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
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
        PageMaster<Indicator> pageMaster = indicatorService.selectIndicatorList(indicator, indicatorListVO.getPageNum(), indicatorListVO.getPageSize());
        return R.ok(pageMaster);
    }

    /**
     * 根据指标id获取详细信息 .
     */
    @ApiOperation(value = "获取病例指标详情接口", notes = "ZMJ")
    @GetMapping(value = "/details")
    public R<IndicatorVO> getInfo(
            @RequestParam @ApiParam(name = "indicatorId", value = "病理指标id", required = true) Long indicatorId) {
        // 获取病理信息
        Indicator indicator = indicatorService.selectIndicatorsById(indicatorId);
        IndicatorVO indicatorVo = new IndicatorVO();
        if (indicator != null) {
            // 浅拷贝
            BeanUtils.copyProperties(indicator, indicatorVo);
        }
        //添加关联项目
        indicatorVo.setProjectVo(projectService.selectProjectInfo(indicatorId));
        return R.ok(indicatorVo);
    }


    /**
     * 病理指标删除接口 .
     */
    @ApiOperation(value = "病理指标删除接口", notes = "ZMJ")
    @Log(title = "病理指标删除接口", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.UPDATE)
    @PostMapping("/del")
    public R<String> delIndicator(@RequestBody IndicatorGetVO indicatorGetVO) {
        if (!Optional.ofNullable(indicatorGetVO.getIndicatorId()).isPresent()) {
            return R.fail(MessageSource.M("INDICATOR_ID_NOTNULL"));
        }
        Integer num = indicatorService.selectIndicatorCountInProject(indicatorGetVO.getIndicatorId().longValue());
        if (num > 0) {
            return R.fail(MessageSource.M("ALREADY_BOUND_NO_DEL"));
        }
        //删除标注类别
        PathologicalIndicatorCategory Pathological = PathologicalIndicatorCategory.builder().indicatorId(indicatorGetVO.getIndicatorId().longValue()).delFlag(1).build();
        pathologicalService.updateByPrimaryKeySelective(Pathological);
        ;
        //删除病理指标
        indicatorService.delIndicator(indicatorGetVO.getIndicatorId().longValue());
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }


    /**
     * 病理指标修改接口 .
     */
    @ApiOperation(value = "病理指标修改接口", notes = "ZMJ")
    @Log(title = "病理指标修改接口", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public R<Integer> edit(@Validated @RequestBody IndicatorReviseVO req) {
        // 和项目绑定的不能修改
        Integer num = indicatorService.selectIndicatorCountInProject(req.getIndicatorId().longValue());
        if (num > 0) {
            return R.fail(MessageSource.M("ALREADY_BOUND"));
        }

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Indicator indicator = new Indicator();
        indicator.setSpeciesId(req.getSpeciesId());
        indicator.setOrganId(req.getOrganId());

        // 查询结构指标是否存在
        List<Indicator> indicatorList = indicatorService.selectIndicator(indicator);
        if (!indicatorList.isEmpty()) {
            return R.fail(MessageSource.M("INDICATOR_EXIST"));
        }

        indicator.setIndicatorName(MapConstant.getOrgan(req.getSpeciesId().toString().concat(req.getOrganId().toString())));
        indicator.setIndicatorNameEn(MapConstant.getOrganEn(req.getSpeciesId().toString().concat(req.getOrganId().toString())));
        indicator.setNumber(indicator.getSpeciesId().toString().concat(indicator.getOrganId().toString()));
        indicator.setCreateBy(sysUser.getUserId());

        // 修改病理指标
        indicatorService.updateIndicator(req);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }


    @ApiOperation(value = "病理指标查重接口", notes = "ZMJ")
    @GetMapping("/check")
    public R<Integer> checkEdit(@RequestParam @ApiParam(name = "indicatorId", value = "病理指标id", required = true) Long indicatorId) {
        Integer num = indicatorService.selectIndicatorCountInProject(indicatorId);
        if (0 < num) {
            return R.fail(MessageSource.M("ALREADY_BOUND"));
        }
        return R.ok(1);
    }

    /**
     * 关联病理指标列表 2.0SAAS .
     */
    @ApiOperation(value = "关联病理指标列表", notes = "wangfeng")
    @GetMapping("/getIndicatorList")
    public R<List<Indicator>> getIndicatorList(@Validated @RequestParam Long speciesId) {
        clearPage();
        Indicator indicator = new Indicator();
        indicator.setSpeciesId(speciesId);
        List<Indicator> list = indicatorService.selectIndicatorInformation(indicator);
        return R.ok(list);
    }
}
