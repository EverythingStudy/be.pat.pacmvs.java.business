package cn.staitech.anno.project.service;

import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.vo.ReviewVO;
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

}
