package cn.staitech.anno.project.service;

import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.vo.*;
import cn.staitech.anno.utils.PageMaster;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface SlideService extends IService<Slide> {

    PageMaster<SlideVO> pageSlides(Page page, SlideQueryIN params) throws Exception;

    List<SlideAnnoStatisticsVO> getSlideAnnoStatistics(SlideQueryIN params) throws Exception;

    void slideAnnoStatisticsExport(SlideQueryIN params) throws Exception;

    PageMaster<ReviewSlideVO> pageReviewSlide(Page page, ReviewSlideIN params);


}
