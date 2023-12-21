package cn.staitech.anno.project.service;

import cn.staitech.anno.project.domain.DownTask;
import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.vo.ReviewIn;
import cn.staitech.anno.project.vo.ReviewRoundIn;
import cn.staitech.anno.project.vo.ReviewUp;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.reviewround.ReviewRoundOutVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 86186
 * @description 针对表【tb_review】的数据库操作Service
 * @createDate 2023-09-15 13:05:15
 */
public interface ReviewService extends IService<Review> {

    void exportReview(Long projectId, Long slideId) throws Exception;

    void csvExportReviewCurrent(Long projectId, Long slideId) throws Exception;

    DownTask csvExportReview(Long projectId, List<Long> slideIds) throws Exception;

    int insert(ReviewIn req) throws Exception;

    int update(ReviewUp req) throws Exception;

    PageMaster<ReviewRoundOutVO> pageReviewRound(Page page, ReviewRoundIn params);


}
