package cn.staitech.anno.controller;

import cn.staitech.anno.constant.PathologicalLogConstant;
import cn.staitech.anno.constant.R.ResponseConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.vo.*;
import cn.staitech.anno.domain.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.service.StructureService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private IndicatorService indicatorService;

    @Resource
    private StructureService structureService;

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
    @RequiresPermissions("special:pathology:tabadd")
    @Log(title = "配置标签-新增标签", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<String> add(@Validated @RequestBody PathologicalIndicatorCategoryVO vo) {
        // 查询Indicator信息
        Indicator indicator = indicatorService.selectIndicatorsById(vo.getIndicatorId());
        if (indicator == null) {
            return R.fail(PathologicalLogConstant.INDICATOR_ABSENT);
        }

        PathologicalIndicatorCategory category = new PathologicalIndicatorCategory();
        BeanUtils.copyProperties(vo, category);

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        // 机构ID
        category.setOrganizationId(sysUser.getOrganizationId());

        // 验证是否存在该条件的记录    A：必填项校验。B：结构编码在当前列表内不可重复；C：结构名称在当前列表内不可重复。D：图层顺序在当前列表内不可重复；E：颜色值在当前列表不可重复
        List<PathologicalIndicatorCategory> list = pathologicalIndicatorCategoryService.selectIndicatorMessage(category);
        if (list.size() > 0) {
            return R.fail(PathologicalLogConstant.CATEGORY_NAME_EXIST);
        }

        // 获取structureName
        String structureName = structureService.getById(vo.getStructureId()).getName();
        // 生成categoryName
        String categoryName = indicator.getIndicatorName() + structureName;
        category.setCategoryName(categoryName);
        // 生成完整编码
        category.setNumber(indicator.getNumber() + "" + vo.getStructureId());

        // 添加标注类别
        pathologicalIndicatorCategoryService.insertSelective(category);
        IndicatorReviseVO indicatorReviseVO = IndicatorReviseVO.builder().indicatorId(vo.getIndicatorId().intValue()).build();
        // 更新病理表数据
        indicatorService.updateIndicator(indicatorReviseVO);
        return R.ok(ResponseConstant.OPERATE_SUCCEED);
    }


    /**
     * 配置标签-标签列表 .
     */
    @ApiOperation(value = "获取标注类别列表接口", notes = "wangfeng")
    @RequiresPermissions("special:pathology:tablist")
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
     * 配置标签-标签列表 .
     */
    @ApiOperation(value = "根据项目查询结构指标列表", notes = "gjt")
    @Log(title = "配置标签-标签列表", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.QUERY)
    @GetMapping("/selectList")
    public R<List<PathologicalIndicatorCategory>> selectList(@RequestParam(value = "projectId") @ApiParam(name = "projectId", value = "项目id", required = true) Long projectId) {
        //获取病理指标下的标注类别
        List<PathologicalIndicatorCategory> categoryList = pathologicalIndicatorCategoryService.selectprojectList(projectId);
        return R.ok(categoryList);
    }


    /**
     * 标签详细 .
     */
    @ApiOperation(value = "获取标签详情接口", notes = "wangfeng")
    @GetMapping(value = "/details")
    public R<PathologicalIndicatorCategory> getInfo(
            @RequestParam @ApiParam(name = "categoryId", value = "标注类别id", required = true) Long categoryId) {
        //根据标注id获取标注类别详情
        PathologicalIndicatorCategory categoryList = pathologicalIndicatorCategoryService.selectByPrimaryKey(
                categoryId);
        return R.ok(categoryList);
    }

    /**
     * 配置标签-编辑 .
     */
    @ApiOperation(value = "标注类别修改接口", notes = "wangfeng")
    @RequiresPermissions("special:pathology:tabedit")
    @Log(title = "配置标签-编辑", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public R<String> edit(@Validated @RequestBody PathologicalIndicatorCategory category) {
        if (category.getCategoryId() == null || category.getIndicatorId() == null) {
            return R.fail(PathologicalLogConstant.MISSING_REQUIRED_VALUE);
        }

        // 查询Indicator信息
        Indicator indicator = indicatorService.selectIndicatorsById(category.getIndicatorId());
        if (indicator == null) {
            return R.fail(PathologicalLogConstant.INDICATOR_ABSENT);
        }


        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        // 机构ID
        category.setOrganizationId(sysUser.getOrganizationId());

        // 获取structureName
        String structureName = structureService.getById(category.getStructureId()).getName();
        // 生成categoryName
        String categoryName = indicator.getIndicatorName() + structureName;
        category.setCategoryName(categoryName);
        // 生成完整编码
        category.setNumber(indicator.getNumber() + "" + category.getStructureId());


        // 验证是否存在该条件的记录    A：必填项校验。B：结构编码在当前列表内不可重复；C：结构名称在当前列表内不可重复。D：图层顺序在当前列表内不可重复；E：颜色值在当前列表不可重复
        List<PathologicalIndicatorCategory> list = pathologicalIndicatorCategoryService.selectIndicatorMessage(category);
        if (list.size() > 0) {
            return R.fail(PathologicalLogConstant.CATEGORY_NAME_EXIST);
        }


        category.setUpdateBy(SecurityUtils.getUserId());


        //修改标注类别信息
        pathologicalIndicatorCategoryService.updateByPrimaryKeySelective(category);
        return R.ok(ResponseConstant.OPERATE_SUCCEED);
    }

    /**
     * 配置标签-删除 .
     */
    @ApiOperation(value = "标签删除接口", notes = "ZMJ")
    @RequiresPermissions("special:pathology:tabremove")
    @Log(title = "配置标签-删除", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.DELETE)
    @PostMapping("/del")
    public R<String> del(@RequestBody CategoryVO categoryVO) {
        //查询标注是否关联标签
        Integer num = pathologicalIndicatorCategoryService.selectLabelNum(categoryVO.getCategoryId());
        if (0 < num) {
            return R.fail(PathologicalLogConstant.USED);
        }
        //查询标签数据
        PathologicalIndicatorCategory category = pathologicalIndicatorCategoryService.selectCategoryAll(categoryVO.getCategoryId());
        PathologicalIndicatorCategory Pathological = PathologicalIndicatorCategory.builder().categoryId(categoryVO.getCategoryId()).delFlag(1).build();
        //删除标注类别
        pathologicalIndicatorCategoryService.updateByPrimaryKeySelective(Pathological);
        IndicatorReviseVO indicatorReviseVO = IndicatorReviseVO.builder().indicatorId(category.getIndicatorId().intValue()).build();
        //更新病理表数据
        indicatorService.updateIndicator(indicatorReviseVO);
        return R.ok(null, ResponseConstant.OPERATE_SUCCEED);
    }


    /**
     * 验证标签名称、颜色、图层顺序、是否存在、是否关联切片2.0
     * String checkCategory = checkCategory(category);
     * if (!checkCategory.equals("1")) {
     * return R.fail(checkCategory);
     * }
     */
    public String checkCategory(PathologicalIndicatorCategory annotationCategory) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        // 机构ID
        annotationCategory.setOrganizationId(sysUser.getOrganizationId());

        PathologicalIndicatorCategory category = PathologicalIndicatorCategory.builder().categoryId(annotationCategory.getCategoryId())
                .categoryName(annotationCategory.getCategoryName()).indicatorId(annotationCategory.getIndicatorId()).build();
        //获取病理下的标注类别名称是否存在
        List<PathologicalIndicatorCategory> categoryList = pathologicalIndicatorCategoryService.selectIndicatorMessage(
                category);
        category.setCategoryName(null);
        category.setHex(annotationCategory.getHex());
        //查询病理下的标注类别颜色是否存在
        List<PathologicalIndicatorCategory> categories = pathologicalIndicatorCategoryService.selectIndicatorMessage(
                category);
        category.setHex(null);
        category.setOrderNumber(annotationCategory.getOrderNumber());
        //查询图层顺序是否已存在
        List<PathologicalIndicatorCategory> orderNumber = pathologicalIndicatorCategoryService.selectIndicatorMessage(
                category);
        if (!categoryList.isEmpty()) {
            return PathologicalLogConstant.CATEGORY_NAME_EXIST;
        } else if (!categories.isEmpty()) {
            return PathologicalLogConstant.COLOR_NAME_EXIST;
        } else if (!orderNumber.isEmpty()) {
            return PathologicalLogConstant.LAYER_ALREADY_EXISTS;
        } else {
            return PathologicalLogConstant.ONE;
        }
    }
}
