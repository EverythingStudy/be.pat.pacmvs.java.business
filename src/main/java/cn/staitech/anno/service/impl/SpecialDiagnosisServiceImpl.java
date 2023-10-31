package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.domain.SysDictData;
import cn.staitech.anno.domain.diagnosis.SpecialDiagnosis;
import cn.staitech.anno.domain.diagnosis.SpecialDiagnosisDetail;
import cn.staitech.anno.domain.diagnosis.SpecialDiagnosisAddVo;
import cn.staitech.anno.domain.diagnosis.SysDictDataVo;
import cn.staitech.anno.domain.diagnosis.SysDictTagVo;
import cn.staitech.anno.enums.SysDictTypeEnum;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.mapper.SpecialDiagnosisDetailMapper;
import cn.staitech.anno.mapper.SpecialDiagnosisMapper;
import cn.staitech.anno.mapper.SysDictDataMapper;
import cn.staitech.anno.service.SpecialDiagnosisService;
import cn.staitech.anno.service.SysDictDataService;
import cn.staitech.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SpecialDiagnosisServiceImpl
 * @Description:诊断处理
 * @date 2023年6月29日
 */
@Slf4j
@Service
public class SpecialDiagnosisServiceImpl implements SpecialDiagnosisService {
    @Resource
    private SpecialDiagnosisMapper specialDiagnosisMapper;
    @Resource
    private SpecialDiagnosisDetailMapper specialDiagnosisDetailMapper;
    @Resource
    private SysDictDataService sysDictDataService;
    @Resource
    private SysDictDataMapper sysDictDataMapper;
    @Resource
    private SlideMapper slideMapper;

    @Override
    public SpecialDiagnosis getSpecialDiagnosis(Long specialDiagnosisId) {
        SpecialDiagnosis diagnosis = specialDiagnosisMapper.selectByPrimaryKey(specialDiagnosisId);
        return diagnosis;
    }


    /**
     * 诊断保存或修改
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveOrUpdateSpecialDiagnosisVo(List<SpecialDiagnosisAddVo> addVoList) {
        int addStatus = -1;
        int checkFlage = 0;
        for (SpecialDiagnosisAddVo addVo : addVoList) {
            //校验必须要选脏器
            Long viscera = addVo.getViscera();
            Long grade = addVo.getGrade();
            if (null == viscera) {
                checkFlage = -1;
                break;
            }
            if (null == grade) {
                checkFlage = -1;
                break;
            }
            Long specialDiagnosisId = addVo.getSpecialDiagnosisId();
            if (null != specialDiagnosisId) {
                //判断当前人和编辑人是否是同一个人
                Long currentUserId = SecurityUtils.getUserId();
                //查下当前数据创建人是谁
                SpecialDiagnosis specialDiagnosis = specialDiagnosisMapper.selectByPrimaryKey(specialDiagnosisId);
                Long createBy = specialDiagnosis.getCreateBy();
                if (!currentUserId.equals(createBy)) {
                    checkFlage = -2;
                    break;
                }
            }
        }
        if (checkFlage != 0) {
            return checkFlage;
        }
        for (SpecialDiagnosisAddVo addVo : addVoList) {
            long startTime = System.currentTimeMillis();
            SpecialDiagnosis record = new SpecialDiagnosis();
            //处理标签顺序问题
            List<Object> postionList = addVo.getPositionList();
            if (CollectionUtils.isNotEmpty(postionList)) {
                List<Object> newPostionList = transferList(postionList);
                addVo.setPositionList(newPostionList);
            }
            List<Object> ddefinitionList = addVo.getDdefinitionList();
            if (CollectionUtils.isNotEmpty(ddefinitionList)) {
                List<Object> newDdefinitionList = transferList(ddefinitionList);
                addVo.setDdefinitionList(newDdefinitionList);
            }
            BeanUtils.copyProperties(addVo, record);

            int operType = 1;
            if (null == addVo.getSpecialDiagnosisId()) {
                record.setCreateBy(SecurityUtils.getUserId());
                record.setCreateTime(DateUtil.date());
                specialDiagnosisMapper.insertSelective(record);
            } else {
                operType = 2;
                record.setUpdateBy(SecurityUtils.getUserId());
                record.setUpdateTime(DateUtil.date());
                specialDiagnosisMapper.updateByPrimaryKeySelective(record);
            }
            long specialDiagnosisId = record.getSpecialDiagnosisId();
            addStatus = saveOrUpdateDetail(addVo, specialDiagnosisId, operType);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            log.info("人工诊断单条处理运行时间： " + totalTime + " 毫秒");
        }
        if (addStatus == 1) {
            //修改诊断状态
            List<SpecialDiagnosisAddVo> dataList = new ArrayList<>();
            dataList.add(addVoList.get(0));
            slideMapper.updateBatchBySpecialDiagnosis(dataList);
        }

        return checkFlage;
    }

    public List<Object> transferList(List<Object> list) {
        List<Object> retList = new ArrayList<>();
        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Integer value = (Integer) list.get(i);
            newList.add(value);
        }
        Collections.sort(newList);
        ;//按从小到大排序，只能对基本数据类型的包装对象
        retList.addAll(newList);
        return retList;
    }

    /**
     * @param @param addVo
     * @param @param specialDiagnosisId
     * @param @param operType
     * @return void
     * @throws
     * @Title: saveOrUpdateDetail
     * @Description: 明细处理
     */
    public int saveOrUpdateDetail(SpecialDiagnosisAddVo addVo, long specialDiagnosisId, int operType) {
        long startTime = System.currentTimeMillis();
        int addStatus = -1;

        String visceraWord = addVo.getVisceraWord();
        String positionWord = addVo.getPositionWord();
        String ddefinitionWord = addVo.getDdefinitionWord();
        String lesionWord = addVo.getLesionWord();
        String gradeWord = addVo.getGradeWord();

        Long viscera = addVo.getViscera();
        List<Object> visceraList = new ArrayList<>();
        if (null != viscera) {
            visceraList.add(viscera);
        }
        List<Object> positionList = addVo.getPositionList();
        List<Object> ddefinitionList = addVo.getDdefinitionList();

        Long lesion = addVo.getLesion();
        List<Object> lesionList = new ArrayList<>();
        if (null != lesion) {
            lesionList.add(lesion);
        }

        Long grade = addVo.getGrade();
        List<Object> gradeList = new ArrayList<>();
        if (null != grade) {
            gradeList.add(grade);
        }


        if (operType == 1) {
            SpecialDiagnosisDetail sdd = saveDetail(visceraList, visceraWord, specialDiagnosisId, SysDictTypeEnum.organization.label(), "", "");
            saveDetail(positionList, positionWord, specialDiagnosisId, SysDictTypeEnum.position.label(), sdd.getTags(), String.valueOf(viscera));
            saveDetail(lesionList, lesionWord, specialDiagnosisId, SysDictTypeEnum.lesion.label(), sdd.getTags(), String.valueOf(viscera));
            saveDetail(ddefinitionList, ddefinitionWord, specialDiagnosisId, SysDictTypeEnum.ddefinition.label(), "", "");
            saveDetail(gradeList, gradeWord, specialDiagnosisId, SysDictTypeEnum.grade.label(), "", "");
            addStatus = 1;
        } else {
            SpecialDiagnosisDetail sdd = updateDetail(visceraList, visceraWord, specialDiagnosisId, SysDictTypeEnum.organization.label(), "", "");
            updateDetail(positionList, positionWord, specialDiagnosisId, SysDictTypeEnum.position.label(), sdd.getTags(), String.valueOf(viscera));
            updateDetail(lesionList, lesionWord, specialDiagnosisId, SysDictTypeEnum.lesion.label(), sdd.getTags(), String.valueOf(viscera));
            updateDetail(ddefinitionList, ddefinitionWord, specialDiagnosisId, SysDictTypeEnum.ddefinition.label(), "", "");
            updateDetail(gradeList, gradeWord, specialDiagnosisId, SysDictTypeEnum.grade.label(), "", "");
        }

        return addStatus;
    }


    /**
     * @param @param  labelList
     * @param @param  labelWord
     * @param @param  specialDiagnosisId
     * @param @param  dictType
     * @param @param  filter
     * @param @return
     * @return SpecialDiagnosisDetail
     * @throws
     * @Title: saveDetail
     * @Description: 明细save到数据库
     */
    public SpecialDiagnosisDetail saveDetail(List<Object> labelList, String labelWord, Long specialDiagnosisId,
                                             String dictType, String filter, String viscera) {
        SpecialDiagnosisDetail detail = new SpecialDiagnosisDetail();
        detail.setSpecialDiagnosisId(specialDiagnosisId);
        detail.setDictType(dictType);
        String labelIds = "";
        if (StringUtils.isNotEmpty(labelWord)) {
            detail.setCustomizeTagName(labelWord);
        }

        if (CollectionUtils.isNotEmpty(labelList)) {
            labelIds = labelList.stream().map(String::valueOf).collect(Collectors.joining(","));
        }

        detail.setTags(labelIds);
        String labelName = getLabelNameByParm(dictType, labelIds, viscera);
        if (StringUtils.isNotEmpty(labelName) && labelName.contains("其它（自定义）")) {
            labelName = labelName.replaceAll("其它（自定义）", labelWord);
        }
        if (StringUtils.isNotEmpty(labelName)) {
            detail.setTagName(labelName);
        }
        detail.setCreateTime(DateUtil.date());
        if (StringUtils.isNotEmpty(labelIds) && StringUtils.isNotEmpty(detail.getTags())) {
            specialDiagnosisDetailMapper.insertSelective(detail);
        }
        return detail;
    }


    /**
     * @param @param  labelList
     * @param @param  labelWord
     * @param @param  specialDiagnosisId
     * @param @param  dictType
     * @param @param  filter
     * @param @return
     * @return SpecialDiagnosisDetail
     * @throws
     * @Title: updateDetail
     * @Description: 明细update到数据库
     */
    public SpecialDiagnosisDetail updateDetail(List<Object> labelList, String labelWord, Long specialDiagnosisId,
                                               String dictType, String filter, String viscera) {
        SpecialDiagnosisDetail detail = new SpecialDiagnosisDetail();
        detail.setSpecialDiagnosisId(specialDiagnosisId);
        detail.setDictType(dictType);
        String labelIds = "";
        if (StringUtils.isNoneEmpty(labelWord)) {
            detail.setCustomizeTagName(labelWord);
        }

        if (CollectionUtils.isNotEmpty(labelList)) {
            labelIds = labelList.stream().map(String::valueOf).collect(Collectors.joining(","));
        }

        detail.setTags(labelIds);
        String labelName = getLabelNameByParm(dictType, labelIds, viscera);
        if (StringUtils.isNotEmpty(labelName) && labelName.contains("其它（自定义）")) {
            labelName = labelName.replaceAll("其它（自定义）", labelWord);
        }
        if (StringUtils.isNotEmpty(labelName)) {
            detail.setTagName(labelName);
        }
        detail.setCreateTime(DateUtil.date());
        // 先查询当前切片之前的数据，有就修改、没有就添加
        Map<String, Object> map = new HashMap<>(16);
        map.put("dictType", dictType);
        map.put("specialDiagnosisId", specialDiagnosisId);
        List<SpecialDiagnosisDetail> detailList = specialDiagnosisDetailMapper.getSpecialDiagnosisDetailListByParm(map);
        if (CollectionUtils.isNotEmpty(detailList)) {
            SpecialDiagnosisDetail ail = detailList.get(0);
            SpecialDiagnosisDetail upAil = new SpecialDiagnosisDetail();
            upAil.setTags(labelIds);
            upAil.setSpecialDiagnosisDetailId(ail.getSpecialDiagnosisDetailId());
            if (StringUtils.isNoneEmpty(labelWord)) {
                upAil.setCustomizeTagName(labelWord);
            }
            specialDiagnosisDetailMapper.updateByPrimaryKeySelective(upAil);
        } else {
            specialDiagnosisDetailMapper.insertSelective(detail);
        }
        return detail;
    }

    public String getLabelNameByParm(String dictType, String labelIds, String viscera) {
        String labelName = "";
        String[] labelArray = labelIds.split(",");
        SysDictTagVo sysDictTagVo = new SysDictTagVo();
        sysDictTagVo.setDictType(dictType);
        sysDictTagVo.setTagIdList(labelArray);
        if (StringUtils.isNotEmpty(viscera)) {
            sysDictTagVo.setFilter(viscera);
        }
        SysDictData sysDictData = sysDictDataMapper.getLabelNameByParm(sysDictTagVo);
        if (null != sysDictData) {
            labelName = sysDictData.getDictLabel();
        }
        if (StringUtils.isEmpty(labelName)) {
            log.info("dictType：" + dictType + " labelIds:" + labelIds + " 未查询到相关信息");
        }
        return labelName;
    }

    @Override
    public List<SysDictDataVo> getCommonTag(String dictType) {
        Map<String, Object> dictMap = new HashMap<>(16);
        dictMap.put("dictType", dictType);
        List<SysDictDataVo> list = sysDictDataService.getSysDictDataVoListByParm(dictMap);
        if (CollectionUtils.isNotEmpty(list)) {
            for (SysDictDataVo dict : list) {
                dict.setDictValueInt(Integer.valueOf(dict.getDictValue()));
            }
        }
        return list;
    }


}
