package cn.staitech.anno.project.mapper;

import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.vo.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 86186
 * @description 针对表【tb_slide(tb_slide)】的数据库操作Mapper
 * @createDate 2023-09-13 17:21:03
 * @Entity cn.staitech.anno.project.domain.Slide
 */
public interface SlideMapperV1 extends BaseMapper<Slide> {
    IPage<SlideVO> pageSlides(@Param("page") Page page, @Param("params") SlideQueryIn params);

    IPage<ReviewSlideVO> pageReviewSlide(@Param("page") Page page, @Param("params") ReviewSlideIn params);

    List<SlideExportVO> querySlides(@Param("params") SlideQueryIn params);

    List<SlideVO> getSlides(@Param("params") SlideQueryIn params);
}




