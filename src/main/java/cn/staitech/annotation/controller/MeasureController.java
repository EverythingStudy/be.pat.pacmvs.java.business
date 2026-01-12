package cn.staitech.annotation.controller;

import cn.staitech.annotation.domain.Measure;
import cn.staitech.annotation.netty.message.AnnotationFeature;
import cn.staitech.annotation.service.MeasureService;
import cn.staitech.annotation.utils.measure.MeasureMessageGenerator;
import cn.staitech.annotation.vo.measure.*;
import cn.staitech.common.core.domain.CustomPage;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.locationtech.jts.geom.Geometry;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mugw
 * @version 1.0
 * @description 测量标注管理
 * @date 2025/5/21 14:33:43
 */
@Api(value = "viewer页面-测量")
@Slf4j
@RestController
@RequestMapping("/measure")
public class MeasureController {

    @Resource
    private MeasureService measureService;

    @ApiOperation(value = "获取测量分页")
    @PostMapping("/page")
    public R<CustomPage<MeasureVo>> page(@Validated @RequestBody MeasureReq req) throws Exception {
        CustomPage<Measure> page = new CustomPage<>(req);
        measureService.page(page, Wrappers.<Measure>lambdaQuery().eq(Measure::getSlideId, req.getSlideId())
                .like(req.getMeasureFullName() != null, Measure::getMeasureFullName, req.getMeasureFullName())
                .ne(Measure::getLocationType, Geometry.TYPENAME_POINT));
        long points = measureService.count(Wrappers.<Measure>lambdaQuery().eq(Measure::getSlideId, req.getSlideId())
                .like(req.getMeasureFullName() != null, Measure::getMeasureFullName, req.getMeasureFullName())
                .eq(Measure::getLocationType, Geometry.TYPENAME_POINT));
        CustomPage<MeasureVo> pageResult = new CustomPage<>(req);
        List<MeasureVo> measureVoList = new ArrayList<>();
        if (page.getTotal() > 0) {
            measureVoList = page.getRecords().stream().map(MeasureVo::convert).collect(Collectors.toList());
            if (points > 0) {
                measureVoList.add(MeasureVo.builder().pointCount(points).measureFullName("P").build());
            }
            pageResult.setTotal(page.getTotal());
            pageResult.setRecords(measureVoList);
        }else{
            if (points > 0) {
                pageResult.setTotal(1);
                pageResult.setRecords(Arrays.asList(MeasureVo.builder().pointCount(points).measureFullName("P").build()));
            }
        }

        return R.ok(pageResult);
    }

    @ApiOperation(value = "获取GeoJson数据")
    @GetMapping("/getDataList")
    public R<List<AnnotationFeature>> getDataList(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) throws Exception {
        List<Measure> measureList = measureService.list(Wrappers.<Measure>lambdaQuery().eq(Measure::getSlideId, slideId));
        List<AnnotationFeature> featureList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(measureList)){
            featureList = measureList.stream().map(MeasureMessageGenerator::generateFeatures).collect(Collectors.toList());
        }
        return R.ok(featureList);
    }

    @ApiOperation(value = "添加测量",tags = "I18n")
    @PostMapping("/add")
    public R<String> add(@Validated @RequestBody MeasureAddVo measureAddVo) throws Exception {
        Measure measure = new Measure();
        BeanUtils.copyProperties(measure, measureAddVo);
        R<Measure> result = measureService.addMeasure(measure);
        if (result.getCode() == R.SUCCESS){
            return R.ok(String.valueOf(result.getData().getMeasureId()));
        }else{
            return R.fail(result.getMsg());
        }
    }

    @ApiOperation(value = "删除测量",tags = "I18n")
    @PostMapping("/del")
    public R<String> del(@RequestBody DelMeasureReq req) throws Exception {
        return measureService.delete(req.getMarking_id());
    }


    @ApiOperation(value = "标注测量excel导出",tags = "I18n")
    @PostMapping("/export")
    public void export(@RequestBody ExportSlideReq req) throws Exception {
        measureService.export(req.getSlideId());
    }

}
