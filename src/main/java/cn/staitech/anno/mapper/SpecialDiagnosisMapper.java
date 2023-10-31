package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.diagnosis.SpecialDiagnosis;
import cn.staitech.anno.domain.diagnosis.StatisticsBodyVo;
import cn.staitech.anno.domain.diagnosis.StatisticsHeadVo;

import java.util.List;
import java.util.Map;

public interface SpecialDiagnosisMapper {
    int deleteByPrimaryKey(Long specialDiagnosisId);

    int insert(SpecialDiagnosis record);

    int insertSelective(SpecialDiagnosis record);

    SpecialDiagnosis selectByPrimaryKey(Long specialDiagnosisId);

    int updateByPrimaryKeySelective(SpecialDiagnosis record);

    int updateByPrimaryKey(SpecialDiagnosis record);

    public List<SpecialDiagnosis> getSpecialDiagnosisListByParm(Map<String, Object> map);

    public List<StatisticsHeadVo> getStatisticsHeadVoListByParm(Map<String, Object> map);

    public List<StatisticsHeadVo> getSysVisceraNameListByParm(Map<String, Object> map);

    /**
     * 已诊断+未见明显异常
     *
     * @param map
     * @return
     */
    public List<StatisticsBodyVo> getDiagnosedListByParm(Map<String, Object> map);

    /**
     * 查询专题下所有的病理改变+部位的组合体
     *
     * @param map
     * @return
     */
    public List<StatisticsBodyVo> getLessionListByParm(Map<String, Object> map);

    /**
     * 查询专题下所有的病理改变+部位的组合体+数据统计信息
     *
     * @param map
     * @return
     */
    public List<StatisticsBodyVo> getLessionStaticsListByParm(Map<String, Object> map);

    /**
     * 查询专题下所有的病理改变+部位的组合体+程度 数据统计信息
     *
     * @param map
     * @return
     */
    public List<StatisticsBodyVo> getLessionGradeStaticsListByParm(Map<String, Object> map);


    /**
     * 查询专题下所有的病理信息
     *
     * @param map
     * @return
     */
    public List<StatisticsBodyVo> getNeedStaticsListByParm(Map<String, Object> map);


    /**
     * 查询专题下所有的病理信息
     *
     * @param map
     * @return
     */
    public List<StatisticsBodyVo> getStaticsVisceraListByParm(Map<String, Object> map);

}