package cn.staitech.anno.service;

import java.util.List;
import java.util.Map;

import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.vo.reportRecord.ReportRecordAddVO;

/**
 * 
* @ClassName: DiagnosticReportService
* @Description:
* @author wanglibei
* @date 2023年8月3日
* @version V1.0
 */
public interface DiagnosticReportService {
	/**
     * 创建报告
     * @return
     * @throws Exception
     */
	public void createRpt(Special special,ReportRecordAddVO recordAddVO,List<List<String>> tDataList,List<Map<String, Object>> tableMapList ,String rptPath)throws Exception;
}
