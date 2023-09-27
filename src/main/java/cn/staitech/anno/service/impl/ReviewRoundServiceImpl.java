package cn.staitech.anno.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.domain.ReviewRound;
import cn.staitech.anno.domain.reviewround.ReviewRoundBatchInVO;
import cn.staitech.anno.domain.reviewround.ReviewRoundInsertInVO;
import cn.staitech.anno.domain.reviewround.ReviewRoundOutVO;
import cn.staitech.anno.mapper.ReviewRoundMapper;
import cn.staitech.anno.service.ReviewRoundService;
import cn.staitech.anno.service.SysUserService;
import cn.staitech.anno.service.TopicService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangf
 * @description 针对表【tb_review_round】的数据库操作Service实现
 * @createDate 2023-09-18 16:15:33
 */
@Service
public class ReviewRoundServiceImpl extends ServiceImpl<ReviewRoundMapper, ReviewRound>
        implements ReviewRoundService {

    @Resource
    private ReviewRoundMapper reviewRoundMapper;

    @Resource
    private TopicService topicService;

    @Resource
    private SysUserService sysUserService;

    /**
     * 批量添评审轮次
     *
     * @param reviewRoundBatchInVO
     * @return
     */
    public boolean saveBatchByList(ReviewRoundBatchInVO reviewRoundBatchInVO) {

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        List<ReviewRoundInsertInVO> insertVoList = reviewRoundBatchInVO.getInsertList();
        List<ReviewRound> list = new ArrayList<>(insertVoList.size());
        Snowflake snowflake = new Snowflake();
        String str = snowflake.nextIdStr();
        for (ReviewRoundInsertInVO vo : insertVoList) {
            ReviewRound reviewRound = new ReviewRound();

            reviewRound.setProjectId(reviewRoundBatchInVO.getProjectId());
            reviewRound.setReviewContent(reviewRoundBatchInVO.getReviewContent());

            reviewRound.setRoundId(vo.getRoundId());
            reviewRound.setTopicId(vo.getTopicId());
            reviewRound.setGroupId(vo.getGroupId());

            reviewRound.setCreateBy(sysUser.getUserId());
            reviewRound.setOrganizationId(sysUser.getOrganizationId());

            reviewRound.setCreateTime(new Date());
            if (reviewRoundBatchInVO.getContentId()!=null&&!"".equals(reviewRoundBatchInVO.getContentId())){
                reviewRound.setContentId(reviewRoundBatchInVO.getContentId());
            }else{
                reviewRound.setContentId(str);
            }
            list.add(reviewRound);
        }
        return (this.saveBatch(list));
    }


    /**
     * 评审轮次列表
     *
     * @param pageNum
     * @param pageSize
     * @param projectId
     * @return
     */
    public PageMaster<ReviewRoundOutVO> pageReviewRound(int pageNum, int pageSize, Long projectId) {

        PageHelper.startPage(pageNum, pageSize).setReasonable(true);
        ReviewRound reviewRound = new ReviewRound();
        reviewRound.setProjectId(projectId);
        QueryWrapper queryWrapper = new QueryWrapper<>(reviewRound);
        // 【评审设置】评审轮次列表排序方式为创建时间倒序，现在是正序 http://jira.shengtong.com/browse/SAAS-87
        queryWrapper.orderByDesc("create_time");

        List<ReviewRound> list = this.list(queryWrapper);
        PageMaster pageMaster = new PageMaster<>(list);

        List<ReviewRoundOutVO> respList = new ArrayList<>(list.size());

        Map<Long, String> topicMap = topicService.selectMap(2);

        for (ReviewRound round : list) {
            ReviewRoundOutVO reviewRoundOutVO = new ReviewRoundOutVO();
            BeanUtils.copyProperties(round, reviewRoundOutVO);

            // 评审轮次
            reviewRoundOutVO.setRoundName(MapConstant.getRoundName(round.getRoundId()));
            // 组别
            reviewRoundOutVO.setGroupName(MapConstant.getGroupName(round.getGroupId()));
            //专题编号
            if (topicMap.containsKey(round.getTopicId())) {
                reviewRoundOutVO.setTopicName(topicMap.get(round.getTopicId()));
            }
            // 创建者
            reviewRoundOutVO.setCreateByName(sysUserService.selectUserById(round.getCreateBy()).getUserName());
            respList.add(reviewRoundOutVO);
        }

        pageMaster.setList(respList);
        PageHelper.clearPage();
        return pageMaster;
    }


    /**
     * 查询单个评审轮次
     *
     * @param reviewRoundId
     * @return
     */
    public ReviewRoundOutVO getOneById(Long reviewRoundId) {
        ReviewRound reviewRound = new ReviewRound();
        reviewRound.setReviewRoundId(reviewRoundId);
        ReviewRound round = this.getById(reviewRound);

        ReviewRoundOutVO reviewRoundOutVO = new ReviewRoundOutVO();
        BeanUtils.copyProperties(round, reviewRoundOutVO);

        // 评审轮次
        reviewRoundOutVO.setRoundName(MapConstant.getRoundName(round.getRoundId()));
        // 组别
        reviewRoundOutVO.setGroupName(MapConstant.getGroupName(round.getGroupId()));

        //专题编号
        Map<Long, String> topicMap = topicService.selectMap(2);
        if (topicMap.containsKey(round.getTopicId())) {
            reviewRoundOutVO.setTopicName(topicMap.get(round.getTopicId()));
        }
        // 创建者
        reviewRoundOutVO.setCreateByName(sysUserService.selectUserById(round.getCreateBy()).getUserName());
        return reviewRoundOutVO;
    }
}




