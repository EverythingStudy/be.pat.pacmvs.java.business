package cn.staitech.anno.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.vo.reportRecord.ReportRecordAddVO;
import cn.staitech.anno.mapper.SpecialDiagnosisDetailMapper;
import cn.staitech.anno.mapper.SpecialDiagnosisMapper;
import cn.staitech.anno.mapper.SpecialMapper;
import cn.staitech.anno.service.DiagnosticReportService;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: DiagnosticStatisticsServiceImpl
 * @Description:诊断统计处理
 * @author wanglibei
 * @date 2023年7月18日
 * @version V1.0
 */
@Slf4j
@Service
public class DiagnosticReportServiceImpl implements DiagnosticReportService {

	private final static String SUFFIX = ".docx";

	@Resource
	private SpecialDiagnosisMapper specialDiagnosisMapper;

	@Resource
	private SpecialDiagnosisDetailMapper specialDiagnosisDetailMapper;

	@Value("${rpt.dir:../REPORT}")
	private String RPT_DIR;

	@Resource
	private SpecialMapper specialMapper;


	@SuppressWarnings("unused")
	@Override
	public void createRpt(Special special,ReportRecordAddVO recordAddVO,List<List<String>> tDataList,List<Map<String, Object>> tableMapList ,String rptPath) throws Exception {
		
	}

}
