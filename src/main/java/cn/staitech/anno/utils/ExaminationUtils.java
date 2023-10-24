package cn.staitech.anno.utils;

import cn.staitech.anno.domain.ExaminationLog;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.vo.ExaminationStateVO;
import cn.staitech.anno.service.ExaminationLogService;
import cn.staitech.anno.service.ExaminationService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 复核工具类
 *
 * @author: YL
 * @email: yangl@staitech.cn
 * @date: 2022/9/13 星期二 13:33
 */
@Component
public class ExaminationUtils {

    public static ExaminationUtils examinationUtils;
    @Resource
    private ExaminationService examinationService;
    @Resource
    private ExaminationLogService examinationLogService;
    @Resource
    private SlideService slideService;

    /**
     * 修改复核状态并查看切片信息
     *
     * @param examinationStateVo
     * @param slideId
     * @return
     */
    public static Slide getSlide(ExaminationStateVO examinationStateVo, Long slideId) {
        examinationUtils.examinationService.updateExaminationState(examinationStateVo);
        return examinationUtils.slideService.selectById(slideId);
    }

    /**
     * 记录复核状态
     *
     * @param examinationLog
     * @param slide
     * @param slide1
     */
    public static void examinationStateLog(ExaminationLog examinationLog, Slide slide, Integer slide1) {
        examinationLog.setSlideId(slide.getSlideId());
        examinationLog.setProjectId(slide.getProjectId());
        examinationLog.setImageId(slide.getImageId());
        examinationLog.setExaminationFlag(slide1);
        examinationLog.setCreateBy(SecurityUtils.getUserId());
        examinationUtils.examinationLogService.insertExaminationLog(examinationLog);
    }

    @PostConstruct
    public void init() {
        examinationUtils = this;
        examinationUtils.examinationService = this.examinationService;
        examinationUtils.examinationLogService = this.examinationLogService;
        examinationUtils.slideService = this.slideService;
    }
}
