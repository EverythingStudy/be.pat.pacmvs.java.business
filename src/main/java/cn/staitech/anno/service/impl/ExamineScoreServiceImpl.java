package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.domain.*;
import cn.staitech.anno.domain.examineScore.ExamineScoreAddVO;
import cn.staitech.anno.domain.examineScore.ExamineScoreExportVO;
import cn.staitech.anno.domain.examineScore.SelectExaminationListVO;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.queue.DelayQueueExample;
import cn.staitech.anno.service.ExamineScoreService;
import cn.staitech.anno.utils.ExcludeEmptyQueryWrapper;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
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
    private ProjectMarksRelMapper projectMarksRelMapper;

    @Resource
    private DelayQueueExample delayQueueExample;

    @Resource
    private QuestionProjectRelMapper questionProjectRelMapper;

    @Override
    public PageResponse<ExamineScore> selectList(Integer pageSize, Integer pageNum, Long projectId, String nickName, Long examResults) {
        PageResponse<ExamineScore> resp = new PageResponse<>();
        // 查询考核评分表中信息
        ExcludeEmptyQueryWrapper<ExamineScore> examineScoreQueryWrapper = new ExcludeEmptyQueryWrapper<>();
        examineScoreQueryWrapper
                .eq("project_id", projectId)
                .like("nick_name", nickName)
                .eq("exam_results", examResults)
                .orderByDesc("create_time");
        Page<ExamineScore> page = PageHelper.startPage(pageNum, pageSize);
        List<ExamineScore> examineScoreList = examineScoreMapper.selectList(examineScoreQueryWrapper);
        resp.setTotal(page.getTotal());
        resp.setList(examineScoreList);
        resp.setPages(page.getPages());
        resp.setPageNum(pageNum);
        resp.setPageSize(pageSize);
        return resp;
    }

    @Override
    public List<SelectExaminationListVO> selectExaminationList(Long projectId, String imageName) {
        QuestionBank questionBank = new QuestionBank();
        questionBank.setProjectId(projectId);
        questionBank.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
        questionBank.setImageName(imageName);
        return examineScoreMapper.selectExaminationList(questionBank);
    }

    @Override
    public SelectExaminationListVO selectExaminationBy(Long questionProjectId){
        QueryWrapper<ExamineScore> examineScoreQueryWrapper = new QueryWrapper<>();
        examineScoreQueryWrapper.eq("question_project_id",questionProjectId).eq("create_by", SecurityUtils.getLoginUser().getSysUser().getUserId());
        ExamineScore examineScoreBy = examineScoreMapper.selectOne(examineScoreQueryWrapper);
        SelectExaminationListVO examinationListVO = new SelectExaminationListVO();
        if(examineScoreBy != null){
            ExamineScore examineScore = new ExamineScore();
            examineScore.setQuestionProjectId(questionProjectId);
            examineScore.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
            examinationListVO = examineScoreMapper.selectExaminationBy(examineScore);
        }
        else{
            examinationListVO = examineScoreMapper.selectQuestionProject(questionProjectId);
            if(examinationListVO != null){
                examinationListVO.setOperateStatus(0L);
            }
        }
        return examinationListVO;
    }

    @Override
    public int add(ExamineScoreAddVO examineScoreAddVO) throws Exception {
        // 根据题目项目id查询关系表中数据
        QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(examineScoreAddVO.getQuestionProjectId());
        if (questionProjectRel == null) {
            throw new Exception("数据异常");
        }
        // 查询应标个数
        QueryWrapper<ProjectMarksRel> projectMarksRelQueryWrapper = new QueryWrapper<>();
        projectMarksRelQueryWrapper.eq("project_id", questionProjectRel.getProjectId());
        ProjectMarksRel projectMarksRelBy = projectMarksRelMapper.selectOne(projectMarksRelQueryWrapper);
        // 查询题库表中信息
        QuestionBank questionBank = questionBankMapper.selectById(questionProjectRel.getQuestionId());
        if (questionBank == null) {
            throw new Exception("数据异常");
        }
        ExamineScore examineScore = new ExamineScore();
        examineScore.setQuestionProjectId(questionProjectRel.getQuestionProjectId());
        examineScore.setImageName(questionBank.getImageName());
        examineScore.setProjectId(questionProjectRel.getProjectId());
        examineScore.setNickName(SecurityUtils.getLoginUser().getSysUser().getNickName());
        Date date = new Date();
        examineScore.setStartTime(date);
        examineScore.setEndTime(DateUtil.offsetMinute(date, 20));
        examineScore.setShouldNumber(projectMarksRelBy.getShouldMarks());
        examineScore.setOperateStatus("1");
        examineScore.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
        examineScore.setCreateTime(date);
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
            throw new Exception("数据异常");
        }
        // 查询切片表中应标数量
        QueryWrapper<MarkingExamine> markingExamineQueryWrapper = new QueryWrapper<>();
        markingExamineQueryWrapper.eq("question_project_id", req.getQuestionProjectId()).eq("create_by", examineScoreBy.getCreateBy());
        Integer markingCount = markingExamineMapper.selectCount(markingExamineQueryWrapper);
        ExamineScore examineScore = new ExamineScore();
        examineScore.setExamineScoreId(examineScoreBy.getExamineScoreId());
        examineScore.setOperateStatus("2");
        examineScore.setRealityNumber(Long.valueOf(markingCount));
        // 更新当前评分记录
        return examineScoreMapper.updateById(examineScore);
    }



    @Override
    public List<ExamineScoreExportVO> selectLists(List<Long> examineScoreIdList) {
        return examineScoreMapper.selectLists(examineScoreIdList);
    }


}
