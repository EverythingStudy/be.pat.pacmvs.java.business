package cn.staitech.anno.service;

import cn.staitech.anno.domain.MarkingStatistic;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.marking.MarkingStatisticSelectVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 智能标注-标注统计-用户标签统计
 *
 * @author wangfeng
 */
public interface MarkingStatisticService {

    /**
     * 标签统计
     *
     * @param selectVO
     * @return
     */
    PageMaster<MarkingStatistic> selectMarkingStatistic(MarkingStatisticSelectVO selectVO);

    /**
     * 标签统计 - 导出execl
     *
     * @param selectVO
     * @param response
     * @throws Exception
     */
    void execlExport(MarkingStatisticSelectVO selectVO, HttpServletResponse response) throws Exception;
}
