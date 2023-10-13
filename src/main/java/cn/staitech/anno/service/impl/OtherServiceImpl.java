package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.domain.MarkingExamine;
import cn.staitech.anno.mapper.ExamineScoreMapper;
import cn.staitech.anno.mapper.MarkingExamineMapper;
import cn.staitech.anno.service.OtherService;
import cn.staitech.system.api.RemoteLabelService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class OtherServiceImpl implements OtherService {

    @Resource
    private ExamineScoreMapper examineScoreMapper;

    @Resource
    private MarkingExamineMapper markingExamineMapper;

    @Resource
    private RemoteLabelService remoteLabelService;

    @Override
    public void updateExamStatus(Long examineScoreId) {
        // 查询详情信息
        ExamineScore examineScoreBy = examineScoreMapper.selectById(examineScoreId);
        if (examineScoreBy != null) {
            // 判断是否已经交卷
            if (Objects.equals(examineScoreBy.getOperateStatus(), "0")) {
                // 查询切片表中应标数量
                QueryWrapper<MarkingExamine> markingExamineQueryWrapper = new QueryWrapper<>();
                markingExamineQueryWrapper.eq("question_project_id", examineScoreBy.getQuestionProjectId()).eq("create_by", examineScoreBy.getCreateBy());
                Integer markingCount = markingExamineMapper.selectCount(markingExamineQueryWrapper);
                ExamineScore examineScore = new ExamineScore();
                examineScore.setExamineScoreId(examineScoreBy.getExamineScoreId());
                examineScore.setOperateStatus("2");
                examineScore.setRealityNumber(Long.valueOf(markingCount));
                // 更新当前评分记录
                examineScoreMapper.updateById(examineScore);
                JSONObject markingJsonObject = new JSONObject();
                markingJsonObject.put("examine_score_id",examineScoreId);
                remoteLabelService.marking(markingJsonObject);
            }
        }
    }

    @Override
    public void atRegularTimeUpdateExamStatus() {
        // 考试结束时间小于当前时间说明考试已经结束，查询七天之内考试结束且状态为未交卷的数据,进行更新
        QueryWrapper<ExamineScore> examineScoreQueryWrapper = new QueryWrapper<>();
        Date startTime = new Date();
        // 七天前
        Date endTime = DateUtil.offsetDay(startTime, -7);
        examineScoreQueryWrapper
                .eq("operate_status", "1")
                .ge("end_time", endTime)
                .lt("end_time", startTime);
        List<ExamineScore> examineScoreList = examineScoreMapper.selectList(examineScoreQueryWrapper);
        for (ExamineScore examineScore : examineScoreList) {
            updateExamStatus(examineScore.getExamineScoreId());
        }
    }


}
