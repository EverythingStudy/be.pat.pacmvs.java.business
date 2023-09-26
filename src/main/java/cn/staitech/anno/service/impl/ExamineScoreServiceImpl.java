package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.mapper.ExamineScoreMapper;
import cn.staitech.anno.service.ExamineScoreService;
import cn.staitech.common.core.domain.PageResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public PageResponse<ExamineScore> selectList(Integer pageSize, Integer pageNum, Long projectId, String nickName, Long examResults) {

        PageResponse<ExamineScore> resp = new PageResponse<>();
        // 查询考核评分表中信息
        QueryWrapper<ExamineScore> examineScoreQueryWrapper = new QueryWrapper<>();
        examineScoreQueryWrapper
                .eq("project_id", projectId)
                .like("nick_name", nickName)
                .eq("del_flag", "0")
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
    public List<ExamineScore> selectLists(Long projectId,List<Long> examineScoreIdList){
        QueryWrapper<ExamineScore> examineScoreQueryWrapper = new QueryWrapper<>();
        examineScoreQueryWrapper
                .eq("project_id","project_id")
                .in("examine_score_id",examineScoreIdList)
                .eq("del_flag","0")
        ;
        return examineScoreMapper.selectList(examineScoreQueryWrapper);
    }


}
