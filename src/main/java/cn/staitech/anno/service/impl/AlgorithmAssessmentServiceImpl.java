package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.AlgorithmAssessment;
import cn.staitech.anno.domain.AlgorithmJson;
import cn.staitech.anno.domain.assessment.in.*;
import cn.staitech.anno.domain.ParseJson;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.assessment.in.CreateAssessmentDataIn;
import cn.staitech.anno.domain.assessment.in.CreateAssessmentIn;
import cn.staitech.anno.domain.assessment.in.GetAssessmentListIn;
import cn.staitech.anno.domain.assessment.in.GetJsonInfoDataIn;
import cn.staitech.anno.domain.assessment.in.GetJsonInfoIn;
import cn.staitech.anno.domain.assessment.in.RemoveAssessmentIn;
import cn.staitech.anno.domain.assessment.out.AssessmentExportOut;
import cn.staitech.anno.domain.assessment.out.GetAssessmentListOut;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.domain.SlideAttr;
import cn.staitech.anno.project.mapper.ProjectMapperV1;
import cn.staitech.anno.project.mapper.SlideAttrMapper;
import cn.staitech.anno.service.AlgorithmAssessmentService;
import cn.staitech.anno.service.AlgorithmJsonService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.anno.utils.ParseJsonUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.date.DateUtils;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.StringUtils;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static cn.staitech.anno.aspect.LogFileAspect.response;
import static cn.staitech.anno.constant.CommonConstant.FILE_SUFFIX_JSON;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gjt
 * @since 2023-10-17
 */
@Service
@Slf4j
public class AlgorithmAssessmentServiceImpl extends ServiceImpl<AlgorithmAssessmentMapper, AlgorithmAssessment> implements AlgorithmAssessmentService {


    @Resource
    private AlgorithmAssessmentMapper algorithmAssessmentMapper;

    @Autowired
    private AlgorithmJsonService algorithmJsonService;

    @Autowired
    private MarkingService markingService;

    @Resource
    private AlgorithmJsonMapper algorithmJsonMapper;

    @Resource
    private AssessmentResultsMapper assessmentResultsMapper;

    @Resource
    private SlideMapper slideMapper;

    @Resource
    private ProjectMapperV1 projectMapperV1;

    @Autowired
    private SlideService slideService;

    @Resource
    private SlideAttrMapper slideAttrMapper;

    @Override
    public boolean zipExport(String zipUrl, Long projectId) throws Exception {
        StringBuilder sb;
        File file1 = new File(zipUrl);
        Map<String, String> ddlList = new HashMap<>();
        try {
            //zip可以包含对个文件，如果只有一个文件，则只解析一个文件的，包含多个文件则分别解析
            //必须指明读取的各式，不然会存在问题
            ZipFile zipFile = new ZipFile(file1, Charset.forName("gbk"));
            //按流的方式读取文件，输入到管道中
            InputStream in = new BufferedInputStream(Files.newInputStream(file1.toPath()));
            //字节流转换为压缩文件输入流，通常用来读取压缩文件
            ZipInputStream zp = new ZipInputStream(in);
            //定义文件条目
            ZipEntry ze;
            Enumeration<? extends ZipEntry> zipEnum = zipFile.entries();
            // 循环压缩包中解压内容
            while (zipEnum.hasMoreElements()) {
                // 获取下一个元素
                ze = zipEnum.nextElement();
                sb = new StringBuilder();
                if (!ze.isDirectory()) {
                    long size = ze.getSize();
                    if (size > 0) {
                        //读取文件内容
                        BufferedReader bf = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze), StandardCharsets.UTF_8));
                        String line;
                        while ((line = bf.readLine()) != null) {
                            sb.append(line);
                        }
                        // 获取文件内容并转换成string
                        String fileContent = sb.toString();
                        // 获取文件中的内容
                        org.json.JSONObject jsonObject = new org.json.JSONObject(fileContent);
                        // 获取图像相关信息
                        org.json.JSONObject image = jsonObject.getJSONObject("image");
                        // 获取标签相关信息
                        org.json.JSONArray labelInfo = jsonObject.getJSONArray("label_info");
                        // 标签列表长度超出一个，抛出异常
                        if (labelInfo.length() > 1) {
                            throw new Exception("文件中存在多个标签，解析失败");
                        }
                        if (image != null) {
                            // 获取标注名称
                            String imageName = image.getString("image_name");

                            if (imageName != null) {
                                // 写入数据库
                                writeAlgorithm(projectId, imageName, fileContent);
                            }
                            //这里是对读取的文件内容进行处理
                            ddlList.put(ze.getName(), sb.toString());
                            bf.close();
                        }
                    }
                }
                zp.closeEntry();
            }
        } catch (Exception e) {
            throw new Exception("json文件解析失败");
        }
        return true;
    }


    @Override
    public R getJsonInfo(GetJsonInfoIn req) {
        log.info("获取json数据接口开始：");
        List<GetJsonInfoDataIn> reqList = req.getReqList();
        List<AlgorithmJson> jsonReq = new ArrayList<>();
        for (GetJsonInfoDataIn getJsonInfoDataIn : reqList) {

            String s = StringUtils.substringAfterLast(getJsonInfoDataIn.getAlgorithmJsonName(), ".");
            if (!"json".equals(s)) {
                return R.fail("文件格式异常！");
            }
            //读取文件解析数据校验
            ParseJson parseJson = new ParseJson();
            try {
                parseJson = ParseJsonUtil.parseJson(getJsonInfoDataIn.getAlgorithmJsonUrl(), 0);
            } catch (IOException e) {
                e.printStackTrace();
                log.info("json格式解析异常" + e.toString());
                return R.fail("json格式解析异常");
            }
            int labels = parseJson.getLabels();
            if (labels != 1) {
                return R.fail("json结构指标不唯一！");
            }
            LambdaQueryWrapper<AlgorithmAssessment> qw = new LambdaQueryWrapper<>();
            qw.eq(AlgorithmAssessment::getImageName, getJsonInfoDataIn.getAlgorithmJsonName());
            qw.eq(AlgorithmAssessment::getProjectId, req.getProjectId());
            List<AlgorithmAssessment> algorithmAssessments = this.baseMapper.selectList(qw);
            if (CollectionUtils.isEmpty(algorithmAssessments)) {
                return R.fail("项目下无此切片算法考核信息！");
            }
            extracted(jsonReq, getJsonInfoDataIn, algorithmAssessments);
        }
        algorithmJsonService.saveBatch(jsonReq);
        return R.ok();
    }

    /**
     * json数据参数构建
     * @param jsonReq
     * @param getJsonInfoDataIn
     * @param algorithmAssessments
     */
    private void extracted(List<AlgorithmJson> jsonReq, GetJsonInfoDataIn getJsonInfoDataIn, List<AlgorithmAssessment> algorithmAssessments) {

        AlgorithmAssessment algorithmAssessment = algorithmAssessments.get(0);
        AlgorithmJson algorithmJson = new AlgorithmJson();
        algorithmJson.setAlgorithmAssessmentId(algorithmAssessment.getAlgorithmAssessmentId());
        algorithmJson.setSlideId(algorithmJson.getSlideId());
        algorithmJson.setAlgorithmJsonName(getJsonInfoDataIn.getAlgorithmJsonName());
        algorithmJson.setAlgorithmJsonUrl(getJsonInfoDataIn.getAlgorithmJsonUrl());
        algorithmJson.setCreateBy(SecurityUtils.getUserId());
        algorithmJson.setCreateTime(new Date());
        algorithmJson.setJsonType("0");
        LambdaQueryWrapper<AlgorithmJson> qw = new LambdaQueryWrapper<>();
        qw.eq(AlgorithmJson::getAlgorithmJsonUrl,getJsonInfoDataIn.getAlgorithmJsonUrl());
        qw.eq(AlgorithmJson::getAlgorithmAssessmentId,algorithmAssessment.getAlgorithmAssessmentId());
        List<AlgorithmJson> algorithmJsons = algorithmJsonMapper.selectList(qw);
        if(CollectionUtils.isEmpty(algorithmJsons)){
            jsonReq.add(algorithmJson);
        }

    }

        public void writeAlgorithm(Long projectId, String imageName, String fileContent) throws Exception {
        QueryWrapper<AlgorithmAssessment> algorithmAssessmentQueryWrapper = new QueryWrapper<>();
        algorithmAssessmentQueryWrapper.eq("project_id", projectId).eq("del_flag", "0");
        // 查询算法考核列表，获取算法考核列表
        algorithmAssessmentQueryWrapper.eq("project_id", projectId).eq("del_flag", "0");
        List<AlgorithmAssessment> algorithmAssessments = algorithmAssessmentMapper.selectList(algorithmAssessmentQueryWrapper);
        for (AlgorithmAssessment algorithmAssessment : algorithmAssessments) {
            // 判断图片名称是否与json文件中图片名称是否一致
            if (Objects.equals(algorithmAssessment.getImageName(), imageName)) {
                // 根据切片获取文件路径
                String algorithmJsonName = String.valueOf(algorithmAssessment.getSlideId());
                String fileUrl = "D:\\home\\pat_saas\\Data\\test\\" + algorithmJsonName + FILE_SUFFIX_JSON;
                // 创建文件
                createFile(fileUrl);
                // 写入文件
                exportJson(fileUrl, fileContent);
                // 写入文件后更新算法json表中数据
                algorithmJsonMapper.insert(setAlgorithmJson(algorithmAssessment, fileUrl, algorithmJsonName));
            }
        }
    }


    public AlgorithmJson setAlgorithmJson(AlgorithmAssessment algorithmAssessment, String fileUrl, String algorithmJsonName) {
        AlgorithmJson algorithmJson = new AlgorithmJson();
        algorithmJson.setAlgorithmAssessmentId(algorithmAssessment.getAlgorithmAssessmentId());
        algorithmJson.setSlideId(algorithmAssessment.getSlideId());
        algorithmJson.setAlgorithmJsonUrl(fileUrl);
        algorithmJson.setAlgorithmJsonName(algorithmJsonName);
        algorithmJson.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
        algorithmJson.setCreateTime(new Date());
        return algorithmJson;
    }


    private Boolean createFile(String url) throws Exception {
        File file = new File(url);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new Exception(MessageSource.M("FILE_DOWNLOAD_ERROR"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void exportJson(String fileUrl, String jsonString) {
        try {
            OutputStream outputStream = Files.newOutputStream(Paths.get(fileUrl));
            outputStream.write(jsonString.getBytes());
            // 关闭流
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void export(AssessmentExportIN assessmentExportIN) throws Exception {
        if(assessmentExportIN.getAlgorithmentList().size() < 1){
            throw new Exception("未选择切片");
        }
        List<AssessmentExportOut> assessmentExportOutList = assessmentResultsMapper.selectExportList(assessmentExportIN);
        // 查询项目中得信息
        Project projectBy = projectMapperV1.selectById(assessmentExportIN.getProjectId());
        String projectName = "";
        if (projectBy != null) {
            projectName = projectBy.getProjectName();
        }
        // 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = getTitleList(CommonConstant.ALGORITHMASSESSMENT_COLHEAD_KEY, CommonConstant.ALGORITHMASSESSMENT_COLHEAD_VALUE);
        ExcelTool excelTool = new ExcelTool<>(MessageSource.M("EXCEL_FILE_PATH"), 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(projectName, "UTF-8") + CommonConstant.FILE_SUFFIX_XLSX);
        excelTool.exportExcel(titleData, assessmentExportOutList, response.getOutputStream(), true, false);
//
    }

    public List<Map<String, String>> getTitleList(String[] colHeadKey, String[] colHeadValue) {
        // 定义表头
        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < colHeadKey.length; i++) {
            Map<String, String> map = new HashMap<String, String>(1);
            map.put(colHeadKey[i], colHeadValue[i]);
            list.add(map);
        }
        return list;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R createAssessment(CreateAssessmentIn req) {
        log.info("生成算法考核接口开始：");

        List<CreateAssessmentDataIn> slideList = req.getSlideList();
        List<Slide> qw = new ArrayList<>();
        List<AlgorithmAssessment> algorithmAssessments = slideList.stream().map(e -> {
            AlgorithmAssessment resp = new AlgorithmAssessment();
            Slide slide = new Slide();
            slide.setSlideId(e.getSlideId());
            slide.setIfCreateQuestions("1");
            BeanUtils.copyProperties(e, resp);
            resp.setImageName(e.getImageCode());
            resp.setCreateBy(SecurityUtils.getUserId());
            resp.setCreateTime(new Date());
            // 插入json文件返回数据
            String urlPath;
            try {
                urlPath = markingService.slideJsonExport(e.getSlideId());
            } catch (Exception exception) {
                log.error(exception.toString());
                throw new RuntimeException(MessageSource.M("ERROR_GENERATE_JSON"));
            }
            String s = StringUtils.substringAfterLast(urlPath, File.separator);
            resp.setAnnotationJsonName(s);
            resp.setAnnotationJsonUrl(urlPath);
            //设置标注类别
            LambdaQueryWrapper<SlideAttr> qw2 = new LambdaQueryWrapper<>();
            qw2.eq(SlideAttr::getSlideId,e.getSlideId());
            qw2.eq(SlideAttr::getAttrType,"2");
            qw2.eq(SlideAttr::getDelFlag,"0");
            List<SlideAttr> slideAttrs = slideAttrMapper.selectList(qw2);
            if(!CollectionUtils.isEmpty(slideAttrs)){
                StringBuilder sb = new StringBuilder();
                for (SlideAttr slideAttr : slideAttrs) {
                    sb.append(slideAttr.getAttrId());
                }
                resp.setCategoryIds(sb.toString());
            }
            qw.add(slide);
            return resp;

        }).collect(Collectors.toList());
        //批量插入考核算法
        saveBatch(algorithmAssessments);
        List<AlgorithmJson> collect = algorithmAssessments.stream().map(e -> {
            AlgorithmJson resp = new AlgorithmJson();
            resp.setAlgorithmAssessmentId(e.getAlgorithmAssessmentId());
            resp.setSlideId(e.getSlideId());
            resp.setAlgorithmJsonName(e.getAnnotationJsonName());
            resp.setAlgorithmJsonUrl(e.getAnnotationJsonUrl());
            resp.setCreateBy(SecurityUtils.getUserId());
            resp.setCreateTime(new Date());
            resp.setJsonType("0");
            return resp;
        }).collect(Collectors.toList());
        //批量插入json表
        algorithmJsonService.saveBatch(collect);
        //修改切片是否生成状态
        slideService.updateBatchById(qw);
        return R.ok();
    }


    @Override
    public PageResponse<GetAssessmentListOut> getAssessmentList(GetAssessmentListIn req) {
        log.info("算法考核列表分页查询接口开始：");

        PageResponse resp = new PageResponse();
        //算法考核参数构建
        LambdaQueryWrapper<AlgorithmAssessment> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(req.getImageName()), AlgorithmAssessment::getImageName, req.getImageName());
        qw.eq(AlgorithmAssessment::getProjectId, req.getProjectId());
        qw.apply(!ObjectUtils.isEmpty(req.getCategoryId()),"(find_in_set("+req.getCategoryId()+",category_ids))");
        //qw.eq(!ObjectUtils.isEmpty(req.getCategoryId()), AlgorithmAssessment::getCategoryId, req.getCategoryId());
        if (!CollectionUtils.isEmpty(req.getCreateTimeParams())) {
            Date date = DateUtils.addAndSubtractDaysByCalendar(req.getCreateTimeParams().get("endTime"), 1);
            qw.lt(AlgorithmAssessment::getCreateTime, date);
            qw.ge(AlgorithmAssessment::getCreateTime, req.getCreateTimeParams().get("beginTime"));
        }

        Page<AlgorithmAssessment> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<AlgorithmAssessment> algorithmAssessments = this.baseMapper.selectList(qw);
        //算法json数据查询
        List<GetAssessmentListOut> collect = new ArrayList<>();

        if (!CollectionUtils.isEmpty(algorithmAssessments)) {

            collect = algorithmAssessments.stream().map(e -> {
                GetAssessmentListOut resp2 = new GetAssessmentListOut();
                resp2.setCategoryIds(e.getCategoryIds().split(","));
                BeanUtils.copyProperties(e, resp2);
                resp2.setImageCode(e.getImageName());
                LambdaQueryWrapper<AlgorithmJson> qw2 = new LambdaQueryWrapper<>();
                qw2.eq(AlgorithmJson::getAlgorithmAssessmentId, e.getAlgorithmAssessmentId());
                qw2.eq(AlgorithmJson::getJsonType,"1");
                List<AlgorithmJson> algorithmJsons = algorithmJsonMapper.selectList(qw2);
                if (!CollectionUtils.isEmpty(algorithmJsons)) {
                    resp2.setAlgorithmJsonNames(algorithmJsons.stream().map(AlgorithmJson::getAlgorithmJsonName).collect(Collectors.toList()));
                }

                return resp2;
            }).collect(Collectors.toList());
        }


        resp.setTotal(page.getTotal());
        resp.setList(collect);
        resp.setPages(page.getPages());


        return resp;
    }


    @Override
    public R removeAssessment(RemoveAssessmentIn req) {
        log.info("算法考核数据删除家口开始：");
        LambdaQueryWrapper<AlgorithmJson> qw = new LambdaQueryWrapper<>();
        qw.eq(AlgorithmJson::getAlgorithmAssessmentId, req.getAlgorithmAssessmentId());
        List<AlgorithmJson> algorithmJsons = algorithmJsonMapper.selectList(qw);

        if (!CollectionUtils.isEmpty(algorithmJsons)) {
            R.fail("有上传的JSON文件的切片禁止删除");
        }
        AlgorithmAssessment entity = new AlgorithmAssessment();
        entity.setAlgorithmAssessmentId(req.getAlgorithmAssessmentId());
        entity.setDelFlag("1");
        this.baseMapper.updateById(entity);
        return R.ok();
    }

}
