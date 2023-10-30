package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.domain.*;
import cn.staitech.anno.domain.examine.*;
import cn.staitech.anno.domain.geojson.*;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.mapper.ProjectMapperV1;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.queue.DelayQueueExample;
import cn.staitech.anno.service.ExamineScoreService;
import cn.staitech.anno.service.FileService;
import cn.staitech.anno.utils.GeometryUtil;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.RandomUtils;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.RemoteLabelService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ibm.icu.text.SimpleDateFormat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static cn.staitech.anno.constant.CommonConstant.FILE_SUFFIX_JSON;
import static cn.staitech.anno.constant.CommonConstant.GLIDE_LINE;

/**
 * 服务实现类
 *
 * @author gjt
 * @since 2023-09-25
 */
@Service
public class ExamineScoreServiceImpl extends ServiceImpl<ExamineScoreMapper, ExamineScore> implements ExamineScoreService {
    @Resource
    private ExamineScoreMapper examineScoreMapper;

    @Resource
    private MarkingExamineMapper markingExamineMapper;

    @Resource
    private QuestionBankMapper questionBankMapper;

    @Resource
    private RemoteLabelService remoteLabelService;

    @Resource
    private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;

    @Resource
    private MarkingMapper markingMapper;

    @Resource
    private SlideMapperV1 slideMapperV1;

    @Resource
    private ProjectMarksRelMapper projectMarksRelMapper;

    @Resource
    private DelayQueueExample delayQueueExample;

    @Resource
    private ProjectMapperV1 projectMapperV1;

    @Resource
    private FileService fileService;

    @Resource
    private QuestionProjectRelMapper questionProjectRelMapper;

    @Override
    public PageResponse<ExamineScore> selectList(Integer pageSize, Integer pageNum, Long projectId, String nickName, Long examResults) {
        PageResponse<ExamineScore> resp = new PageResponse<>();
        // 查询考核评分表中信息
        Page<ExamineScore> page = PageHelper.startPage(pageNum, pageSize);
        ExamineSelectVo examineSelectVo = new ExamineSelectVo();
        examineSelectVo.setProjectId(projectId);
        examineSelectVo.setNickName(nickName);
        examineSelectVo.setExamResults(examResults);
        List<ExamineScore> examineScoreList = examineScoreMapper.selectExamineList(examineSelectVo);
        resp.setTotal(page.getTotal());
        resp.setList(examineScoreList);
        resp.setPages(page.getPages());
        resp.setPageNum(pageNum);
        resp.setPageSize(pageSize);
        return resp;
    }

    @Override
    public void refreshInterval(ExamineScoreExportInsertVo examineScoreExportInsertVo) {
        List<Long> questionProjectRelList = new ArrayList<>();
        for (Long examineId : examineScoreExportInsertVo.getExamineScoreIdList()) {
            ExamineScore examineScore = examineScoreMapper.selectById(examineId);
            QueryWrapper<QuestionProjectRel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("question_project_id", examineScore.getQuestionProjectId());
            QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectOne(queryWrapper);
            questionProjectRelList.add(questionProjectRel.getQuestionId());
        }
        List<Long> myList = questionProjectRelList.stream().distinct().collect(Collectors.toList());
        JSONObject markingJsonObject = new JSONObject();
        markingJsonObject.put("question_id", myList);
        remoteLabelService.Standard(markingJsonObject);
    }

    @Override
    public List<SelectExaminationListVO> selectExaminationList(Long projectId, String imageName) {
        QuestionBank questionBank = new QuestionBank();
        questionBank.setProjectId(projectId);
        questionBank.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
        questionBank.setImageName(imageName);
        // 根据项目查询
        QueryWrapper<QuestionProjectRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId).eq("del_flag", '0');
        QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectOne(queryWrapper);
        // 为空表示项目未添加切片
        if (questionProjectRel == null) {
            return new ArrayList<>();
        }
        QueryWrapper<ExamineScore> examineScoreQueryWrapper = new QueryWrapper<>();
        examineScoreQueryWrapper.eq("question_project_id", questionProjectRel.getQuestionProjectId()).eq("create_by", SecurityUtils.getLoginUser().getSysUser().getUserId());
        ExamineScore examineScoreBy = examineScoreMapper.selectOne(examineScoreQueryWrapper);
        List<SelectExaminationListVO> selectExaminationListVOS;
        if (examineScoreBy != null) {
            selectExaminationListVOS = examineScoreMapper.selectExaminationList(questionBank);
        } else {
            selectExaminationListVOS = examineScoreMapper.selectQuestionProjectList(questionProjectRel.getQuestionProjectId());
        }
        return selectExaminationListVOS;
    }

    @Override
    public SelectExaminationListVO selectExaminationBy(Long questionProjectId) {
        QueryWrapper<ExamineScore> examineScoreQueryWrapper = new QueryWrapper<>();
        examineScoreQueryWrapper.eq("question_project_id", questionProjectId).eq("create_by", SecurityUtils.getLoginUser().getSysUser().getUserId());
        ExamineScore examineScoreBy = examineScoreMapper.selectOne(examineScoreQueryWrapper);
        SelectExaminationListVO examinationListVO = new SelectExaminationListVO();
        if (examineScoreBy != null) {
            ExamineScore examineScore = new ExamineScore();
            examineScore.setQuestionProjectId(questionProjectId);
            examineScore.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
            examinationListVO = examineScoreMapper.selectExaminationBy(examineScore);
        } else {
            examinationListVO = examineScoreMapper.selectQuestionProject(questionProjectId);
        }
        return examinationListVO;
    }

    @Override
    public ExamineScoreBy selectByIds(Long examineScoreId) {
        return examineScoreMapper.selectByIds(examineScoreId);
    }

    @Override
    public int add(ExamineScoreAddVO examineScoreAddVO) throws Exception {
        // 根据题目项目id查询关系表中数据

        QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(examineScoreAddVO.getQuestionProjectId());
        if (questionProjectRel == null) {
            throw new Exception(MessageSource.M("DATA_EXCEPTION"));
        }
        // 校验当前项目是否暂停或者完成
        Project projectBy = projectMapperV1.selectById(questionProjectRel.getProjectId());
        if (projectBy != null) {
            if (projectBy.getStatus() == 3 || projectBy.getStatus() == 4) {
                throw new Exception(MessageSource.M("PROJECT_STOP_OR_OVER"));
            }
        }
        // 查询应标个数
        QueryWrapper<ProjectMarksRel> projectMarksRelQueryWrapper = new QueryWrapper<>();
        projectMarksRelQueryWrapper.eq("project_id", questionProjectRel.getProjectId());
        ProjectMarksRel projectMarksRelBy = projectMarksRelMapper.selectOne(projectMarksRelQueryWrapper);
        // 查询题库表中信息
        QuestionBank questionBank = questionBankMapper.selectById(questionProjectRel.getQuestionId());
        if (questionBank == null) {
            throw new Exception(MessageSource.M("DATA_EXCEPTION"));
        }
        ExamineScore examineScore = new ExamineScore();
        examineScore.setQuestionProjectId(questionProjectRel.getQuestionProjectId());
        examineScore.setImageName(questionBank.getImageName());
        examineScore.setProjectId(questionProjectRel.getProjectId());
        examineScore.setNickName(SecurityUtils.getLoginUser().getSysUser().getNickName());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        examineScore.setStartTime(sdf.format(date));
        examineScore.setEndTime(sdf.format(DateUtil.offsetMinute(date, 20)));
        if (projectMarksRelBy != null) {
            examineScore.setShouldNumber(projectMarksRelBy.getShouldMarks());
        }
        examineScore.setOperateStatus("1");
        examineScore.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
        examineScore.setCreateTime(sdf.format(date));
        examineScore.setSlideId(questionBank.getSlideId());
        examineScore.setGeojsonUrl(questionBank.getGeojsonUrl());
        int res = examineScoreMapper.insert(examineScore);
        delayQueueExample.addDelayQueueExample(examineScore.getExamineScoreId());
        return res;
    }

    @Override
    public int update(ExamineScoreAddVO req) throws Exception {
        // 根据题目项目id和当前用户查询出评分表中数据
        QueryWrapper<ExamineScore> examineScoreQueryWrapper = new QueryWrapper<>();
        examineScoreQueryWrapper
                .eq("question_project_id", req.getQuestionProjectId())
                .eq("create_by", SecurityUtils.getLoginUser().getSysUser().getUserId());
        ExamineScore examineScoreBy = examineScoreMapper.selectOne(examineScoreQueryWrapper);
        if (examineScoreBy == null) {
            throw new Exception(MessageSource.M("DATA_EXCEPTION"));
        }
        // 查询切片表中应标数量
        QueryWrapper<MarkingExamine> markingExamineQueryWrapper = new QueryWrapper<>();
        markingExamineQueryWrapper.eq("question_project_id", req.getQuestionProjectId()).eq("create_by", SecurityUtils.getLoginUser().getSysUser().getUserId());
        Integer markingCount = markingExamineMapper.selectCount(markingExamineQueryWrapper);
        ExamineScore examineScore = new ExamineScore();
        examineScore.setExamineScoreId(examineScoreBy.getExamineScoreId());
        examineScore.setOperateStatus("2");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        examineScore.setCompleteTime(sdf.format(date));
        examineScore.setUpdateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
        examineScore.setUpdateTime(sdf.format(date));
        String fileUrl;
        try {
            fileUrl = slideJsonExport(examineScoreBy);
        } catch (Exception exception) {
            log.error(exception.toString());
            throw new RuntimeException(MessageSource.M("ERROR_HAS_ALREADY"));
        }
        examineScore.setExaminationGeojsonUrl(fileUrl);
        examineScore.setRealityNumber(Long.valueOf(markingCount));
        int res = examineScoreMapper.updateById(examineScore);

        JSONObject markingJsonObject = new JSONObject();
        markingJsonObject.put("examine_score_id", examineScore.getExamineScoreId());
        markingJsonObject.put("user_id", examineScoreBy.getCreateBy());
        remoteLabelService.marking(markingJsonObject);
        // 更新当前评分记录
        return res;
    }

    public String slideJsonExport(ExamineScore examineScoreBy) {
        Long slideId = examineScoreBy.getSlideId();
        if (!Optional.ofNullable(slideId).isPresent()) {
            try {
                throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Slide slideBy = slideMapperV1.selectById(slideId);
        if (!Optional.ofNullable(slideBy).isPresent()) {
            try {
                throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // 标注数据
        MarkingExamine markingExamine = new MarkingExamine();
        markingExamine.setQuestionProjectId(examineScoreBy.getQuestionProjectId());
        markingExamine.setCreateBy(examineScoreBy.getCreateBy());
        List<Features> features = markingExamineMapper.selectListBy(markingExamine);
        features.forEach(i -> i.setGeometry(GeometryUtil.updateYAxle(i.getGeometry())));

        // 查询项目详情
        JsonExport jsonExport = null;
        Project projectBy = projectMapperV1.selectById(slideBy.getProjectId());
        if (Objects.equals(projectBy.getProjectType(), "2")) {
            jsonExport = markingMapper.jsonExportSelect(slideId);
        } else {
            jsonExport = markingMapper.reviewJsonExportSelect(slideId);
        }

        // 项目信息
        GeoProject project = new GeoProject();
        // 种属编码 + 结构编码 + 数据库项目id
        String projectId = jsonExport.getSpeciesId() + GLIDE_LINE + jsonExport.getOrganId() + GLIDE_LINE + jsonExport.getProjectId();
        project.setProject_id(projectId);
        project.setProject_name(jsonExport.getProjectName());

        // 图像信息
        GeoImage image = new GeoImage();
        image.setImage_shape(jsonExport.getImageShape());
        image.setImage_type(jsonExport.getFormat());
        image.setImage_name(jsonExport.getImageName());
        image.setCreate_time(jsonExport.getCreateTime());
        // 项目id + 十三位时间戳 + 两位随机数
        String imageId = jsonExport.getProjectId() + GLIDE_LINE + System.currentTimeMillis() + GLIDE_LINE + RandomUtils.RandomNumbers();
        image.setImage_id(imageId);
        image.setImage_url(jsonExport.getImageUrl());

        // 作者信息
        GeoAttribute attribute = new GeoAttribute();
        attribute.setAuthor(SecurityUtils.getLoginUser().getSysUser().getUserName());
        attribute.setDepartment(SecurityUtils.getLoginUser().getSysUser().getDept());

        // 标签信息
        QueryWrapper<MarkingExamine> markingQueryWrapper = new QueryWrapper<>();
        markingQueryWrapper
                .select("category_id")
                .eq("question_project_id", examineScoreBy.getQuestionProjectId())
                .eq("create_by", examineScoreBy.getCreateBy())
                .ne("category_id", 0)
                .groupBy("category_id");
        List<MarkingExamine> markingList = markingExamineMapper.selectList(markingQueryWrapper);
        List<GeoLabel> categoryList = new ArrayList<>();
        if (markingList.size() > 0) {
            for (MarkingExamine marking : markingList) {
                GeoLabel geoLabel = pathologicalIndicatorCategoryMapper.selectGeoLabel(marking.getCategoryId());
                categoryList.add(geoLabel);
            }
        }
        // 构建geoJson数据
        GeoJson geoJson = new GeoJson();
        geoJson.setFeatures(features);
        geoJson.setImage(image);
        geoJson.setProject(project);
        geoJson.setAttribute(attribute);
        geoJson.setLabel_info(categoryList);
        String jsonString = JSON.toJSONString(geoJson, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);

        // 写入文件
        String fileUrl = null;
        try {
            fileUrl = fileService.createExamineScoreFiles(slideId, FILE_SUFFIX_JSON, examineScoreBy.getQuestionProjectId(), examineScoreBy.getCreateBy());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        exportJson(fileUrl, jsonString);
        return fileUrl;
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
    public void updatePersonalFit(Long examineScoreId) {
        JSONObject markingJsonObject = new JSONObject();
        ExamineScore examineScore = examineScoreMapper.selectById(examineScoreId);
        markingJsonObject.put("examine_score_id", examineScoreId);
        markingJsonObject.put("user_id", examineScore.getCreateBy());
        remoteLabelService.marking(markingJsonObject);
    }


    @Override
    public List<ExamineScoreExportVO> selectLists(List<Long> examineScoreIdList) {
        return examineScoreMapper.selectLists(examineScoreIdList);
    }
}
