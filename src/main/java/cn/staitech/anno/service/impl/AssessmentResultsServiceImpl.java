package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.domain.AssessmentResults;
import cn.staitech.anno.domain.SysOrganization;
import cn.staitech.anno.domain.assessmentResults.AssessmentResultsQueryIn;
import cn.staitech.anno.mapper.AssessmentResultsMapper;
import cn.staitech.anno.service.AssessmentResultsService;
import cn.staitech.anno.utils.ExcludeEmptyQueryWrapper;
import cn.staitech.common.core.domain.PageResponse;
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
 * @since 2023-10-17
 */
@Service
public class AssessmentResultsServiceImpl extends ServiceImpl<AssessmentResultsMapper, AssessmentResults> implements AssessmentResultsService {

    @Resource
    private AssessmentResultsMapper assessmentResultsMapper;

    @Override
    public PageResponse<AssessmentResults> selectPageList(AssessmentResultsQueryIn req) {
        PageResponse<AssessmentResults> resp = new PageResponse<>();
        ExcludeEmptyQueryWrapper<AssessmentResults> queryWrapper = new ExcludeEmptyQueryWrapper<>();
        queryWrapper
                .like("json_name", req.getJsonName())
                .like("examine_people", req.getExaminePeople())
                .eq("algorithm_assessment_id", req.getAlgorithmAssessmentId())
                .like("examine_category_name", req.getExamineCategoryName())
                .orderByDesc("create_time");
        if (req.getCreateTimeParams() != null) {
            if (req.getCreateTimeParams().getBeginTime() != null) {
                queryWrapper.ge("create_time", req.getCreateTimeParams().getBeginTime());
            }
            if (req.getCreateTimeParams().getEndTime() != null) {
                Date endTime = DateUtil.offsetDay(req.getCreateTimeParams().getEndTime(), 1);
                queryWrapper.lt("create_time", endTime);
            }
        }
        Page<SysOrganization> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<AssessmentResults> assessmentResults = assessmentResultsMapper.selectList(queryWrapper);
        resp.setTotal(page.getTotal());
        resp.setList(assessmentResults);
        resp.setPages(page.getPages());
        resp.setPageNum(req.getPageNum());
        resp.setPageSize(req.getPageSize());
        return resp;
    }


}
