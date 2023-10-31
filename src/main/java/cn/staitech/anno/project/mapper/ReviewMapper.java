package cn.staitech.anno.project.mapper;

import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.vo.ReviewRoundIn;
import cn.staitech.anno.project.vo.ReviewVO;
import cn.staitech.anno.vo.reviewround.ReviewRoundOutVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 86186
 * @description 针对表【tb_review】的数据库操作Mapper
 * @createDate 2023-09-15 13:05:15
 * @Entity cn.staitech.anno.project.domain.Review
 */
public interface ReviewMapper extends BaseMapper<Review> {
    List<ReviewVO> exportReview(@Param("params") Map params);

    IPage<ReviewRoundOutVO> pageReviewRound(@Param("page") Page page, @Param("params") ReviewRoundIn params);

    Review selectSlide(Long slideId);
}




