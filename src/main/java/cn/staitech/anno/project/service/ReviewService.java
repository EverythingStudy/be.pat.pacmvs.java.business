package cn.staitech.anno.project.service;

import cn.staitech.anno.domain.reviewround.ReviewRoundOutVO;
import cn.staitech.anno.project.domain.DownTask;
import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.vo.ReviewIN;
import cn.staitech.anno.project.vo.ReviewRoundIN;
import cn.staitech.anno.project.vo.ReviewUP;
import cn.staitech.anno.project.vo.ReviewVO;
import cn.staitech.anno.utils.PageMaster;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author 86186
* @description 针对表【tb_review】的数据库操作Service
* @createDate 2023-09-15 13:05:15
*/
public interface ReviewService extends IService<Review> {

    void exportReview(Long projectId,Long slideId)throws Exception;

    void csvExportReviewCurrent(Long projectId,List<Long> slideIds)throws Exception;
    DownTask csvExportReview(Long projectId, List<Long> slideIds)throws Exception;

    int insert(ReviewIN req) throws Exception;

    int update(ReviewUP req) throws Exception;

    PageMaster<ReviewRoundOutVO> pageReviewRound(Page page, ReviewRoundIN params);


}
