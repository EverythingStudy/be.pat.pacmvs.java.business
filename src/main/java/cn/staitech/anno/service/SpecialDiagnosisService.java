package cn.staitech.anno.service;

import java.util.List;

import cn.staitech.anno.domain.diagnosis.SpecialDiagnosis;
import cn.staitech.anno.domain.vo.diagnosis.SpecialDiagnosisAddVo;
import cn.staitech.anno.domain.vo.diagnosis.SpecialDiagnosisVo;
import cn.staitech.anno.domain.vo.diagnosis.SysDictDataVo;
import cn.staitech.anno.domain.vo.diagnosis.SysDictResultVo;
import cn.staitech.anno.domain.vo.diagnosis.VisceraVo;
import cn.staitech.common.core.domain.R;

/**
 * 
 * @ClassName: SpecialDiagnosisService
 * @Description:人工诊断处理
 * @author wanglibei
 * @date 2023年6月28日
 * @version V1.0
 */
public interface SpecialDiagnosisService {
	
	//查询诊断数据列表
	public List<SpecialDiagnosisVo> getSpecialDiagnosisVo(String specialImageId,String projctId,String specialId,String groupId);
	//保存/修改诊断数据
	public int saveOrUpdateSpecialDiagnosisVo(List<SpecialDiagnosisAddVo>  addVoList);
	
//	public List<SysDictDataVo> getTagSearch(String dictType,long viscusCode);
	
	public List<SysDictDataVo> getCommonTag(String dictType);
	
	public List<VisceraVo> getRelationshipTag(String dictType);

	
	public void deleteSpecialDiagnosisVo( Long specialDiagnosisId);
	
	public SpecialDiagnosis getSpecialDiagnosis(Long specialDiagnosisId); 
	
	public SysDictResultVo getSysDictResultVo(); 
}
