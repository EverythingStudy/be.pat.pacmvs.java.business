package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.AlgorithmAssessment;
import cn.staitech.anno.domain.AlgorithmJson;
import cn.staitech.anno.domain.assessment.in.CreateAssessmentDataIn;
import cn.staitech.anno.domain.assessment.in.CreateAssessmentIn;
import cn.staitech.anno.domain.assessment.in.GetAssessmentListIn;
import cn.staitech.anno.domain.assessment.in.RemoveAssessmentIn;
import cn.staitech.anno.domain.assessment.out.GetAssessmentListOut;
import cn.staitech.anno.mapper.AlgorithmAssessmentMapper;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.mapper.AlgorithmJsonMapper;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.service.AlgorithmAssessmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.staitech.anno.service.MarkingService;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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

    @Override
    public boolean zipExport(String zipUrl, Long projectId) throws Exception {
        StringBuilder sb;
        File file1 = new File(zipUrl);
        Map<String, String> ddlList = new HashMap<>();
        try {
            // 根据项目查询算法考核表中数据



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
                        // 获取文件中的内容
                        org.json.JSONObject jsonObject = new org.json.JSONObject(sb.toString());
                        // 获取图像相关信息
                        org.json.JSONObject image = jsonObject.getJSONObject("image");
                        if (image != null) {
                            // 获取标注名称
                            String imageName = image.getString("image_name");

                            String geoImageId = image.getString("image_id");
                            if (imageName != null) {
                                // 写入数据库
                                writeAlgorithm(projectId,imageName);
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

    public void writeAlgorithm(Long projectId, String imageName){
        QueryWrapper<AlgorithmAssessment> algorithmAssessmentQueryWrapper = new QueryWrapper<>();
        algorithmAssessmentQueryWrapper.eq("project_id",projectId).eq("del_flag","0");
        List<AlgorithmAssessment> algorithmAssessments = algorithmAssessmentMapper.selectList(algorithmAssessmentQueryWrapper);
        for(AlgorithmAssessment algorithmAssessment:algorithmAssessments){

        }

        //

    }

    @Autowired
    private MarkingService markingService;

    @Resource
    private AlgorithmJsonMapper algorithmJsonMapper;

    @Resource
    private SlideMapper slideMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R createAssessment(CreateAssessmentIn req) {
        log.info("生成算法考核接口开始：");

        List<CreateAssessmentDataIn> slideList = req.getSlideList();
        List<AlgorithmAssessment> algorithmAssessments = slideList.stream().map(e -> {
            AlgorithmAssessment resp = new AlgorithmAssessment();
            BeanUtils.copyProperties(e, resp);
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
            return resp;

        }).collect(Collectors.toList());

        saveBatch(algorithmAssessments);
        //修改切片是否生成状态
        return R.ok();
    }

    @Override
    public PageResponse<GetAssessmentListOut> getAssessmentList(GetAssessmentListIn req) {
        log.info("算法考核列表分页查询接口开始：");

        PageResponse resp = new PageResponse();

        LambdaQueryWrapper<AlgorithmAssessment> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(req.getImageName()), AlgorithmAssessment::getImageName, req.getImageName());
        qw.eq(AlgorithmAssessment::getProjectId, req.getProjectId());
        qw.eq(!ObjectUtils.isEmpty(req.getCategoryId()), AlgorithmAssessment::getCategoryId, req.getCategoryId());
        if (!CollectionUtils.isEmpty(req.getCreateTimeParams())) {
            try {
                Date date = DateUtils.addAndSubtractDaysByCalendar(DateUtils.stringToDate((String) req.getCreateTimeParams().get("endTime"), "yyyy-MM-dd"), 1);
                qw.lt(AlgorithmAssessment::getCreateTime, date);
                qw.ge(AlgorithmAssessment::getCreateTime, DateUtils.stringToDate((String) req.getCreateTimeParams().get("beginTime"), "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("时间入参格式转化异常-{}", e);
                throw new RuntimeException("时间入参格式转化异常!");
            }

        }

        Page<AlgorithmAssessment> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<AlgorithmAssessment> algorithmAssessments = this.baseMapper.selectList(qw);
        List<GetAssessmentListOut> collect = new ArrayList<>();

        if (!CollectionUtils.isEmpty(algorithmAssessments)) {
            LambdaQueryWrapper<AlgorithmJson> qw2 = new LambdaQueryWrapper<>();
            collect = algorithmAssessments.stream().map(e -> {
                GetAssessmentListOut resp2 = new GetAssessmentListOut();
                BeanUtils.copyProperties(e, resp);
                qw2.eq(AlgorithmJson::getAlgorithmAssessmentId, e.getAlgorithmAssessmentId());
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
