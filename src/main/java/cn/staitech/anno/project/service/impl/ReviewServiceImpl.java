package cn.staitech.anno.project.service.impl;

import cn.staitech.anno.project.vo.ReviewVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.service.ReviewService;
import cn.staitech.anno.project.mapper.ReviewMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 86186
* @description 针对表【tb_review】的数据库操作Service实现
* @createDate 2023-09-15 13:05:15
*/
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
    implements ReviewService{


    @Override
    public void exportReview(Long projectId,Long slideId)throws Exception{
        Map params = new HashMap();
        if (projectId!= null){
            params.put("projectId",projectId);
        }
        if (slideId!= null){
            params.put("slideId",slideId);
        }
        List<ReviewVO> reviewVOS = getBaseMapper().exportReview(params);

    }

}




