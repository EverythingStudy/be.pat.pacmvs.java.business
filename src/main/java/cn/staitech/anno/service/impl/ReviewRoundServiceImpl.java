package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ReviewRound;
import cn.staitech.anno.domain.reviewround.ReviewRoundBatchInVO;
import cn.staitech.anno.domain.reviewround.ReviewRoundInsertInVO;
import cn.staitech.anno.mapper.ReviewRoundMapper;
import cn.staitech.anno.service.ReviewRoundService;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

            list.add(reviewRound);
        }
        return (this.saveBatch(list));
    }
}




