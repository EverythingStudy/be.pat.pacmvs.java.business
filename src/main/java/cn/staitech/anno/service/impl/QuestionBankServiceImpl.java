package cn.staitech.anno.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.*;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.service.IQuestionBankService;
import cn.staitech.anno.service.IQuestionProjectRelService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.question.in.*;
import cn.staitech.anno.vo.question.out.GetProjectBoxOut;
import cn.staitech.anno.vo.question.out.GetQuestionListOut;
import cn.staitech.anno.vo.question.out.GetQuestionsOut;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.SpringUtils;
import cn.staitech.common.core.utils.StringUtils;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.RemoteLabelService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static cn.staitech.common.security.utils.SecurityUtils.isAdmin;

/**
 * <p>
 * 题库表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-09-26
 */
@Slf4j
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank> implements IQuestionBankService {

    @Resource
    private SlideMapper slideMapper;

    @Resource
    private QuestionProjectRelMapper questionProjectRelMapper;

    @Autowired
    private IQuestionProjectRelService iQuestionProjectRelService;

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private RemoteLabelService remoteLabelService;

    @Autowired
    private MarkingService markingService;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ExamineScoreMapper examineScoreMapper;

    @Resource
    private QuestionBankMapper questionBankMapper;

    /**
     * 生成考题
     *
     * @param req
     * @return
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R createQuestion(CreateQuestionIn req) {
        log.info("生成考题接口开始：");
        //通过项目id查询切片
        LambdaQueryWrapper<Slide> qw = new LambdaQueryWrapper<>();
        qw.eq(Slide::getProjectId, req.getProjectId());
        qw.eq(Slide::getIsDelete, 0);
        List<Slide> slides = slideMapper.selectList(qw);
        //生成json文件
        if (CollectionUtils.isEmpty(slides)) {
            return R.ok();
        }
        List<QuestionBank> resp = new ArrayList<>();

        for (Slide s : slides) {
            Slide slide = slideMapper.selectById(s.getSlideId());
            if (Objects.equals(slide.getStatus(), "3")) {
                Image image = imageMapper.selectById(slide.getImageId());
                // 插入json文件返回数据
                String urlPath;
                try {
                    urlPath = markingService.slideLabelJsonExport(s.getSlideId(), SecurityUtils.getLoginUser().getSysUser());
                } catch (Exception exception) {
                    log.error(exception.toString());
                    throw new RuntimeException(MessageSource.M("ERROR_GENERATE_JSON"));
                }
                if(urlPath != null && !"".equals(urlPath)){
                    String[] pathList = urlPath.split(",");
                    for (String path : pathList) {
                        String jsonName = StringUtils.substringAfterLast(path, File.separator);
                        QuestionBank ret = new QuestionBank();
                        BeanUtils.copyProperties(s, ret);
                        ret.setCreateBy(SecurityUtils.getUserId());
                        ret.setCreateTime(new Date());
                        ret.setImageCode(image.getImageCode());
                        ret.setImageName(image.getImageName());
                        ret.setSize(image.getSize());
                        ret.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
                        ret.setJsonName(jsonName);
                        ret.setGeojsonUrl(path);
                        resp.add(ret);
                    }
                }
            }
        }
        //插入题库表
        if(resp.size() > 0){
            QuestionBankServiceImpl bean = SpringUtils.getBean(QuestionBankServiceImpl.class);
            List<Long> questionBankList = new ArrayList<>();
            for (QuestionBank questionBank : resp) {
                baseMapper.insert(questionBank);
                questionBankList.add(questionBank.getQuestionId());
            }

            JSONObject markingJsonObject = new JSONObject();
            markingJsonObject.put("question_id", questionBankList);
            remoteLabelService.Standard(markingJsonObject);

        }
        cn.staitech.anno.domain.Project project = new cn.staitech.anno.domain.Project();
        project.setProjectId(req.getProjectId());
        project.setIfCreateQuestions("1");
        projectMapper.updateById(project);
        return R.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R createBySlide(CreateBySlideIn req) throws InterruptedException {
        log.info("根据切片生成考题接口开始：");

        List<CreateBySlideData> slideDataList = req.getSlideDataList();
        if (CollectionUtils.isEmpty(slideDataList)) {
            R.ok();
        }
        List<QuestionBank> questionBanks = new ArrayList<>();
        for (CreateBySlideData reqBy : req.getSlideDataList()) {
            Slide slide = slideMapper.selectById(reqBy.getSlideId());
            if (Objects.equals(slide.getStatus(), "3")) {
                Image image = imageMapper.selectById(slide.getImageId());
                String urlPath;
                try {
                    urlPath = markingService.slideLabelJsonExport(reqBy.getSlideId(), SecurityUtils.getLoginUser().getSysUser());
                } catch (Exception ex) {
                    throw new RuntimeException(MessageSource.M("ERROR_GENERATE_JSON"));
                }
                if(urlPath != null && !"".equals(urlPath)){
                    String[] pathList = urlPath.split(",");
                    for (String path : pathList) {
                        String jsoName = StringUtils.substringAfterLast(path, File.separator);
                        QuestionBank ret = new QuestionBank();
                        BeanUtils.copyProperties(reqBy, ret);
                        BeanUtils.copyProperties(image, ret);
                        ret.setCreateBy(SecurityUtils.getUserId());
                        ret.setCreateTime(new Date());
                        ret.setUpdateBy(null);
                        ret.setUpdateTime(null);
                        ret.setJsonName(jsoName);
                        ret.setGeojsonUrl(path);
                        ret.setProjectId(reqBy.getProjectId());
                        ret.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
                        questionBanks.add(ret);
                    }
                }
            }
        }
        if(questionBanks.size() > 0){
            List<Long> questionBankList = new ArrayList<>();
            for (QuestionBank questionBank : questionBanks) {
                baseMapper.insert(questionBank);
                questionBankList.add(questionBank.getQuestionId());
            }
            JSONObject markingJsonObject = new JSONObject();
            markingJsonObject.put("question_id", questionBankList);
            remoteLabelService.Standard(markingJsonObject);
        }
        return R.ok();
    }

    @Override
    public PageResponse<GetQuestionListOut> getQuestionList(GetQuestionListIn req) {
        log.info("考题列表分页查询接口开始：");
        PageResponse resp = new PageResponse<>();
        if (!isAdmin(SecurityUtils.getUserId())) {
            req.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        }
        Page<GetQuestionListOut> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<GetQuestionListOut> getQuestionListOuts = this.baseMapper.selectQuestionList(req);

        resp.setTotal(page.getTotal());
        resp.setPages(page.getPages());
        resp.setList(getQuestionListOuts);

        return resp;
    }

    @Override
    public R<List<GetProjectBoxOut>> getProjectBox() {
        log.info("考核选片项目下拉框查询接口开始：");
        if (!isAdmin(SecurityUtils.getUserId())) {
            List<GetProjectBoxOut> resp = this.baseMapper.selectProjectList(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
            return R.ok(resp);
        }
        List<GetProjectBoxOut> resp = this.baseMapper.selectProjectList(null);
        return R.ok(resp);
    }

    @Override
    public List<GetQuestionListOut> getQuestionListExt(GetQuestionsIn req) {
        log.info("考题列表不分页查询接口开始：");
        List<GetQuestionListOut> resp = this.baseMapper.selectQuestionListExt(req);

        return resp;
    }

    @Override
    public GetQuestionsOut getQuestionByProject(Long projectId) {

        log.info("考核选片-选片列表查询：");
        GetQuestionsOut resp = new GetQuestionsOut();
        List<GetQuestionListOut> respData = questionProjectRelMapper.selectListByProject(projectId);
        resp.setReqList(respData);
        resp.setShouldMarks(questionProjectRelMapper.selectShouldMarks(projectId));
        return resp;
    }

    @Override
    public R confirmSelection(ConfirmSelectionIn req) {
        log.info("考核选片-确认选择接口开始：");

        LambdaQueryWrapper<QuestionProjectRel> qw = new LambdaQueryWrapper<>();
        qw.eq(QuestionProjectRel::getProjectId, req.getProjectId());
//        qw.eq(QuestionProjectRel::getQuestionId, req.getQuestionId());
        qw.eq(QuestionProjectRel::getDelFlag, CommonConstant.NUMBER_0);
        List<QuestionProjectRel> questionProjectRels = questionProjectRelMapper.selectList(qw);
        List<Long> questionIdList = questionProjectRels.stream().map(QuestionProjectRel::getQuestionId).collect(Collectors.toList());
        List<QuestionProjectRel> questionProjectRelList = new ArrayList<>();
        for (Long question : req.getQuestionId()) {
            //筛选出没有添加过的questionId
            if (!questionIdList.contains(question)) {
                QuestionProjectRel entity = new QuestionProjectRel();
                entity.setQuestionId(question);
                QuestionBank questionBank = questionBankMapper.selectById(question);
                entity.setProjectId(req.getProjectId());
                entity.setImageCode(questionBank.getImageCode());
                entity.setJsonName(questionBank.getJsonName());
                entity.setImageName(questionBank.getImageName());
//                entity.setCreateBy(27L);
                entity.setCreateBy(SecurityUtils.getUserId());
                entity.setCreateName(SecurityUtils.getUsername());
//                entity.setCreateName("zmj");
//                entity.setCreateTime(new Date());
                questionProjectRelList.add(entity);
//                questionProjectRelMapper.insert(entity);
            }
        }
        if (CollectionUtil.isNotEmpty(questionProjectRelList)) {
            questionProjectRelMapper.examineInsert(questionProjectRelList);
        }

//        if (!CollectionUtils.isEmpty(questionProjectRels)) {
//            return R.fail(MessageSource.M("PROHIBIT_REPETITION"));
//        }


//        BeanUtils.copyProperties(req, entity);
//        entity.setCreateBy(SecurityUtils.getUserId());
//        entity.setCreateName(SecurityUtils.getUsername());
//        entity.setCreateTime(new Date());
//        questionProjectRelMapper.insert(entity);

        return R.ok();

    }

    @Override
    public R settingCompleted(SettingCompletedIn req) {
        log.info("考核选片-设置完成接口开始：");
        /*List<Long> dataList = req.getDataList();
        List<QuestionProjectRel> param = dataList.stream().map(e -> {
            QuestionProjectRel questionProjectRel = new QuestionProjectRel();
            questionProjectRel.setShouldMarks(req.getShouldMarks());
            questionProjectRel.setQuestionProjectId(e);
            return questionProjectRel;
        }).collect(Collectors.toList());
        iQuestionProjectRelService.updateBatchById(param);*/

        if (questionProjectRelMapper.countShouldMarks(req.getProjectId()) > 0) {
            questionProjectRelMapper.updateShouldMarks(req.getProjectId(), req.getShouldMarks());
        } else {
            questionProjectRelMapper.insertShouldMarks(req.getProjectId(), req.getShouldMarks());
        }

        return R.ok();
    }

    @Override
    public R removeQuestion(SettingCompletedIn req) {
        log.info("考核选片-删除接口开始：");
        List<Long> dataList = req.getDataList();

        for (Long aLong : dataList) {
            LambdaQueryWrapper<ExamineScore> qw = new LambdaQueryWrapper<>();
            qw.eq(ExamineScore::getQuestionProjectId, aLong);
            List<ExamineScore> examineScores = examineScoreMapper.selectList(qw);
            if (!CollectionUtils.isEmpty(examineScores)) {
                return R.fail(MessageSource.M("ERROR_HAS_ALREADY"));
            }
        }
        List<QuestionProjectRel> param = dataList.stream().map(e -> {
            QuestionProjectRel questionProjectRel = new QuestionProjectRel();
            questionProjectRel.setDelFlag(CommonConstant.NUMBER_1);
            questionProjectRel.setQuestionProjectId(e);
            return questionProjectRel;
        }).collect(Collectors.toList());
        iQuestionProjectRelService.updateBatchById(param);

        return R.ok();
    }

}
