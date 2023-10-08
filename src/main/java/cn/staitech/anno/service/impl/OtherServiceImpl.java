package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.domain.MarkingExamine;
import cn.staitech.anno.mapper.ExamineScoreMapper;
import cn.staitech.anno.mapper.MarkingExamineMapper;
import cn.staitech.anno.service.OrganService;
import cn.staitech.anno.service.OtherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class OtherServiceImpl implements OtherService {

    @Resource
    private ExamineScoreMapper examineScoreMapper;

    @Resource
    private MarkingExamineMapper markingExamineMapper;

    @Override
    public void updateExamStatus(Long examineScoreId) {
        // 查询详情信息
        ExamineScore examineScoreBy = examineScoreMapper.selectById(examineScoreId);
        if(examineScoreBy != null){
            // 判断是否已经交卷
            if(Objects.equals(examineScoreBy.getExamStatus(), "0")){
                // 查询切片表中应标数量
                QueryWrapper<MarkingExamine> markingExamineQueryWrapper = new QueryWrapper<>();
                markingExamineQueryWrapper.eq("question_project_id", examineScoreBy.getQuestionProjectId()).eq("create_by", examineScoreBy.getCreateBy());
                Integer markingCount = markingExamineMapper.selectCount(markingExamineQueryWrapper);
                ExamineScore examineScore = new ExamineScore();
                examineScore.setExamineScoreId(examineScoreBy.getExamineScoreId());
                examineScore.setExamStatus("1");
                examineScore.setOperateStatus("1");
                examineScore.setRealityNumber(Long.valueOf(markingCount));
                // 更新当前评分记录
                examineScoreMapper.updateById(examineScore);
            }
        }
    }

}
