package cn.staitech.anno.service;

import cn.staitech.anno.domain.reportrecord.ReportRecordAddVO;

/**
 * 报告服务
 */
public interface ReportService {

    /**
     * 创建报告
     *
     * @return
     * @throws Exception
     */
    String createRpt(ReportRecordAddVO recordAddVO) throws Exception;

}


