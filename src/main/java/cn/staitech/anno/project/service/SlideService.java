package cn.staitech.anno.project.service;

import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.vo.*;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.system.api.model.LoginUser;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface SlideService extends IService<Slide> {

    PageMaster<SlideVO> pageSlides(Page page, SlideQueryIn params) throws Exception;

    List<SlideAnnoStatisticsVO> getSlideAnnoStatistics(SlideQueryIn params) throws Exception;

    void slideAnnoStatisticsExport(SlideQueryIn params) throws Exception;

    PageMaster<ReviewSlideVO> pageReviewSlide(Page page, ReviewSlideIn params);
    
    public boolean isProjectAmin(LoginUser user,Long projectId);

}
