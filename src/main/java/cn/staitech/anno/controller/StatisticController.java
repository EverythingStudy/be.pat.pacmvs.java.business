package cn.staitech.anno.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.staitech.anno.constant.R.StatisticResponseConstant;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.vo.AnnotationBroadcastVO;
import cn.staitech.anno.domain.vo.statistic.*;
import cn.staitech.anno.service.*;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.utils.StatisticListUtils;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.staitech.anno.constant.ProjectConstant.NO_ATTRIBUTE;
import static cn.staitech.anno.constant.R.StatisticResponseConstant.MAX_SELECT_TIME_ERROR;
import static cn.staitech.anno.constant.R.StatisticResponseConstant.SELECT_TIME_ERROR;
import static cn.staitech.anno.constant.StatisticConstant.*;
import static cn.staitech.anno.utils.StatisticListUtils.exportExcelDateUtil;
import static cn.staitech.anno.utils.StatisticListUtils.exportExcelUtil;

/**
 * 数据统计处理
 *
 * @author zhaoshaoning
 */
@Api(value = "数据统计接口", tags = "数据统计模块")
@RestController
@Slf4j
@RequestMapping("statistic")
public class StatisticController extends BaseController {
    @Resource
    private StatisticService statisticService;

    @Resource
    private ProjectService projectService;

    @Resource
    private IndicatorService indicatorService;

    @Resource
    private SlideService slideService;

    @Resource
    private PathologicalIndicatorCategoryService pathologicalIndicatorCategoryService;

    @Resource
    private AnnotationService annotationService;

    /**
     * 综合统计列表页面接口
     *
     * @param statisticList
     * @return
     * @throws ParseException
     */
    @RequiresPermissions("anno:statistic")
    @ApiOperation(value = "综合统计列表/细分筛选查询")
    @PostMapping("/statisticList")
    public R<StatisticListOutVO> statisticList(@Valid @RequestBody StatisticListInVO statisticList) throws ParseException {
        // 创建返回结果实例
        StatisticListOutVO statisticListRep = new StatisticListOutVO();
        List<StatisticObjectOutVO> resp = new ArrayList<>();
        StatisticListUtils statisticListUtils = new StatisticListUtils();
        // 通过SysDictData获取统计维度值、统计数量类别
        String displayQuantity =  statisticService.statisticSelectDictDataById(statisticList.getStatisticCategory()).getDictLabel();
        String statisticalDimension = statisticService.statisticSelectDictDataById(statisticList.getStatisticDimension()).getDictLabel();
        // 数量（横轴）--标注数量
        if (displayQuantity.equals(ANNOTATION_COUNT)) {
            // 统计维度（竖轴）--标注日期
            if (statisticalDimension.equals(ANNOTATION_DATE)) {
                // 判断startTime、endTime值，并返回日期差
                long daysBetween = statisticListUtils.statisticSetStartEndTime(statisticList, statisticService);
                // 按日期差进行分类查询
                if (daysBetween < THREE_YEAR) {
                    statisticListRep = statisticListUtils.statisticAnnoDateRespOut(statisticList, statisticListRep, resp, displayQuantity, statisticalDimension, daysBetween, statisticService);
                    return R.ok(statisticListRep);
                } else if (daysBetween >= THREE_YEAR) {
                    return R.fail(MAX_SELECT_TIME_ERROR);
                } else {
                    return R.fail(SELECT_TIME_ERROR);
                }
            } else {
                // 统计维度（竖轴）--除标注日期以外的
                statisticListRep = statisticListUtils.statisticAnnoRespOut(statisticList, statisticListRep, resp, displayQuantity, statisticalDimension, statisticService);
                return R.ok(statisticListRep);
            }
        }
        // 数量（横轴）--图像数量
        else if (displayQuantity.equals(SLIDE_COUNT)) {
            // 统计维度（竖轴）--标注日期
            if (statisticalDimension.equals(ANNOTATION_DATE)) {
                // 判断startTime、endTime值，并返回日期差
                long daysBetween = statisticListUtils.statisticSetStartEndTime(statisticList, statisticService);
                // 按日期差进行分类查询
                if (daysBetween >= 0 && daysBetween < THREE_YEAR) {
                    statisticListRep = statisticListUtils.statisticImageDateRespOut(statisticList, statisticListRep, resp, displayQuantity, statisticalDimension, daysBetween, statisticService);
                    return R.ok(statisticListRep);
                } else if (daysBetween >= THREE_YEAR) {
                    return R.fail(MAX_SELECT_TIME_ERROR);
                } else {
                    return R.fail(SELECT_TIME_ERROR);
                }
            } else {
                // 统计维度（竖轴）--除标注日期以外的
                statisticListRep = statisticListUtils.statisticImageRespOut(statisticList, statisticListRep, resp, displayQuantity, statisticalDimension, statisticService);
                return R.ok(statisticListRep);
            }
        } else {
            return R.fail(StatisticResponseConstant.STATISTIC_CATEGORY);
        }
    }

    /**
     * 综合统计列表页面导出excel
     *
     * @param response
     * @param statisticList
     */
    @RequiresPermissions("anno:statistic")
    @ApiOperation(value = "综合统计列表/导出细分筛选查询excel")
    @Log(title = "综合统计列表Excel导出", businessType = BusinessType.EXPORT)
    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response, @Validated StatisticListInVO statisticList) throws IOException {
        // 数量（横轴）: （标注数量/图像数量）
        String displayQuantity = statisticService.statisticSelectDictDataById(statisticList.getStatisticCategory()).getDictLabel();

        // 统计维度（项目/病理指标/标注类别/成员/图像）
        String statisticalDimension = statisticService.statisticSelectDictDataById(statisticList.getStatisticDimension()).getDictLabel();

        // 数量（横轴）: 标注数量
        if (displayQuantity.equals(ANNOTATION_COUNT)) {
            List<StatisticObjectOutVO> result;
            switch (statisticalDimension) {
                case PROJECT:
                    result = statisticService.statisticSelectAnnoProjectList(statisticList);
                    exportExcelUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                case PATHOLOGY_INDICATOR:
                    result = statisticService.statisticSelectAnnoIndicatorList(statisticList);
                    exportExcelUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                case ANNOTATION_CATEGORY:
                    result = statisticService.statisticSelectAnnoCategoryList(statisticList);
                    result.forEach(o -> {
                        if (StringUtils.isEmpty(o.getStatisticName()) && ObjectUtil.isNotNull(o.getStatisticCount())) {
                            o.setStatisticName(NO_ATTRIBUTE);
                        }
                    });
                    exportExcelUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                case USER:
                    result = statisticService.statisticSelectMemberList(statisticList);
                    exportExcelUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                case SLIDE:
                    result = statisticService.statisticSelectAnnoImageList(statisticList);
                    exportExcelUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                case ANNOTATION_DATE:
                    result = statisticService.statisticSelectAnnoDateDaysList(statisticList);
                    exportExcelDateUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                default:
                    throw new RuntimeException(StatisticResponseConstant.STATISTIC_DIMENSION);
            }
        } else if (displayQuantity.equals(SLIDE_COUNT)) {
            List<StatisticObjectOutVO> result;
            switch (statisticalDimension) {
                case PROJECT:
                    result = statisticService.statisticSelectImageProjectList(statisticList);
                    exportExcelUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                case PATHOLOGY_INDICATOR:
                    result = statisticService.statisticSelectImageIndicatorList(statisticList);
                    exportExcelUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                case ANNOTATION_CATEGORY:
                    result = statisticService.statisticSelectImageCategoryList(statisticList);
                    exportExcelUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                case USER:
                    result = statisticService.statisticSelectMemberList(statisticList);
                    exportExcelUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                case ANNOTATION_DATE:
                    result = statisticService.statisticSelectImageDateDaysList(statisticList);
                    exportExcelDateUtil(response, displayQuantity, statisticalDimension, result);
                    break;
                default:
                    throw new RuntimeException(StatisticResponseConstant.STATISTIC_DIMENSION);
            }
        }
    }

    /**
     * 获取项目列表
     *
     * @return
     */
    @ApiOperation(value = "共用接口/获取项目下拉菜单列表")
    @GetMapping("/projectList")
    public R<List<StatisticProjectListOutVO>> projectList() {
        List<StatisticProjectListOutVO> list = projectService.selectProjectStatisticList(new Project());
        return R.ok(list);
    }

    /**
     * 获取病理指标列表
     *
     * @param projectIdList
     * @return
     */
    @ApiOperation(value = "共用接口/获取病理指标下拉菜单列表")
    @PostMapping("/indicatorList")
    public R<List<StatisticIndicatorListOutVO>> indicatorList(@RequestBody StatisticIndicatorListInVO projectIdList) {
        List<StatisticIndicatorListOutVO> list = indicatorService.selectIndicatorStatisticList(projectIdList);
        return R.ok(list);
    }

    /**
     * 获取标注类别列表
     *
     * @param indicatorProjectIdList
     * @return
     */
    @ApiOperation(value = "共用接口/获取标注类别下拉菜单列表")
    @PostMapping("/categoryList")
    public R<List<StatisticCategoryListOutVO>> categoryList(@RequestBody StatisticCategoryListInVO indicatorProjectIdList) {
        List<StatisticCategoryListOutVO> list = pathologicalIndicatorCategoryService.selectAnnotationCategoryStatisticList(indicatorProjectIdList);
        StatisticCategoryListOutVO statisticCategoryListOutVO = new StatisticCategoryListOutVO();
        statisticCategoryListOutVO.setCategoryId(0);
        statisticCategoryListOutVO.setCategoryName("无属性");
        list.add(0, statisticCategoryListOutVO);
        return R.ok(list);
    }

    /**
     * 综合统计列表/成员列表
     *
     * @param statisticListInVO
     * @return
     */
    @ApiOperation(value = "综合统计列表/成员列表")
    @PostMapping("/userList")
    public R<List<StatisticUserListOutVO>> userList(@RequestBody StatisticListInVO statisticListInVO) {
        // 统计类别：标注数量、图像数量
        Long category = statisticListInVO.getStatisticCategory();
        // 统计维度：项目、病理指标、标注类别、成员、图像
        Long dimension = statisticListInVO.getStatisticDimension();

        StatisticSysDictDataOutVO categoryData = statisticService.statisticSelectDictDataById(category);
        StatisticSysDictDataOutVO dimensionData = statisticService.statisticSelectDictDataById(dimension);

        // 显示数量：标注数量
        if (Objects.nonNull(categoryData) && categoryData.getDictLabel().equals(ANNOTATION_COUNT)) {
            List<StatisticUserListOutVO> userList = statisticService.queryAnnotationMembersList(statisticListInVO);
            return R.ok(userList);
        }
        // 显示数量：图像数量
        if (Objects.nonNull(categoryData) && Objects.nonNull(dimensionData) && categoryData.getDictLabel().equals(SLIDE_COUNT)) {
            // 统计维度：标注类别
            if (dimensionData.getDictLabel().equals(ANNOTATION_CATEGORY)) {
                List<StatisticUserListOutVO> userList = statisticService.queryAnnotationMembersList(statisticListInVO);
                return R.ok(userList);
            }
            List<StatisticUserListOutVO> userList = statisticService.queryImageMembersList(statisticListInVO);
            return R.ok(userList);
        }
        return R.fail(null, "Invalid");
    }

    /**
     * 标注统计列表/成员列表
     *
     * @param statisticListInVO
     * @return
     */
    @ApiOperation(value = "标注统计列表/成员列表")
    @PostMapping("/memberList")
    public R<List<StatisticUserListOutVO>> memberList(@RequestBody StatisticListInVO statisticListInVO) {
        List<StatisticUserListOutVO> userList = statisticService.queryAnnotationMembersList(statisticListInVO);
        return R.ok(userList);
    }

    /**
     * 获取图像列表
     *
     * @param projectIdList
     * @return
     */
    @ApiOperation(value = "共用接口/获取图像下拉菜单列表")
    @PostMapping("/slideList")
    public R<List<StatisticSlideListOutVO>> slideList(@RequestBody StatisticSlideListInVO projectIdList) {
        List<StatisticSlideListOutVO> slideList = slideService.selectSlideListByProjectIdList(projectIdList);
        return R.ok(slideList);
    }

    /**
     * 标注统计列表页面接口
     *
     * @param statisticList
     * @return
     */
    @RequiresPermissions("anno:statistic")
    @ApiOperation(value = "标注统计列表/细分筛选查询/获取统计维度ID")
    @PostMapping("/annotationStatisticIdList")
    public R<List<AnnotationStatisticIdListOutVO>> annotationStatisticIdList(@Valid @RequestBody StatisticListInVO statisticList) {
        // 统计类别：标注数量、图像数量
        Long statisticCategory = statisticList.getStatisticCategory();
        // 统计维度：项目、病理指标、标注类别、成员、图像
        Long statisticDimension = statisticList.getStatisticDimension();
        // 通过SysDictData获取统计维度值、统计数量类别
        StatisticSysDictDataOutVO statisticCategorySysDictData = statisticService.statisticSelectDictDataById(statisticCategory);
        StatisticSysDictDataOutVO statisticDimensionSysDictData = statisticService.statisticSelectDictDataById(statisticDimension);
        String statisticCategoryDictLabel = statisticCategorySysDictData.getDictLabel();
        String statisticDimensionDictLabel = statisticDimensionSysDictData.getDictLabel();

        if (ANNOTATION_COUNT.equals(statisticCategoryDictLabel)) {
            // 统计数量:标注数量
            List<AnnotationStatisticIdListOutVO> resp;
            switch (statisticDimensionDictLabel) {
                // 统计维度: 项目
                case PROJECT:
                    resp = statisticService.statisticSelectAnnoProjectIdList(statisticList);
                    break;
                // 统计维度: 病理指标
                case PATHOLOGY_INDICATOR:
                    resp = statisticService.statisticSelectAnnoIndicatorIdList(statisticList);
                    break;
                // 统计维度: 标注类别
                case ANNOTATION_CATEGORY:
                    resp = statisticService.statisticSelectAnnoCategoryIdList(statisticList);
                    break;
                // 统计维度: 成员
                case USER:
                    resp = statisticService.statisticSelectAnnoMembersIdList(statisticList);
                    break;
                // 统计维度: 图像
                case SLIDE:
                    resp = statisticService.statisticSelectAnnoImageIdList(statisticList);
                    break;
                default:
                    throw new RuntimeException(StatisticResponseConstant.STATISTIC_DIMENSION);
            }
            return R.ok(resp);
        }

        log.error("statisticCount参数值错误");
        return R.fail("statisticCount参数值错误");
    }

    /**
     * 获取统计维度分页数据
     *
     * @param statisticList
     * @return
     */
    @RequiresPermissions("anno:statistic")
    @ApiOperation(value = "标注统计列表/细分筛选查询/获取统计维度分页数据")
    @PostMapping("/annotationStatisticPageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    public R<PageMaster<AnnotationStatisticListPageOutVO>> annotationStatisticPageList(@Valid @RequestBody AnnotationStatisticListPageInVO statisticList) {
        // 统计数量（标注/图像）
        String category = statisticService.statisticSelectDictDataById(statisticList.getStatisticCategory()).getDictLabel();
        // 统计维度（项目/病理指标/标注类别/成员/图像）
        String dimension = statisticService.statisticSelectDictDataById(statisticList.getStatisticDimension()).getDictLabel();
        if (category.equals(ANNOTATION_COUNT)) {
            startPage();
            List<AnnotationStatisticListPageOutVO> resp;
            switch (dimension) {
                case PROJECT:
                    resp = statisticService.statisticSelectAnnoProjectPageList(statisticList);
                    break;
                case PATHOLOGY_INDICATOR:
                    resp = statisticService.statisticSelectAnnoIndicatorPageList(statisticList);
                    break;
                case ANNOTATION_CATEGORY:
                    resp = statisticService.statisticSelectAnnoCategoryPageList(statisticList);
                    break;
                case USER:
                    resp = statisticService.SelectMembersPageList(statisticList);
                    break;
                case SLIDE:
                    resp = statisticService.statisticSelectAnnoImagePageList(statisticList);
                    break;
                default:
                    throw new RuntimeException(StatisticResponseConstant.STATISTIC_DIMENSION);
            }

            PageMaster<AnnotationStatisticListPageOutVO> pageMaster = new PageMaster<>(resp);
            return R.ok(pageMaster);
        }
        return R.fail(StatisticResponseConstant.STATISTIC_COUNT);
    }

    /**
     * 获取标注详细信息
     *
     * @param annotationId
     * @return
     */
    @RequiresPermissions("anno:statistic")
    @ApiOperation(value = "标注统计列表/细分筛选查询/获取标注详细信息")
    @GetMapping("/annotationDetail")
    public R<AnnotationBroadcastVO> annotationDetail(@RequestParam("annotationId") Long annotationId) {
        AnnotationBroadcastVO annotationBy = annotationService.annoUserCategory(annotationId);
        return R.ok(annotationBy);
    }
}
