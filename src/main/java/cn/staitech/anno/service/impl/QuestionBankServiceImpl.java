package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.QuestionBankConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.QuestionBank;
import cn.staitech.anno.domain.QuestionProjectRel;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.question.in.ConfirmSelectionIn;
import cn.staitech.anno.domain.question.in.CreateBySlideIn;
import cn.staitech.anno.domain.question.in.CreateQuestionIn;
import cn.staitech.anno.domain.question.in.GetQuestionListIn;
import cn.staitech.anno.domain.question.in.GetQuestionsIn;
import cn.staitech.anno.domain.question.in.SettingCompletedIn;
import cn.staitech.anno.domain.question.out.GetProjectBoxOut;
import cn.staitech.anno.domain.question.out.GetQuestionListOut;
import cn.staitech.anno.domain.question.out.GetQuestionsOut;
import cn.staitech.anno.mapper.ImageMapper;
import cn.staitech.anno.mapper.QuestionBankMapper;
import cn.staitech.anno.mapper.QuestionProjectRelMapper;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.service.IQuestionBankService;
import cn.staitech.anno.service.IQuestionProjectRelService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.SpringUtils;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.staitech.anno.constant.QuestionBankConstant.NUMBER_0;
import static cn.staitech.anno.constant.QuestionBankConstant.PROHIBIT_REPETITION;
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

    @Autowired
    private MarkingService markingService;

    /**
     * 生成考题
     *
     * @param req
     * @return
     */
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

        List<QuestionBank> resp = slides.stream().map(e -> {
            Image image = imageMapper.selectById(e.getImageId());
            QuestionBank ret = new QuestionBank();
            BeanUtils.copyProperties(e, ret);
            ret.setCreateBy(SecurityUtils.getUserId());
            ret.setCreateTime(new Date());
            ret.setImageCode(image.getImageCode());
            ret.setSize(image.getSize());

            // 插入json文件返回数据
            String urlPath;
            try {
                urlPath = markingService.slideJsonExport(e.getSlideId());
            } catch (Exception exception) {
                log.error(exception.toString());
                 throw new RuntimeException(QuestionBankConstant.ERROR_GENERATE_JSON);
            }
            String s = StringUtils.substringAfterLast(urlPath, "/");
            ret.setJsonName(s);
            return ret;
        }).collect(Collectors.toList());
        //插入题库表
        QuestionBankServiceImpl bean = SpringUtils.getBean(QuestionBankServiceImpl.class);
        bean.saveBatch(resp);
        return R.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R createBySlide(CreateBySlideIn req) {
        log.info("根据切片生成考题接口开始：");
        //
        List<QuestionBank> questionBanks = req.getSlideDataList().stream().map(e -> {
            Image image = imageMapper.selectById(e.getImageId());
            QuestionBank ret = new QuestionBank();
            BeanUtils.copyProperties(e, ret);
            ret.setCreateBy(SecurityUtils.getUserId());
            ret.setCreateTime(new Date());
            ret.setImageCode(image.getImageCode());
            ret.setSize(image.getSize());
            // 插入json文件返回数据
            String urlPath;
            try {
                urlPath = markingService.slideJsonExport(e.getSlideId());
            } catch (Exception exception) {
                log.error(exception.toString());
                throw new RuntimeException(QuestionBankConstant.ERROR_GENERATE_JSON);
            }
            String s = StringUtils.substringAfterLast(urlPath, "/");
            ret.setJsonName(s);
            return ret;
        }).collect(Collectors.toList());
        QuestionBankServiceImpl bean = SpringUtils.getBean(QuestionBankServiceImpl.class);
        bean.saveBatch(questionBanks);
        return R.ok();
    }

    @Override
    public PageResponse<GetQuestionListOut> getQuestionList(GetQuestionListIn req) {
        log.info("考题列表分页查询接口开始：");
        PageResponse resp = new PageResponse<>();
        if (!isAdmin(SecurityUtils.getUserId())) {
            req.setOrganizationId(SecurityUtils.getUserId());
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
            List<GetProjectBoxOut> resp = this.baseMapper.selectProjectList(SecurityUtils.getUserId());
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
        List<GetQuestionListOut> respData = new ArrayList<>();
        LambdaQueryWrapper<QuestionProjectRel> qw = new LambdaQueryWrapper<>();
        qw.eq(QuestionProjectRel::getProjectId, projectId);
        qw.eq(QuestionProjectRel::getDelFlag, NUMBER_0);

        List<QuestionProjectRel> questionProjectRels = questionProjectRelMapper.selectList(qw);
        if (!CollectionUtils.isEmpty(questionProjectRels)) {
            respData = questionProjectRels.stream().map(e -> {
                GetQuestionListOut ret = new GetQuestionListOut();
                BeanUtils.copyProperties(e, ret);
                return ret;
            }).collect(Collectors.toList());
            resp.setShouldMarks(questionProjectRels.get(0).getShouldMarks());
        }
        resp.setReqList(respData);

        return resp;
    }

    @Override
    public R confirmSelection(ConfirmSelectionIn req) {
        log.info("考核选片-确认选择接口开始：");
        LambdaQueryWrapper<QuestionProjectRel> qw = new LambdaQueryWrapper<>();
        qw.eq(QuestionProjectRel::getProjectId,req.getProjectId());
        qw.eq(QuestionProjectRel::getQuestionId,req.getQuestionId());
        List<QuestionProjectRel> questionProjectRels = questionProjectRelMapper.selectList(qw);

        if(!CollectionUtils.isEmpty(questionProjectRels)){
            return R.fail(PROHIBIT_REPETITION);
        }

        QuestionProjectRel entity = new QuestionProjectRel();
        BeanUtils.copyProperties(req, entity);
        entity.setCreateBy(SecurityUtils.getUserId());
        entity.setCreateName(SecurityUtils.getUsername());
        entity.setCreateTime(new Date());
        questionProjectRelMapper.insert(entity);

        return R.ok();

    }

    @Override
    public R settingCompleted(SettingCompletedIn req) {
        log.info("考核选片-设置完成接口开始：");
        List<Long> dataList = req.getDataList();
        List<QuestionProjectRel> param = dataList.stream().map(e -> {
            QuestionProjectRel questionProjectRel = new QuestionProjectRel();
            questionProjectRel.setShouldMarks(req.getShouldMarks());
            questionProjectRel.setQuestionProjectId(e);
            return questionProjectRel;
        }).collect(Collectors.toList());
        iQuestionProjectRelService.updateBatchById(param);

        return R.ok();
    }

}
