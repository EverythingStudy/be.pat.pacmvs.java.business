package cn.staitech.anno.controller;

import cn.staitech.anno.config.RedisClientUtil;
import cn.staitech.anno.constant.ExaminationConstant;
import cn.staitech.anno.constant.R.MeasureResponseConstant;
import cn.staitech.anno.constant.R.ResponseConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.document.GeometryDoc;
import cn.staitech.anno.domain.file.Chunk;
import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.GeoImage;
import cn.staitech.anno.domain.geojson.GeoProject;
import cn.staitech.anno.domain.geojson.Properties;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.viewAddIn;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.marking.SlideRes;
import cn.staitech.anno.domain.project.out.ProjectInfoOut;
import cn.staitech.anno.domain.vo.BroadcastVO;
import cn.staitech.anno.domain.vo.special.SpecialResVo;
import cn.staitech.anno.elasticsearchRepositories.GeometryDocMapper;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.service.*;
import cn.staitech.anno.utils.CustomizationIdUtils;
import cn.staitech.anno.utils.FileUtils;
import cn.staitech.anno.utils.SendMessage;
import cn.staitech.common.core.constant.CacheConstants;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.StringUtils;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.redis.service.RedisService;
import cn.staitech.common.security.utils.SecurityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cn.staitech.anno.constant.AnnotationConstant.*;
import static cn.staitech.anno.constant.ProjectConstant.CHARACTER_SET;
import static cn.staitech.anno.constant.ViewerConstant.MICRON;
import static cn.staitech.anno.utils.FileUtils.*;
import static cn.staitech.anno.aspect.LogFileAspect.response;
import static cn.staitech.common.core.utils.file.FileUtils.deleteFile;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/viewer")
@Api(value = "viewer页面", tags = "viewer页面")
public class ViewerController {

    DecimalFormat decimalFormat = new DecimalFormat("0.00000");

    @Resource
    private ViewerService viewerService;
    @Resource
    private MarkingService markingService;

    @Resource
    private SlideService slideService;

    @Resource
    private RedisClientUtil redisClientUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PathologicalIndicatorCategoryService pathologicalIndicatorCategoryService;

    @Resource
    private GeometryDocMapper geometryDocMapper;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private FileService fileService;

    @Resource
    private SubImageService subImageService;

    @Resource
    private ImageService imageService;

    @Resource
    private ProjectExtService projectService;

    @Resource
    private SpecialService specialService;

    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;

    @Value("${netty.port}")
    private Integer nettyPort;


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "导入zip文件(小文件)")
    @ApiImplicitParams({@ApiImplicitParam(name = "specialId", value = "专题Id", required = true, dataType = "Long"), @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file")})
    @PostMapping("/upload")
    public R<String> uploadZip(@RequestParam("specialId") Long specialId, @RequestParam("file") MultipartFile file) throws Exception {
        if (!Optional.ofNullable(specialId).isPresent()) {
            return R.fail("参数异常");
        }
        String fileUrl = fileService.upload(file);
        viewerService.zipExport(fileUrl, specialId);
        return R.ok();
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "导入zip文件(大文件)")
    @ApiImplicitParams({@ApiImplicitParam(name = "specialId", value = "专题Id", required = true, dataType = "Long"), @ApiImplicitParam(name = "fileName", value = "文件名称", required = true, dataType = "String"), @ApiImplicitParam(name = "chunk", value = "分片Id", required = true, dataType = "Integer"), @ApiImplicitParam(name = "chunkTotal", value = "分片总数", required = true, dataType = "Integer"), @ApiImplicitParam(name = "chunkSize", value = "分片大小", required = true, dataType = "Long"), @ApiImplicitParam(name = "file", value = "分片文件", required = true, dataType = "file")})
    @PostMapping("/uploadZip")
    public R<String> uploadZip(@RequestParam("specialId") Long specialId, @RequestParam("fileName") String fileName, @RequestParam("chunk") Integer chunk, @RequestParam("chunkTotal") Integer chunkTotal, @RequestParam("chunkSize") Long chunkSize, @RequestParam("file") MultipartFile file) throws Exception {
        Chunk chunkObj = new Chunk().setChunkNumber(chunk).setFile(file).setFileName(fileName).setTotalChunks(chunkTotal).setSpecialId(specialId).setChunkSize(chunkSize);
        String zipUrl = fileService.mergeChunk(chunkObj);
        if (!Optional.ofNullable(specialId).isPresent()) {
            return R.fail("参数异常");
        }
        viewerService.zipExport(zipUrl, specialId);
        return R.ok("操作成功");
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String sPath) throws IOException {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            }
//            else {  // 为目录时调用删除目录方法
//                return deleteDirectory(file);
//            }
        }
        return true;

    }

    // http://127.0.0.1:9998/viewer/exportZip?specialId=583
    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "导出json数据zip格式")
    @GetMapping("/exportZip")
    public void exportJson1(@RequestParam(value = "specialId") @ApiParam(name = "specialId", value = "专题id", required = true) Long specialId) throws IOException {
        if (!Optional.ofNullable(specialId).isPresent()) {
//            return R.fail("参数异常");
        }
        SpecialResVo special = specialService.selectSpecialId(specialId);
        String zipPath = "/home/uploadPath/zip";
        try {
            File file = new File(zipPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(zipPath + File.separator + special.getSpecialId() + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fileOutputStream);
            ByteArrayInputStream bais;
            List<SlideRes> slideList = markingService.selectSlideList(specialId);
            for (SlideRes slide : slideList) {
                Slide slideBy = slideService.getById(slide.getSlideId());
                if (slideBy != null) {
                    System.out.println();
                    if (slideBy.getGeojsonUrl() != null && !slideBy.getGeojsonUrl().equals("")) {
                        JSONObject res = getAnnotation(slideBy.getGeojsonUrl());
                        // 构建image数据
                        SubImage image = subImageService.getById(slideBy.getImageId());
                        GeoImage geoImage = new GeoImage();
                        if (image != null) {
                            geoImage.setCreate_time(image.getCreateTime());
                            String type = String.valueOf((image.getImageName().split("\\.")[image.getImageName().split("\\.").length - 1]));
                            geoImage.setImage_type(type);
                            String name = image.getImageName().split("\\.")[0];
                            geoImage.setImage_name(name);
                            String shape = "(" + image.getWidth() + "," + image.getHeight() + ")";
                            geoImage.setImage_shape(shape);
                        }
                        res.put("image", geoImage);
                        // 查询项目详情
                        ProjectInfoOut project = projectService.getProjectById(slideBy.getProjectId());
                        GeoProject geoProject = new GeoProject();
                        if (project != null) {
                            geoProject.setProject_id(project.getProjectId());
                            geoProject.setProject_name(geoProject.getProject_name());
                        }
                        res.put("project", geoProject);
                        log.info("--------------------------->" + slideBy);
                        String jsonName = slide.getImageName().replaceAll(slide.getImageName().split("\\.")[slide.getImageName().split("\\.").length - 1], "json");
//                        Company company =
//                                JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(paramsMap)),Company.class);

                        // 构建数据
                        // 添加图片和项目、指标信息

                        zos.putNextEntry(new ZipEntry(jsonName));
                        //json数据转为输入流
                        bais = new ByteArrayInputStream(res.toString().getBytes(CHARACTER_SET));

                        int len = 0;
                        byte[] buf = new byte[1024];
//                byte[] buf = new byte[8192];

                        //从输入流中读取数据，写入到zip输出流
                        while ((len = bais.read(buf)) != -1) {
                            zos.write(buf, 0, len);
                        }
                        bais.close();
                        zos.closeEntry();
                    }
                }
            }
            zos.flush();
            zos.close();
            File zip = new File(zipPath + File.separator + special.getSpecialId() + ".zip");
            long zipLength = zip.length();

            // 获取文件名
            String filename = zip.getName();
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(zip);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            //删除zip本地文件
            if (zip.exists()) {
                zip.delete();
            }
            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, CHARACTER_SET));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + zipLength);
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            log.error(MeasureResponseConstant.DOWNLOAD_ERROR, ex);
        }
        // 查询专题下的切片
        // 根据切片查询数据，放入一个jsonObject
//        return R.ok("ok");

    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取标注列表")
    @GetMapping("/selectList")
    public R<List<Marking>> selectList(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail("参数异常");
        }
//        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.matchQuery("slideId", slideId)).mustNot(QueryBuilders.matchQuery("location_type", "Point"));
//        // 数据分页
////        if (pageNum > 0) {
////            Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
////            query.withPageable(pageable);
////        }
//        query.withSort(Sort.by(DESC, "marking_id"));
//        query.withQuery(boolQueryBuilder);
//        SearchHits<GeometryDoc> searchHits = elasticsearchRestTemplate.search(query.build(), GeometryDoc.class);
//
//        List<GeometryDoc> blogs = new ArrayList<>();
//        for (SearchHit<GeometryDoc> searchHit : searchHits) {
//            blogs.add(searchHit.getContent());
//        }

        List<Marking> markingList = markingService.selectList(slideId);

        List<PointCount> pointCounts = markingService.selectCategoryCountList(slideId);
        for (PointCount pointCount : pointCounts) {
            Marking marking = new Marking();
            marking.setCategory_id(pointCount.getCategory_id());
            marking.setPoint_count(pointCount.getPoint_count());
            marking.setLabel_name(pointCount.getLabel_name());
            marking.setMeasure_name("P");
            // 根据category_id查询标签信息
            if (pointCount.getCategory_id() != 0) {
                PathologicalIndicatorCategory category = pathologicalIndicatorCategoryService.selectByPrimaryKey(pointCount.getCategory_id());
                marking.setLabel_name(category.getCategoryName());
                if (category.getCategoryName() != null) {
                    marking.setMeasure_full_name("P" + "_" + category.getCategoryName());
                }
            } else {
                marking.setMeasure_full_name("P");
            }
            markingList.add(0, marking);
        }
        // 查询
        return R.ok(markingList);
    }


    public R<List<Map<String, Object>>> selectPoint(Long slideId) {
        // 组装Builder
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // 关键词筛选
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 模糊匹配
//        boolQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery("productName", keyword));
        // must 精准匹配
        boolQueryBuilder.must(QueryBuilders.matchQuery("slideId", slideId)).must(QueryBuilders.matchQuery("measure_name", "P"));

        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);

        // 分组。terms分组名称、field分组字段、size分组数量
        TermsAggregationBuilder builder = AggregationBuilders.terms("label_name").field("label_name").size(9999);
        nativeSearchQueryBuilder.addAggregation(builder);

        // 数据分页。需求是聚合，数据本身我并不需要，所以只返回一条数据就行
        Pageable pageable = PageRequest.of(0, 1);
        nativeSearchQueryBuilder.withPageable(pageable);

        // 查询 实体类上需要有Document注解
        SearchHits<GeometryDoc> searchHits = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), GeometryDoc.class);
        // 解析
        Aggregations aggregations = (Aggregations) searchHits.getAggregations().aggregations();
        Aggregation aggregation = aggregations.get("label_name");
        // 通过debug可以看到aggregation.getBuckets里就是我所需要的分组信息，但是直接.出不来，需要手动拼出来并强转一下(ParsedLongTerms)
        List<? extends Terms.Bucket> buckets = ((ParsedStringTerms) aggregation).getBuckets();
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < buckets.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            Terms.Bucket bucket = buckets.get(i);
            // 每组的key
            String label_name = (String) bucket.getKey();
            long docCount = bucket.getDocCount();
            map.put("label_name", label_name);
            map.put("count", docCount);
            list.add(map);
        }
        return R.ok(list);
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取标注数据")
    @GetMapping("/select")
    public R<List<GeometryDoc>> select(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) {

        TermQueryBuilder res = QueryBuilders.termQuery("slide_id", slideId);

        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (slideId != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("slideId", slideId));
        }
        query.withQuery(boolQueryBuilder);
        SearchHits<GeometryDoc> searchHits = elasticsearchRestTemplate.search(query.build(), GeometryDoc.class);

        List<GeometryDoc> blogs = new ArrayList<>();
        for (SearchHit<GeometryDoc> searchHit : searchHits) {
            blogs.add(searchHit.getContent());
        }
        return R.ok(blogs);

    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取标注数据")
    @GetMapping("/getAnnotation")
    public R<JSONObject> fetchGeojsonUrl(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail("参数异常");
        }
        Slide slideBy = slideService.getById(slideId);
        if (slideBy == null) {
            return R.fail("未发现切片信息");
        }
        if (Optional.ofNullable(slideBy.getGeojsonUrl()).isPresent()) {
            return R.ok(getAnnotation(slideBy.getGeojsonUrl()));
        }
        return R.ok(new JSONObject());
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "添加标注")
    @PostMapping("/add")
    public R<Long> add(@Validated @RequestBody viewAddIn req) throws Exception {

        // 添加标注时更新key过期时间
        redisClientUtil.updateHashTime(String.valueOf(req.getSlide_id()));

        // 查询切片表中切片信息
        Slide slide = slideService.getById(req.getSlide_id());
        if (slide == null) {
            return R.fail("未查询到切片信息");
        }
        Double area = new Double(req.getArea()) * MICRON;
        Double perimeter = new Double(req.getPerimeter()) * MICRON;
        req.setArea(decimalFormat.format(area));
        req.setPerimeter(decimalFormat.format(perimeter));
        Marking marking = new Marking();
        BeanUtils.copyProperties(req, marking);
        // 拼接标注名称
        String numKey = req.getSlide_id() + "_" + req.getMeasure_name();
        Long numId = redisClientUtil.getAndAddLong("labelNameNum:" + numKey, 1L);
        // 获取标注名称
        String measure_full_name = req.getMeasure_name() + numId + "_";
        String label_color = null;
        String label_name = null;
        if (req.getCategory_id() != null) {
            //根据标注id获取标注类别详情
            PathologicalIndicatorCategory categoryBy = pathologicalIndicatorCategoryService.selectByPrimaryKey(req.getCategory_id());
            if (categoryBy != null) {
                label_color = categoryBy.getColor();
                label_name = categoryBy.getCategoryName();
                measure_full_name += label_name;
            }
        }
        String annotationId = CustomizationIdUtils.getSdId();
        marking.setAnnotation_id(annotationId);
        marking.setCreate_by(SecurityUtils.getUserId());
        marking.setMeasure_full_name(measure_full_name);
        marking.setAnnotation_type("Draw");
        // 添加数据库，添加后返回自增id
        markingService.insert(marking);

        if (Objects.equals(req.getLocation_type(), "Point")) {
            PointCount pointCounts = markingService.selectCategoryCount(marking);
            marking.setPoint_count(pointCounts.getPoint_count());
            // 删除后更新标注点数据
            markingService.updatePointCount(marking);
        }
        // 写入文件中
        // 获取geojsonUrl地址
        String geojsonUrl = viewerService.getGeojsonUrls(slide.getGeojsonUrl(), slide.getSlideId());
        slide.setGeojsonUrl(geojsonUrl);
        // 更新slide表中geojsonUrl地址
        slideService.updateById(slide);
        // 构建完整信息
        Marking markingBy = markingService.selectById(marking.getMarking_id());
        Properties properties = new Properties();
        BeanUtils.copyProperties(markingBy, properties);
        properties.setMeasure_full_name(measure_full_name);
        properties.setLabel_color(label_color);
        properties.setLabel_name(label_name);
        properties.setCreate_time(markingBy.getCreate_time());
        properties.setAnnotation_owner(SecurityUtils.getUsername());
        properties.setMeasure_name(req.getMeasure_name());

        // 将geo写入es中
        GeometryDoc geometryDoc = new GeometryDoc();
        geometryDoc.setMarking_id(marking.getMarking_id());
        String jsonStr = req.getGeometry().toString();
        geometryDoc.setGeometry(jsonStr);
        geometryDoc.setSlideId(req.getSlide_id());
        geometryDoc.setCreate_time(markingBy.getCreate_time());
        BeanUtils.copyProperties(properties, geometryDoc);
//        org.json.JSONObject jsonObject = new org.json.JSONObject(jsonStr);
        geometryDocMapper.save(geometryDoc);
        //发送websocket
        Features features = new Features();
        features.setGeometry(req.getGeometry());
        features.setId(annotationId);
        features.setType("Feature");
        features.setProperties(properties);

        List<PointCount> pointCountList = new ArrayList<>();
        // 判断是否需要发送点总数
        if (Objects.equals(req.getLocation_type(), "Point")) {
            // 查询总点数
            PointCount pointCounts = markingService.selectCategoryCount(marking);
            marking.setPoint_count(pointCounts.getPoint_count());
            // 添加后更新标签点数
            markingService.updatePointCount(marking);
            // 查询点总数

            PointCount pointCount = markingService.selectCategoryCount(markingBy);
            pointCountList.add(pointCount);
        }

        // 写入文件
//        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(features));
        FileUtils.addGeojson(features, geojsonUrl, req.getSlide_id());
        // 更新点数量
        updatePointCount(features, slide.getGeojsonUrl());
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(ADD_STATUS, features, pointCountList);
        NioWebSocketHandler.sendAll(req.getSlide_id(), broadcastVO);
        return R.ok(markingBy.getMarking_id(), ResponseConstant.OPERATE_SUCCEED);
    }

//    @ApiOperationSupport(author = "gjt")
//    @ApiOperation(value = "添加标注")
//    @PostMapping("/add")
//    public R<GeoMarking> add(@Validated @RequestBody viewAddIn req) {
//
//        // 添加标注时更新key过期时间
//        redisClientUtil.updateHashTime(String.valueOf(req.getSlide_id()));
//
//        // 查询切片表中切片信息
//        Slide slide = slideService.getById(req.getSlide_id());
//        if (slide == null) {
//            return R.fail("未查询到切片信息");
//        }
//        // 获取geojsonUrl地址
//        String geojsonUrl = viewerService.getGeojsonUrls(slide.getGeojsonUrl(), slide.getSlideId());
//        slide.setGeojsonUrl(geojsonUrl);
//        // 更新slide表中geojsonUrl地址
//        slideService.updateById(slide);
//        // 向json文件添加数据
//        GeoMarking marking = viewerService.constructAddMarking(req);
//        FileUtils.addGeojson(marking, geojsonUrl, req.getSlide_id());
//        return R.ok(marking);
//    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "删除标注")
    @ApiImplicitParams({@ApiImplicitParam(name = "markingId", value = "标注id", required = true, dataType = "Long", paramType = "query")})
    @PostMapping("/del")
    public R<String> del(Long marking_id) throws Exception {

        if (!Optional.ofNullable(marking_id).isPresent()) {
            return R.fail("参数异常");
        }
        // 查询标注表中信息
        Marking markingBy = markingService.selectById(marking_id);

        if (!Optional.ofNullable(markingBy).isPresent()) {
            return R.fail("未查询到标注信息");
        }
        Slide slide = slideService.getById(markingBy.getSlide_id());
        if (!Optional.ofNullable(slide).isPresent()) {
            return R.fail("未查询到切片信息");
        }
        // 删除前查询详情数据
        markingService.delete(marking_id);
        Optional<GeometryDoc> search = geometryDocMapper.findById(String.valueOf(marking_id));
        GeometryDoc geometryDoc = search.orElse(null);
        String geometry = null;
        if (geometryDoc != null) {
            geometry = search.get().getGeometry();
        }
        com.alibaba.fastjson.JSONObject geometryJson = new com.alibaba.fastjson.JSONObject(Boolean.parseBoolean(geometry));
        // 删除es中标注数据
        geometryDocMapper.deleteById(String.valueOf(marking_id));
        // 删除后根据id查询标注详情
        String label_color = null;
        String label_name = null;
        if (markingBy.getCategory_id() != null) {
            //根据标注id获取标注类别详情
            PathologicalIndicatorCategory categoryBy = pathologicalIndicatorCategoryService.selectByPrimaryKey(markingBy.getCategory_id());
            if (categoryBy != null) {
                label_color = categoryBy.getColor();
                label_name = categoryBy.getCategoryName();
            }
        }
        //发送websocket
        Properties properties = new Properties();
        BeanUtils.copyProperties(markingBy, properties);
        properties.setLabel_color(label_color);
        properties.setLabel_name(label_name);
        Features features = new Features();
        features.setGeometry(geometryJson);
        features.setId(markingBy.getAnnotation_id());
        features.setType("Feature");
        features.setProperties(properties);

        List<PointCount> pointCountList = new ArrayList<>();
        // 判断是否为点类型
        if (Objects.equals(markingBy.getLocation_type(), "Point")) {
            // 更新总点数
            PointCount pointCounts = markingService.selectCategoryCount(markingBy);
            markingBy.setPoint_count(pointCounts.getPoint_count());
            // 删除后更新标注点数据
            markingService.updatePointCount(markingBy);
            // 查询点总数
            PointCount pointCount = markingService.selectCategoryCount(markingBy);
            pointCountList.add(pointCount);

            Features features1 = new Features();
            Properties properties1 = new Properties();
            properties1.setCategory_id(markingBy.getCategory_id());
            properties1.setPoint_count(pointCounts.getPoint_count());
            properties1.setLocation_type(markingBy.getLocation_type());
            features1.setProperties(properties1);
            updatePointCount(features1, slide.getGeojsonUrl());
        }

        // 删除标注信息
        boolean res = delGeojson(marking_id, slide.getGeojsonUrl());
        if (!res) {
            return R.fail("删除标注失败");
        }
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(DELETE_STATUS, features, pointCountList);
        // 使用websocket发送数据
        NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
        // 发送websocket
        return R.ok("操作成功");
    }


//    /**
//     * 更新viewer页面上数据
//     *
//     * @param req 传入参数
//     * @return String
//     */
//    @ApiOperationSupport(author = "gjt")
//    @ApiOperation(value = "更新标注")
//    @PostMapping("/update")
//    public R<Properties> update(@Validated @RequestBody MarkingUpdateIn req) throws Exception {
//        // 查询标注表中信息
//        Slide slide = slideService.getById(req.getSlide_id());
//        if (!Optional.ofNullable(slide).isPresent()) {
//            return R.fail("未查询到切片信息");
//        }
//        if (!Optional.ofNullable(slide.getGeojsonUrl()).isPresent()) {
//            return R.fail("未查询到标注信息");
//        }
//        // 查找详情数据
//        GeoMarking marking1 = selectById(req.getAnnotation_id(),slide.getGeojsonUrl());
//        GeoMarking marking = viewerService.constructUpdMarking(req, marking1.getProperties().getMeasure_full_name());
//        updateGeojson(marking, slide.getGeojsonUrl());
//        GeoMarking marking2 = selectById(req.getAnnotation_id(),slide.getGeojsonUrl());
//        return R.ok(marking2.getProperties());
//    }


    /**
     * 更新viewer页面上数据
     *
     * @param req 传入参数
     * @return String
     */
    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "更新标注")
    @PostMapping("/update")
    public R<Long> update(@Validated @RequestBody MarkingUpdateIn req) throws Exception {

        // 查询标注表中信息
        Marking markingBy = markingService.selectById(req.getMarking_id());
        if (!Optional.ofNullable(markingBy).isPresent()) {
            return R.fail("未查询到标注信息");
        }
        // 查询标注表中信息
        Slide slide = slideService.getById(markingBy.getSlide_id());
        if (!Optional.ofNullable(slide).isPresent()) {
            return R.fail("未查询到切片信息");
        }
        if (!Optional.ofNullable(slide.getGeojsonUrl()).isPresent()) {
            return R.fail("未查询到标注信息");
        }
        Double area = new Double(req.getArea()) * MICRON;
        Double perimeter = new Double(req.getPerimeter()) * MICRON;
        req.setArea(decimalFormat.format(area));
        req.setPerimeter(decimalFormat.format(perimeter));
        // 更新前数据
        // 更新文件中的内容
        String measureFullName = markingBy.getMeasure_full_name();
        Marking marking = new Marking();
        BeanUtils.copyProperties(req, marking);
        PathologicalIndicatorCategory categoryBy1 = pathologicalIndicatorCategoryService.selectByPrimaryKey(markingBy.getCategory_id());
        String label_color = null;
        if (categoryBy1 != null) {
            label_color = categoryBy1.getColor();
        }
        String label_name = null;
        if (req.getCategory_id() != null) {
            //根据标注id获取标注类别详情
            PathologicalIndicatorCategory categoryBy = pathologicalIndicatorCategoryService.selectByPrimaryKey(req.getCategory_id());
            if (req.getCategory_id() == 0) {
                String res1 = String.valueOf(measureFullName.charAt(measureFullName.length() - 1));
                if (!res1.equals("_")) {
                    measureFullName = measureFullName.split("_")[0] + "_";
                }
            }
            if (categoryBy != null) {
                label_color = categoryBy.getColor();
                label_name = categoryBy.getCategoryName();
                String res1 = String.valueOf(measureFullName.charAt(measureFullName.length() - 1));
                if (res1.equals("_")) {
                    measureFullName = measureFullName + label_name;
                } else {
                    measureFullName = measureFullName.split("_")[0] + "_" + label_name;
                }
            }
        }
        marking.setMeasure_full_name(measureFullName);
        markingService.update(marking);

        Marking markingBys = markingService.selectById(req.getMarking_id());
        // 更新更改前的标注点数
        // 查询点数量
        // 查询旧标签id
        List<PointCount> pointCountList = new ArrayList<>();
        if (Objects.equals(markingBys.getLocation_type(), "Point")) {
            if (!Objects.equals(req.getCategory_id(), markingBy.getCategory_id())) {
                PointCount pointCount1 = markingService.selectCategoryCount(markingBys);
                markingBys.setPoint_count(pointCount1.getPoint_count());
                markingService.updatePointCount(markingBys);
                pointCountList.add(pointCount1);
            }
            PointCount newPointCount = markingService.selectCategoryCount(markingBy);
            newPointCount.setCategory_id(markingBy.getCategory_id());
            markingBy.setPoint_count(newPointCount.getPoint_count());
            markingService.updatePointCount(markingBy);
            pointCountList.add(newPointCount);
        }
        // 查询更新后的标注信息
        // 更新到es中
        GeometryDoc geometryDoc = new GeometryDoc();
        BeanUtils.copyProperties(markingBys, geometryDoc);
        // 更新es中数据
        geometryDoc.setMarking_id(marking.getMarking_id());
        if (req.getGeometry() != null) {
            String jsonStr = req.getGeometry().toString();
            geometryDoc.setGeometry(jsonStr);
        }
        geometryDocMapper.save(geometryDoc);
        //发送websocket

        Marking markingBy2 = markingService.selectById(req.getMarking_id());
        Properties properties = new Properties();
        BeanUtils.copyProperties(markingBy2, properties);
        properties.setLabel_color(label_color);
        properties.setLabel_name(label_name);
        properties.setMeasure_full_name(measureFullName);
        properties.setAnnotation_type(marking.getAnnotation_type());
        properties.setAnnotation_owner(SecurityUtils.getUsername());
        properties.setCreate_time(markingBy.getCreate_time());
        Features features = new Features();
        features.setGeometry(req.getGeometry());
        features.setId(markingBy.getAnnotation_id());
        features.setType("Feature");
        features.setProperties(properties);


        // 查找点类型
//        GeoMarking markingBy1 = viewerService.constructUpdMarking(req, marking1.getProperties().getMeasure_full_name());
        updateGeojson(features, slide.getGeojsonUrl());

        if (Objects.equals(markingBy.getLocation_type(), "Point")) {
            // 更新前点数
            Features features1 = new Features();
            Properties properties1 = new Properties();
            properties1.setCategory_id(markingBy.getCategory_id());
            features1.setProperties(properties1);
            updatePointCount(features1, slide.getGeojsonUrl());
            // 更新后点数
            updatePointCount(features, slide.getGeojsonUrl());
        }
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(UPDATE_STATUS, features, pointCountList);
        // 使用websocket发送数据
        NioWebSocketHandler.sendAll(markingBy.getSlide_id(), broadcastVO);
        return R.ok(req.getMarking_id(), ResponseConstant.OPERATE_SUCCEED);
    }


    /**
     * 导出用户人工绘制得标注
     *
     * @param slideId 切片id
     * @return String
     */
    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "导出测量json")
    @GetMapping("/json")
    public void json(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId, @RequestParam(value = "status") @ApiParam(name = "status", value = "状态", required = true) Long status) throws Exception {
        if (!Optional.ofNullable(slideId).isPresent()) {
            throw new Exception("参数异常");
        }
        Slide slide = slideService.getById(slideId);
        if (!Optional.ofNullable(slide).isPresent()) {
            throw new Exception("为查询到切片信息");
        }
        JSONObject jsons = exportJson(slide.getGeojsonUrl(), status);
        Image image = imageService.selectById(slide.getImageId());
        GeoImage geoImage = new GeoImage();
        if (image != null) {
            geoImage.setCreate_time(image.getCreateTime());
            String type = String.valueOf((image.getImageName().split("\\.")[image.getImageName().split("\\.").length - 1]));
            geoImage.setImage_type(type);
            String name = image.getImageName().split("\\.")[0];
            geoImage.setImage_name(name);
            String shape = "(" + image.getWidth() + "," + image.getHeight() + ")";
            geoImage.setImage_shape(shape);
        }
        jsons.put("image", geoImage);
        // 查询项目详情
        ProjectInfoOut project = projectService.getProjectById(slide.getProjectId());
        GeoProject geoProject = new GeoProject();
        if (project != null) {
            geoProject.setProject_id(project.getProjectId());
            geoProject.setProject_name(geoProject.getProject_name());
        }
        jsons.put("project", geoProject);
        // 生成Json字符串
        String jsonString = JSON.toJSONString(jsons, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        jsonString = "geoJson:" + jsonString;
        // 以流的形式下载文件
        try {
            // 清空response
            response.reset();
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setCharacterEncoding(ExaminationConstant.CHARACTER_ENCODING);
            response.setContentType(ExaminationConstant.CONTENT_TYPE);
            response.setHeader(ExaminationConstant.HEADER, "attachment;filename=" + slide.getSlideId() + ".json");
            outputStream.write(jsonString.getBytes());
            // 关闭流
            outputStream.close();
        } catch (Exception e) {
            throw new Exception(MeasureResponseConstant.DOWNLOAD_ERROR);
        }
    }

    //    @Log(title = "标注测量excel导出", businessType = BusinessType.EXPORT)
    @ApiOperation(value = "标注测量excel导出")
    @GetMapping("/export")
    public R<String> export(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) throws Exception {

        Slide slide = slideService.getById(slideId);
        if (!Optional.ofNullable(slide).isPresent()) {
            return R.fail("未查询到切片信息");
        }
        if (!Optional.ofNullable(slide.getGeojsonUrl()).isPresent()) {
            return R.fail("暂无标注数据");
        }
        File file = new File(slide.getGeojsonUrl());
        String jsonData = getStr(file);
        JSONObject parse = (JSONObject) JSONObject.parse(jsonData);
        JSONArray features = parse.getJSONArray("features");
        HSSFWorkbook sheets = jsonToExcel(features);
        // 配置文件下载
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        response.setHeader("Content-Disposition", "attachment;filename=" + slide.getSlideId() + ".xls");
        OutputStream os = response.getOutputStream();
        sheets.write(os);
        sheets.close();
        os.close();
        return R.ok("操作成功");
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "编辑按钮")
    @PostMapping("/viewerEdit")
    public R<String> viewerEdit(Long slideId) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail("参数异常");
        }
        String slide = "slideId" + ":" + slideId;
        RKeys keys = redissonClient.getKeys();
        // 获取文件夹下所有key
        Iterable<String> keysByPattern = keys.getKeysByPattern("slideId:" + "*");
        for (String i : keysByPattern) {
            RMap<Object, Object> userName = redisClientUtil.getHash(i);
            // 根据名称获取用户token
            String userNameKey = CacheConstants.LOGIN_TOKEN_KEY + userName;
            String cacheObject = redisService.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY + userNameKey);
            // 如果token过期,删除存储用户的map
            if (StringUtils.isNull(cacheObject)) {
                // 删除redis中存储用户map
                redisClientUtil.delString(String.valueOf(i));
            }
        }
        RMap<Object, Object> rMap = redisClientUtil.getHash(slide);
        String userName = (String) rMap.get("userName");
        if (userName == null) {
            // 写入redis中
            redisClientUtil.setHash(slide, SecurityUtils.getUserId(), SecurityUtils.getUsername());
            return R.ok("准备编辑");
        } else {
            if (userName.equals(SecurityUtils.getUsername())) {
                return R.ok("准备编辑");
            }
            return R.fail("当前页面不可编辑");
        }
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "退出按钮")
    @PostMapping("/viewerQuit")
    public R<String> viewerQuit(Long slideId) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail("参数异常");
        }
        String slide = "slideId" + ":" + slideId;
        if (!redisClientUtil.delString(slide)) {
            return R.fail("退出编辑失败");
        }
        return R.ok("操作成功");
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "编辑状态")
    @PostMapping("/editStatus")
    public R<Map<String, Object>> editStatus(Long slideId) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail("参数异常");
        }
        String slide = "slideId" + ":" + slideId;
        RMap<Object, Object> rMap = redisClientUtil.getHash(slide);
        String userName = (String) rMap.get("userName");
        Long userId = (Long) rMap.get("userId");
        Map<String, Object> map = new HashMap<>();
        map.put("operatorId", userId);
        map.put("operatorName", userName);
        if (userName != null) {
            map.put("status", 1);
        } else {
            map.put("status", 0);
        }
        return R.ok(map);
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "websocket接口", hidden = true)
    @GetMapping("/getWebsocketPort")
    public R<String> getWebsocketPort() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return R.fail(ResponseConstant.OPERATE_ERROR);
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String localAdd = request.getLocalAddr();
        return R.ok("ws://" + localAdd + ":" + nettyPort + "/");
    }


}
