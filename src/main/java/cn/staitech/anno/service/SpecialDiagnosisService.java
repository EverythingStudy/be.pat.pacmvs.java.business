package cn.staitech.anno.service;

import cn.staitech.anno.domain.diagnosis.SpecialDiagnosis;
import cn.staitech.anno.domain.vo.diagnosis.SpecialDiagnosisAddVo;
import cn.staitech.anno.domain.vo.diagnosis.SysDictDataVo;

import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SpecialDiagnosisService
 * @Description:人工诊断处理
 * @date 2023年6月28日
 */
public interface SpecialDiagnosisService {

    /**
     * 保存/修改诊断数据
     *
     * @param addVoList
     * @return
     */
    public int saveOrUpdateSpecialDiagnosisVo(List<SpecialDiagnosisAddVo> addVoList);

    public List<SysDictDataVo> getCommonTag(String dictType);

    public SpecialDiagnosis getSpecialDiagnosis(Long specialDiagnosisId);
}
