package cn.staitech.anno.project.mapper;

import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.vo.ReviewVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

    Review selectSlide(Long slideId);
}




