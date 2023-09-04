package cn.staitech.anno.service;

import cn.staitech.anno.domain.vo.reportRecord.ReportRecordAddVO;

/**
 * 
* @ClassName: DiagnosticStatisticsService
* @Description:诊断统计
* @author wanglibei
* @date 2023年7月18日
* @version V1.0
 */
public interface DiagnosticStatisticsService {
	/**
     * 创建报告
     * @return
     * @throws Exception
     */
	public String createRpt(ReportRecordAddVO recordAddVO)throws Exception;
}
