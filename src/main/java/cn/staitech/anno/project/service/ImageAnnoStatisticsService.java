package cn.staitech.anno.project.service;


import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.vo.ImageAnnoStatisticsVO;
import cn.staitech.anno.project.vo.SlideQueryIn;
import cn.staitech.common.core.domain.PageResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;


public interface ImageAnnoStatisticsService extends IService<Slide> {

    PageResponse<ImageAnnoStatisticsVO> pageSlides(SlideQueryIn req) throws Exception;

    void slideAnnoStatisticsExport(SlideQueryIn params, HttpServletResponse response) throws Exception;


}
