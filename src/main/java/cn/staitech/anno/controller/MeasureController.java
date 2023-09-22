//package cn.staitech.anno.controller;
//
//import cn.staitech.anno.constant.AnnotationConstant;
//import cn.staitech.anno.constant.ExaminationConstant;
//import cn.staitech.anno.constant.R.AnnotationResponseConstant;
//import cn.staitech.anno.constant.R.MeasureResponseConstant;
//import cn.staitech.anno.constant.R.ResponseConstant;
//import cn.staitech.anno.domain.Measure;
//import cn.staitech.anno.domain.MeasureJson;
//import cn.staitech.anno.domain.Slide;
//import cn.staitech.anno.domain.vo.measurevo.*;
//import cn.staitech.anno.service.AnnotationService;
//import cn.staitech.anno.service.MeasureService;
//import cn.staitech.anno.service.SlideService;
//import cn.staitech.anno.utils.*;
//import cn.staitech.common.core.domain.R;
//import cn.staitech.common.core.web.controller.BaseController;
//import cn.staitech.common.log.annotation.Log;
//import cn.staitech.common.log.enums.BusinessType;
//import cn.staitech.common.security.utils.SecurityUtils;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.vividsolutions.jts.geom.Geometry;
//import io.swagger.annotations.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//import static cn.staitech.anno.constant.AnnotationConstant.NOT_PERMISSION;
//import static cn.staitech.anno.constant.R.MeasureResponseConstant.MEASURE_NUM;
//import static cn.staitech.anno.constant.R.MeasureResponseConstant.SLIDE_LINE;
//
//
///**
// * 测量工具 .
// *
// * @author admin
// */
//@Slf4j
//@Api(value = "测量工具", tags = "测量工具")
//@RestController
//@RequestMapping("/measure")
//@RefreshScope
//public class MeasureController extends BaseController {
//
//    @Resource
//    private SlideService slideService;
//
//    @Resource
//    private MeasureService measureService;
//
//    @Resource
//    private AnnotationService annotationService;
//
//    @Value("${jsonFilePath}")
//    private String path;
//
//    /**
//     * 测量工具JSON生成 .
//     *
//     * @return
//     */
//    // @RequiresPermissions(value = {"anno:measure:json"})
//    // @ProjectRequiresPermissions(value = {"anno:measure:json"})
//    @ApiOperation(value = "实时生成并下载JSON文件")
//    @PostMapping("/json")
//    public R json(
//            @RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId,
//            HttpServletResponse response) {
//        // 参数校验
//        if (!Optional.ofNullable(slideId).isPresent()) {
//            return R.fail(MeasureResponseConstant.ARGUMENT_INVALID);
//        }
//
//        // 获取当前切片信息 主要用于取图像ID
//        Slide slide = slideService.selectById(slideId);
//
//        Long createBy = SecurityUtils.getUserId();
//
//        // 获取当前用户某一切片所有测量标注 measureType=0
//        Measure measureSub = Measure.builder()
//                .slideId(slideId)
//                .measureType(0)
//                .createBy(createBy)
//                .orderField("measure_id")
//                .orderType("asc")
//                .build();
//
//        List<MeasureJsonVO> subList = measureService.selectMeasureJson(measureSub);
//
//        // 获取当前用户某一切片有的测量数据 measureType=1
//        Measure measureParent = Measure.builder()
//                .slideId(slideId)
//                .measureType(1)
//                .createBy(createBy)
//                .orderField("measure_id")
//                .orderType("asc")
//                .build();
//
//        List<MeasureJsonVO> parentList = measureService.selectMeasureJson(measureParent);
//
//        MeasureJson mJson = MeasureJson.builder()
//                .imageId(slide.getImageId())
//                .subList(subList)
//                .parentList(parentList)
//                .build();
//
//        // 生成Json字符串
//        String jsonString = JSON.toJSONString(mJson, SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue);
//
//        // 以流的形式下载文件
//        try {
//            // 清空response
//            response.reset();
//            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
//            response.setCharacterEncoding(ExaminationConstant.CHARACTER_ENCODING);
//            response.setContentType(ExaminationConstant.CONTENT_TYPE);
//            response.setHeader(ExaminationConstant.HEADER,
//                    "attachment;filename=" + getFileName(slideId, createBy, MeasureResponseConstant.FILE_SUFFIX_JSON));
//            outputStream.write(jsonString.getBytes());
//            // 关闭流
//            outputStream.close();
//
//        } catch (Exception e) {
//            log.error(MeasureResponseConstant.DOWNLOAD_ERROR, e);
//            return R.fail(MeasureResponseConstant.DOWNLOAD_ERROR);
//        }
//        return R.ok(MeasureResponseConstant.DOWNLOAD_SUCCESS);
//    }
//
//
//    /**
//     * 标注测量excel导出.
//     *
//     * @param slideId 切片ID
//     */
//    // @RequiresPermissions("anno:measure:export")
//    @Log(title = "标注测量excel导出", businessType = BusinessType.EXPORT)
//    @ApiOperation(value = "标注测量excel导出")
//    @PostMapping("/export")
//    public void export(
//            @RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId,
//            HttpServletResponse response) throws Exception {
//
//        clearPage();
//
//        Long createBy = SecurityUtils.getUserId();
//
//        // 构造表头的每个列头 定义表头
//        List<Map<String, String>> titleList = getTitleList(MeasureResponseConstant.COLHEAD_KEY,
//                MeasureResponseConstant.COLHEAD_VALUE);
//
//        // 行内数据
//        Measure measure = new Measure();
//        measure.setSlideId(slideId);
//        measure.setCreateBy(createBy);
//        // 注意：此条件查询的是不包含Point的记录
//        measure.setLocationType("Point");
//        List<MeasureSelectVO> measureList = measureService.selectMeasureBy(measure);
//
//        List<MeasureSelectExcelVO>  list = new ArrayList<>();
//        for (MeasureSelectVO vo : measureList) {
//            MeasureSelectExcelVO excelVO = new MeasureSelectExcelVO();
//            BeanUtils.copyProperties(vo,excelVO);
//            excelVO.setMeasureName(vo.getMeasureName()+vo.getMeasureNum());
//            list.add(excelVO);
//        }
//
//        // 加点的记录
//        MeasureSelectVO measurePointVo = getMeasurePoint(measure);
//        if (measurePointVo != null) {
//            MeasureSelectExcelVO excelVO = new MeasureSelectExcelVO();
//            BeanUtils.copyProperties(measurePointVo,excelVO);
//            list.add(excelVO);
//        }
//
//        // 获取文件名
//        //String fileName = getFileName(slideId, createBy, MeasureResponseConstant.FILE_SUFFIX_EXCEL);
//
//        // 生成excel文件
//        ExcelTool excelTool = new ExcelTool(MeasureResponseConstant.EXCEL_TITLE, 20, 20);
//        List<Column> titleData = excelTool.columnTransformer(titleList);
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setCharacterEncoding("utf-8");
//        //response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//        excelTool.exportExcel(titleData, list, response.getOutputStream(), true, false);
//    }
//
//
//    /**
//     * 测量工具JSON生成 .
//     *
//     * @return
//     */
////    @RequiresPermissions(value = {"anno:measure:download"})
////     @ApiOperation(value = "实时生成并下载JSON文件,临时文件会在磁盘加载,下载后删除")
////     @GetMapping("/download")
//    public R download(
//            //@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId,
//            @RequestParam(value = "slideId") Long slideId, HttpServletResponse response) throws IOException {
//
//        // 参数校验
//        if (!Optional.ofNullable(slideId).isPresent()) {
//            return R.fail(MeasureResponseConstant.ARGUMENT_INVALID);
//        }
//
//        Long createBy = SecurityUtils.getUserId();
//
//        // 查询当前用户某一切片所有测量标注 measureType=0
//        Measure measureSub = new Measure();
//        measureSub.setSlideId(slideId);
//        measureSub.setMeasureType(0);
//        measureSub.setCreateBy(createBy);
//        measureSub.setOrderField("measure_id");
//        measureSub.setOrderType("asc");
//        List<MeasureJsonVO> subList = measureService.selectMeasureJson(measureSub);
//
//        // 查询当前用户某一切片有的测量数据 measureType=1
//        Measure measureType1 = new Measure();
//        measureType1.setSlideId(slideId);
//        measureType1.setMeasureType(1);
//        measureType1.setCreateBy(createBy);
//        measureType1.setOrderField("measure_id");
//        measureType1.setOrderType("asc");
//        List<MeasureJsonVO> parentList = measureService.selectMeasureJson(measureType1);
//
//        // 生成Json文件
//        File directories = new File(path + File.separator + ExaminationConstant.FILE_PATH);
//        File fileName = new File(getFileName(slideId, createBy, MeasureResponseConstant.FILE_SUFFIX_JSON));
//        File file = new File(directories + File.separator + fileName);
//
//        JsonUtils.createDirectories(directories);
//
//        MeasureJson mJson = MeasureJson.builder()
//                .imageId(1L)
//                .subList(subList)
//                .parentList(parentList)
//                .build();
//
//
//        String jsonString = JSON.toJSONString(mJson, SerializerFeature.PrettyFormat,
//                SerializerFeature.WriteMapNullValue);
//
//        // 写临时文件
//        JsonUtils.writeFile(jsonString, file);
//
//        OutputStream outputStream = null;
//        try {
//            // 以流的形式下载文件
//            FileInputStream stream = new FileInputStream(file.getPath());
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
//            byte[] bytes = new byte[bufferedInputStream.available()];
//            bufferedInputStream.read(bytes);
//            bufferedInputStream.close();
//            // 清空response
//            response.reset();
//            outputStream = new BufferedOutputStream(response.getOutputStream());
//            response.setCharacterEncoding(ExaminationConstant.CHARACTER_ENCODING);
//            response.setContentType(ExaminationConstant.CONTENT_TYPE);
//            response.setHeader(ExaminationConstant.HEADER, "attachment;filename=" + file.getName());
//            outputStream.write(bytes);
//            outputStream.flush();
//        } catch (Exception e) {
//            log.error(MeasureResponseConstant.DOWNLOAD_ERROR, e);
//            return R.fail(MeasureResponseConstant.DOWNLOAD_ERROR);
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    log.error(MeasureResponseConstant.DOWNLOAD_STREAM_ERROR, e);
//                }
//            }
//            //删除临时文件
//            file.delete();
//        }
//        return R.ok(MeasureResponseConstant.DOWNLOAD_SUCCESS);
//    }
//
//
//    /**
//     * 测量工具JSON文件导入
//     *
//     * @param file
//     * @return
//     */
//
//    // @RequiresPermissions("anno:measure:upload")
//    @ApiOperation(value = "测量工具 JSON文件导入接口")
//    @PostMapping("/upload")
//    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "测量工具JSON文件", required = true, dataType = "file")})
//    public R<String> upload(@RequestParam("file") MultipartFile file,
//                            @RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId
//                            ) {
//        // 参数校验
//        if (!Optional.ofNullable(slideId).isPresent()) {
//            return R.fail(MeasureResponseConstant.ARGUMENT_INVALID);
//        }
//
//        if (file == null) {
//            return R.fail(MeasureResponseConstant.NO_FILE);
//        } else if (file.getSize() > MeasureResponseConstant.FILE_MAX_SIZE) {
//            return R.fail(MeasureResponseConstant.FILE_MAX_SIZE_INVALID);
//        }
//
//        // 获取当前切片信息 主要用于取图像ID
//        Slide slide = slideService.selectById(slideId);
//
//        try {
//            // 从文件流直接反序列化对象，不进行文件磁盘存储
//            MeasureJson measureJson = JSON.parseObject(file.getInputStream(), MeasureJson.class);
//
//            Long imageId = measureJson.getImageId();
//
//            if (! slide.getImageId().equals(imageId)) {
//                return R.fail(MeasureResponseConstant.IMAGEID_NOT_EXISTS);
//            }
//
//            // map size = sub list size
//            int mapSize = measureJson.getSubList().size();
//            // 旧、新measureId映射
//            Map<Long, Long> map = new HashMap<>(mapSize);
//
//            // ProjectId
//            Long projectId = slide.getProjectId();
//            // 当前用户ID
//            Long createBy = SecurityUtils.getUserId();
//
//            // 获取当前用户某一切片所有测量标注 measureType=0
//            List<MeasureJsonVO> subList = measureJson.getSubList();
//            // 遍历subList-> 向数据库中添加标注数据，并更新map数据
//            for (MeasureJsonVO vo : subList) {
//                Measure measure = new Measure();
//                BeanUtils.copyProperties(vo, measure);
//                measure.setProjectId(projectId);
//                measure.setImageId(imageId);
//                measure.setSlideId(slideId);
//                measure.setCreateBy(createBy);
//
//                String measureName = vo.getMeasureName();
//                Long numNum = CacheUtils.getAndAddLong(MEASURE_NUM + slideId + SLIDE_LINE + createBy + SLIDE_LINE + measureName, 1L);
//                measure.setMeasureName(measureName);
//
//                measure.setMeasureNum(numNum);
//
//                measureService.insertMeasure(measure);
//                map.put(vo.getMeasureId(), measure.getMeasureId());
//            }
//
//            // 置空subList释放内存
//            measureJson.setSubList(null);
//
//            // 获取当前用户某一切片有的测量数据 measureType=1
//            List<MeasureJsonVO> parentList = measureJson.getParentList();
//            for (MeasureJsonVO vo : parentList) {
//                Measure measure = new Measure();
//                BeanUtils.copyProperties(vo, measure);
//                measure.setProjectId(projectId);
//                measure.setImageId(imageId);
//                measure.setSlideId(slideId);
//                measure.setCreateBy(createBy);
//
//                // 通过Redis取自增ID
//                String measureName = vo.getMeasureName();
//                Long numNum = CacheUtils.getAndAddLong(MEASURE_NUM + slideId + SLIDE_LINE + createBy + SLIDE_LINE + measureName, 1L);
//                measure.setMeasureName(measureName);
//                measure.setMeasureNum(numNum);
//
//                measure.setMaIdA(map.get(vo.getMaIdA()));
//                measure.setMaIdB(map.get(vo.getMaIdB()));
//                measureService.insertMeasure(measure);
//            }
//
//            return R.ok(MeasureResponseConstant.UPLOAD_SUCCESS);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.fail(MeasureResponseConstant.UPLOAD_FAILED);
//        }
//    }
//
//
//    @ApiOperation(value = "添加测量接口")
//    @Log(title = "标注测量", businessType = BusinessType.INSERT)
//    @PostMapping("/addMeasure")
//    public R<Measure> add(@Validated @RequestBody MeasureAddVO req) throws Exception {
//        if (Boolean.FALSE.equals(slideService.markIsNotFinish(req.getSlideId()))) {
//            return R.fail(AnnotationConstant.UPDATE_ANNOTATION_CATEGORY_MESSAGE);
//        }
//        Slide slide = slideService.selectById(req.getSlideId());
//        // 校验当前用户是否有权限
//        boolean res = annotationService.getPermission(slide.getProjectId(),req.getPermission());
//        if(Boolean.FALSE.equals(res)){
//            return R.fail(NOT_PERMISSION);
//        }
//        // 判断新图像是否符合规则
//        Geometry geometry = MarkVerify.addVerify(req.getLocation());
//        if (geometry.isEmpty()) {
//            return R.fail(AnnotationResponseConstant.GRAPHICS_MARK_NOT_RULES);
//        }
//        String area = String.valueOf((int) geometry.getArea());
//        String length = String.valueOf((int) geometry.getLength());
//        Measure measure = new Measure();
//        BeanUtils.copyProperties(req, measure);
//        measure.setArea(area);
//        measure.setPerimeter(length);
//        measure.setProjectId(slide.getProjectId());
//        measure.setImageId(slide.getImageId());
//        measure.setCreateBy(SecurityUtils.getUserId());
//        String measureNum = req.getMeasureName();
//        // 通过Redis取自增ID
//        String numKey = req.getSlideId() + SLIDE_LINE + SecurityUtils.getUserId() + SLIDE_LINE + measureNum ;
//        Long numId = CacheUtils.getAndAddLong(MEASURE_NUM + numKey, 1L);
//        measure.setMeasureName(measureNum);
//        measure.setMeasureNum(numId);
//        measureService.insertMeasure(measure);
//        //更新项目时间
//        ProjectUtils.updateProjectStatus(slide.getProjectId());
//        return R.ok(measure);
//    }
//
//
//    @ApiOperation(value = "查询测量接口")
//    @Log(title = "标注测量", businessType = BusinessType.INSERT)
//    @PostMapping("/selectMeasureBy")
//    public R<List<MeasureSelectVO>> selectBy(
//            @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId) {
//        if (!Optional.ofNullable(slideId).isPresent()) {
//            return R.fail(ResponseConstant.PARAMS_EXCEPTION);
//        }
//        Measure measure = new Measure();
//        measure.setSlideId(slideId);
//        measure.setCreateBy(SecurityUtils.getUserId());
//        List<MeasureSelectVO> list = measureService.selectMeasureBy(measure);
//        if (getMeasurePoint(measure) != null) {
//            list.add(getMeasurePoint(measure));
//        }
//        return R.ok(list);
//    }
//
//    @ApiOperation(value = "删除测量接口")
//    @Log(title = "标注测量", businessType = BusinessType.INSERT)
//    @PostMapping("/deleteMeasureBy")
//    public R<String> deleteBy(@Validated MeasureDelVO req ) throws Exception {
//        Measure measureBy = measureService.selectMeasureById(req.getMeasureId());
//        if (!Optional.ofNullable(measureBy).isPresent()) {
//            return R.fail(MeasureResponseConstant.NOT_MEASURE);
//        }
//        if (Boolean.FALSE.equals(slideService.markIsNotFinish(measureBy.getSlideId()))) {
//            return R.fail(AnnotationConstant.UPDATE_ANNOTATION_CATEGORY_MESSAGE);
//        }
//        // 校验当前用户是否有权限
//        boolean permsRes = annotationService.getPermission(measureBy.getProjectId(),req.getPermission());
//        if(Boolean.FALSE.equals(permsRes)){
//            return R.fail(NOT_PERMISSION);
//        }
//        int res = measureService.deleteMeasureById(req.getMeasureId());
//        //更新项目时间
//        ProjectUtils.updateProjectStatus(measureBy.getProjectId());
//        if (res > 0) {
//            return R.ok(ResponseConstant.OPERATE_SUCCEED);
//        } else {
//            return R.ok(ResponseConstant.OPERATE_ERROR);
//        }
//    }
//
//
//    @ApiOperation(value = "更新测量接口")
//    @Log(title = "标注测量", businessType = BusinessType.INSERT)
//    @PostMapping("/updateMeasureBy")
//    public R<String> updateBy(@Validated @RequestBody MeasureUpdateVO req) throws Exception {
//
//        Measure measureBy = measureService.selectMeasureById(req.getMeasureId());
//        if (Boolean.FALSE.equals(slideService.markIsNotFinish(measureBy.getSlideId()))) {
//            return R.fail(AnnotationConstant.UPDATE_ANNOTATION_CATEGORY_MESSAGE);
//        }
//        // 校验当前用户是否有权限
//        boolean res = annotationService.getPermission(measureBy.getProjectId(),req.getPermission());
//        if(Boolean.FALSE.equals(res)){
//            return R.fail(NOT_PERMISSION);
//        }
//
//        String location = MarkVerify.updateVerify(measureBy.getLocation(), req.getLocation(), req.getOperation());
//        Geometry geometry;
//        try {
//            geometry = MarkVerify.wktReader.read(location);
//            geometry.union(geometry);
//        } catch (Exception e) {
//            return R.fail(AnnotationResponseConstant.NEW_GRAPHICS_MARK_NOT_RULES);
//        }
//        String area = String.valueOf((int) geometry.getArea());
//        String length = String.valueOf((int) geometry.getLength());
//        Measure measure = new Measure();
//        BeanUtils.copyProperties(req, measure);
//        measure.setArea(area);
//        measure.setPerimeter(length);
//        measure.setLocation(location);
//        measure.setUpdateBy(SecurityUtils.getUserId());
//        measureService.updateMeasure(measure);
//        //更新项目时间
//        ProjectUtils.updateProjectStatus(measureBy.getProjectId());
//        return R.ok(ResponseConstant.OPERATE_SUCCEED);
//
//    }
//
//    @ApiOperation(value = "批量删除接口")
//    @Log(title = "标注测量", businessType = BusinessType.INSERT)
//    @PostMapping("/deleteMeasureBatch")
//    public R<String> deleteBatch(
//            @RequestParam @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId) {
//        if (!Optional.ofNullable(slideId).isPresent()) {
//            return R.fail(ResponseConstant.PARAMS_EXCEPTION);
//        }
//        if (Boolean.FALSE.equals(slideService.markIsNotFinish(slideId))) {
//            return R.fail(AnnotationConstant.UPDATE_ANNOTATION_CATEGORY_MESSAGE);
//        }
//        Measure measure = new Measure();
//        measure.setCreateBy(SecurityUtils.getUserId());
//        measure.setSlideId(slideId);
//        measureService.deleteMeasureBatch(measure);
//        Slide slide=slideService.selectById(slideId);
//        //更新项目时间
//        ProjectUtils.updateProjectStatus(slide.getProjectId());
//        return R.ok(ResponseConstant.OPERATE_SUCCEED);
//    }
//
//
//    /**
//     * 生成JSON文件名
//     *
//     * @param slideId
//     * @param createBy
//     * @return
//     */
//    public String getFileName(Long slideId, Long createBy, String fileSuffix) {
//        String updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        return MeasureResponseConstant.FILE_PREFIX + slideId + MeasureResponseConstant.FILE_CONNECTOR + createBy
//                + MeasureResponseConstant.FILE_CONNECTOR + updateTime + fileSuffix;
//    }
//
//    /**
//     * 生成表头
//     */
//    public List<Map<String, String>> getTitleList(String[] colHeadKey, String[] colHeadValue) {
//        // 定义表头
//        List<Map<String, String>> list = new ArrayList<>();
//
//        for (int i = 0; i < colHeadKey.length; i++) {
//            Map<String, String> map = new HashMap<String, String>(1);
//            map.put(colHeadKey[i], colHeadValue[i]);
//            list.add(map);
//        }
//        return list;
//    }
//
//    /**
//     * 获取测量点的记录 获取点的总数
//     *
//     * @param measure
//     * @return
//     */
//    public MeasureSelectVO getMeasurePoint(Measure measure) {
//        int count = measureService.selectMeasureCount(measure);
//        if (count != 0) {
//            MeasureSelectVO measureSelectVo = new MeasureSelectVO();
//            measureSelectVo.setSlideId(measure.getSlideId());
//            measureSelectVo.setMeasureName("P");
//            measureSelectVo.setPointCount((long) count);
//            measureSelectVo.setUserName(SecurityUtils.getUsername());
//            measureSelectVo.setCreateTime(
//                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            return measureSelectVo;
//        }
//        return null;
//    }
//
//
//}