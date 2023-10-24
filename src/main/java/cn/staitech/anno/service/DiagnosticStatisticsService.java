package cn.staitech.anno.service;

import cn.staitech.anno.domain.vo.reportRecord.ReportRecordAddVO;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: DiagnosticStatisticsService
 * @Description:诊断统计
 * @date 2023年7月18日
 */
public interface DiagnosticStatisticsService {
    /**
     * 创建报告
     *
     * @return
     * @throws Exception
     */
    public String createRpt(ReportRecordAddVO recordAddVO) throws Exception;
}
