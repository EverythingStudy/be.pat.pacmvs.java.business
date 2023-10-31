package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.diagnosis.SpecialDiagnosisDetail;

import java.util.List;
import java.util.Map;

public interface SpecialDiagnosisDetailMapper {
    int deleteByPrimaryKey(Long specialDiagnosisDetailId);

    int insert(SpecialDiagnosisDetail record);

    int insertSelective(SpecialDiagnosisDetail record);

    SpecialDiagnosisDetail selectByPrimaryKey(Long specialDiagnosisDetailId);

    int updateByPrimaryKeySelective(SpecialDiagnosisDetail record);

    int updateByPrimaryKey(SpecialDiagnosisDetail record);

    public List<SpecialDiagnosisDetail> getSpecialDiagnosisDetailListByParm(Map<String, Object> map);
}