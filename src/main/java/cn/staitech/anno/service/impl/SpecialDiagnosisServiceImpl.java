package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.domain.SysDictData;
import cn.staitech.anno.domain.diagnosis.SpecialDiagnosis;
import cn.staitech.anno.domain.diagnosis.SpecialDiagnosisDetail;
import cn.staitech.anno.domain.vo.diagnosis.*;
import cn.staitech.anno.enums.SysDictTypeEnum;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.mapper.SpecialDiagnosisDetailMapper;
import cn.staitech.anno.mapper.SpecialDiagnosisMapper;
import cn.staitech.anno.mapper.SysDictDataMapper;
import cn.staitech.anno.service.GetUserInformationService;
import cn.staitech.anno.service.SpecialDiagnosisService;
import cn.staitech.anno.service.SysDictDataService;
import cn.staitech.anno.utils.DictUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
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

    @Resource
    private GetUserInformationService getUserInformationService;

    @Override
    public SpecialDiagnosis getSpecialDiagnosis(Long specialDiagnosisId) {
        SpecialDiagnosis diagnosis = specialDiagnosisMapper.selectByPrimaryKey(specialDiagnosisId);
        return diagnosis;
    }

    @Override
    public List<SpecialDiagnosisVo> getSpecialDiagnosisVo(String subImageId, String projctId, String specialId, String groupId) {
        List<SpecialDiagnosisVo> voList = new ArrayList<SpecialDiagnosisVo>();

        Map<String, Object> map = new HashMap<>(16);
        map.put("projectId", Long.valueOf(projctId));
        map.put("subImageId", Long.valueOf(subImageId));
        map.put("specialId", Long.valueOf(specialId));
        map.put("groupId", Long.valueOf(groupId));
        map.put("status", 1);
        map.put("deleteFlag", 1);
        int index = 0;
        List<SpecialDiagnosis> list = specialDiagnosisMapper.getSpecialDiagnosisListByParm(map);
        if (CollectionUtils.isNotEmpty(list)) {
            for (SpecialDiagnosis diagn : list) {
                SpecialDiagnosisVo vo = new SpecialDiagnosisVo();
                BeanUtils.copyProperties(diagn, vo);
                Long diagnosisId = diagn.getSpecialDiagnosisId();
                Long createBy = diagn.getCreateBy();
                // 根据创建人查询名称
                SysUser loginUser = getUserInformationService.selectById(createBy);
                vo.setCreateUser(loginUser.getNickName());
                vo.setIndex(index);
                map.put("specialDiagnosisId", diagnosisId);
                // 查询所有的明细
                List<SpecialDiagnosisDetail> detailList = specialDiagnosisDetailMapper.getSpecialDiagnosisDetailListByParm(map);

                String visceraTag = "";
                for (SpecialDiagnosisDetail detail : detailList) {
                    String dictType = detail.getDictType();
                    String tags = detail.getTags();
                    String tagName = detail.getTagName();
                    String customizeTagName = detail.getCustomizeTagName();
                    //根据不同的标签去查询value值
                    if (StringUtils.isNotEmpty(tags)) {
                        if (dictType.equals(SysDictTypeEnum.organization.label())) {
                            vo.setViscera(Long.valueOf(tags));
                            visceraTag = tags;
                        } else if (dictType.equals(SysDictTypeEnum.lesion.label())) {
                            vo.setLesion(Long.valueOf(tags));
                            vo.setLesionName(tagName);
                            if (StringUtils.isNotEmpty(customizeTagName)) {
                                vo.setLesionWord(customizeTagName);
                            }
                        } else if (dictType.equals(SysDictTypeEnum.grade.label())) {
                            vo.setGrade(Long.valueOf(tags));
                        }
                        if (dictType.equals(SysDictTypeEnum.position.label())) {
                            List<Object> labelList = transArray(tags);
                            vo.setPositionList(labelList);
                            List<Object> positionNameList = transArray2(tagName);
                            vo.setPositionNameList(positionNameList);
                            if (StringUtils.isNotEmpty(customizeTagName)) {
                                vo.setPositionWord(customizeTagName);
                            }
                        } else if (dictType.equals(SysDictTypeEnum.ddefinition.label())) {
                            List<Object> labelList = transArray(tags);
                            vo.setDdefinitionList(labelList);
                        }
                    }
                }

                // 根据脏器标签查询他对应的部位和dde的列表
                List<VisceraVo> relationshipList = new ArrayList<>();
                relationshipList = getSelectRelationship(SysDictTypeEnum.organization.label(), visceraTag);
                vo.setVisceraList(relationshipList);

                if (createBy.equals(SecurityUtils.getUserId())) {
                    vo.setEditStatus(1);
                    vo.setDisable(true);
                } else {
                    vo.setDisable(false);
                }

                voList.add(vo);
                index++;
            }

        }
        return voList;
    }

    public List<Object> transArray(String tags) {
        String[] tagArray = tags.split(",");
        List<Object> cdids = new ArrayList<>();
        for (String value : tagArray) {
            cdids.add(Long.valueOf(value));
        }
        return cdids;
    }

    public List<Object> transArray2(String tagName) {
        String[] tagArray = tagName.split(";");

        List<Object> cdids = new ArrayList<>();
        for (String value : tagArray) {
            cdids.add(value);
        }
        return cdids;
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
        Map<String, Object> map = new HashMap<>();
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
        Map<String, Object> dictMap = new HashMap<>();
        dictMap.put("dictType", dictType);
        List<SysDictDataVo> list = sysDictDataService.getSysDictDataVoListByParm(dictMap);
        if (CollectionUtils.isNotEmpty(list)) {
            for (SysDictDataVo dict : list) {
                dict.setDictValueInt(Integer.valueOf(dict.getDictValue()));
            }
        }
        return list;
    }


    @Override
    public List<VisceraVo> getRelationshipTag(String dictType) {
        List<VisceraVo> list = new ArrayList<>();
        Map<String, Object> dictMap = new HashMap<>();
        dictMap.put("dictType", dictType);
        List<SysDictDataVo> orgainlist = sysDictDataService.getSysDictDataVoListByParm(dictMap);
        if (CollectionUtils.isNotEmpty(orgainlist)) {
            for (SysDictDataVo vo : orgainlist) {
                VisceraVo visVo = new VisceraVo();
                BeanUtils.copyProperties(vo, visVo);
                String dictValue = vo.getDictValue();
                visVo.setDictValueInt(Integer.valueOf(dictValue));
                //根据sortvalue 查询部位
                Map<String, Object> positionMap = new HashMap<>();
                positionMap.put("dictType", SysDictTypeEnum.position.label());
                if (StringUtils.isNoneEmpty(dictValue)) {
                    positionMap.put("filter", dictValue);
                }
                List<SysDictDataVo> positionlist = new ArrayList<>();
                positionlist = sysDictDataService.getSysDictDataVoListByParm(positionMap);
                if (CollectionUtils.isNotEmpty(positionlist)) {
                    for (SysDictDataVo pvo : positionlist) {
                        pvo.setDictValueInt(Integer.valueOf(pvo.getDictValue()));
                    }
                }
                visVo.setPositionList(positionlist);
                //根据sortvalue  病理改变
                Map<String, Object> lesionMap = new HashMap<>();
                lesionMap.put("dictType", SysDictTypeEnum.lesion.label());
                if (StringUtils.isNoneEmpty(dictValue)) {
                    lesionMap.put("filter", dictValue);
                }
                List<SysDictDataVo> lesionList = new ArrayList<>();
                lesionList = sysDictDataService.getSysDictDataVoListByParm(lesionMap);
                if (CollectionUtils.isNotEmpty(lesionList)) {
                    for (SysDictDataVo lvo : lesionList) {
                        lvo.setDictValueInt(Integer.valueOf(lvo.getDictValue()));
                    }
                }
                visVo.setLesionList(lesionList);
                list.add(visVo);
            }
        }
        return list;
    }

    /**
     * 获取下拉数据
     * @param dictType
     * @param filter
     * @return
     */
    public List<VisceraVo> getSelectRelationship(String dictType, String filter) {
        //先从缓存读取，如果没有在从库里查询
        String cacheKey = "special_getSelectRelationship_" + dictType + "_" + filter;
        List<VisceraVo> list = DictUtils.getVisceraVoCache(cacheKey);
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
            Map<String, Object> dictMap = new HashMap<>();
            dictMap.put("dictType", dictType);
            dictMap.put("dictValue", filter);

            List<SysDictDataVo> orgainlist = sysDictDataService.getSysDictDataVoListByParm(dictMap);
            if (CollectionUtils.isNotEmpty(orgainlist)) {
                for (SysDictDataVo vo : orgainlist) {
                    VisceraVo visVo = new VisceraVo();
                    BeanUtils.copyProperties(vo, visVo);
                    String dictValue = vo.getDictValue();
                    visVo.setDictValueInt(Integer.valueOf(dictValue));
                    //根据sortvalue 查询部位
                    Map<String, Object> positionMap = new HashMap<>();
                    positionMap.put("dictType", SysDictTypeEnum.position.label());
                    if (StringUtils.isNoneEmpty(dictValue)) {
                        positionMap.put("filter", dictValue);
                    }
                    List<SysDictDataVo> positionlist = new ArrayList<>();
                    positionlist = sysDictDataService.getSysDictDataVoListByParm(positionMap);
                    if (CollectionUtils.isNotEmpty(positionlist)) {
                        for (SysDictDataVo pvo : positionlist) {
                            pvo.setDictValueInt(Integer.valueOf(pvo.getDictValue()));
                        }
                    }
                    visVo.setPositionList(positionlist);
                    //根据sortvalue  病理改变
                    Map<String, Object> lesionMap = new HashMap<>();
                    lesionMap.put("dictType", SysDictTypeEnum.lesion.label());
                    if (StringUtils.isNoneEmpty(dictValue)) {
                        lesionMap.put("filter", dictValue);
                    }
                    List<SysDictDataVo> lesionList = new ArrayList<>();
                    lesionList = sysDictDataService.getSysDictDataVoListByParm(lesionMap);
                    if (CollectionUtils.isNotEmpty(lesionList)) {
                        for (SysDictDataVo lvo : lesionList) {
                            lvo.setDictValueInt(Integer.valueOf(lvo.getDictValue()));
                        }
                    }
                    visVo.setLesionList(lesionList);
                    list.add(visVo);
                }
            }
            // add 缓存
            DictUtils.setVisceraVoCache(cacheKey, list);
        }
        return list;
    }


    @Override
    public void deleteSpecialDiagnosisVo(Long specialDiagnosisId) {
        SpecialDiagnosis diagnosis = specialDiagnosisMapper.selectByPrimaryKey(specialDiagnosisId);
        SpecialDiagnosis record = new SpecialDiagnosis();
        record.setSpecialDiagnosisId(specialDiagnosisId);
        record.setDeleteFlag(0);
        record.setStatus(0);
        specialDiagnosisMapper.updateByPrimaryKeySelective(record);
        //查询下当前数据下是否还有数据，如果没有数据了，修改为未诊断
        Map<String, Object> map = new HashMap<>();
        map.put("projectId", diagnosis.getProjectId());
        map.put("subImageId", diagnosis.getSubImageId());
        map.put("specialId", diagnosis.getSpecialId());
        map.put("groupId", diagnosis.getGroupId());
        map.put("status", 1);
        map.put("deleteFlag", 1);
        List<SpecialDiagnosis> list = specialDiagnosisMapper.getSpecialDiagnosisListByParm(map);
        if (CollectionUtils.isEmpty(list)) {
            SpecialDiagnosisAddVo sav = new SpecialDiagnosisAddVo();
            BeanUtils.copyProperties(diagnosis, sav);
            sav.setDiagnosisStatus(0);
            List<SpecialDiagnosisAddVo> diagnosisList = new ArrayList<>();
            diagnosisList.add(sav);
            slideMapper.updateBatchBySpecialDiagnosis(diagnosisList);
        }

    }

    @Override
    public SysDictResultVo getSysDictResultVo() {
        //先从缓存获取
        String cacheKeyi = "special_viscera_organization_1";
        SysDictResultVo vo = DictUtils.getSysDictResultVoCache(cacheKeyi);
        if (null == vo) {
            vo = new SysDictResultVo();
            Map<Integer, String> labelMap = new HashMap<>();
            labelMap.put(SysDictTypeEnum.ddefinition.value(), SysDictTypeEnum.ddefinition.label());
            labelMap.put(SysDictTypeEnum.grade.value(), SysDictTypeEnum.grade.label());

            for (Map.Entry<Integer, String> entry : labelMap.entrySet()) {
                Integer dictTypeKey = entry.getKey();
                String dictTypeStr = entry.getValue();
                List<SysDictDataVo> tagList = new ArrayList<>();
                tagList = getCommonTag(dictTypeStr);
                if (dictTypeKey == SysDictTypeEnum.ddefinition.value()) {
                    //2:病理改变 sys_lesion
                    vo.setDdefinitionList(tagList);
                } else if (dictTypeKey == SysDictTypeEnum.grade.value()) {
                    //4:病变级别 sys_grade
                    vo.setGradeList(tagList);
                }

            }

            List<VisceraVo> relationshipList = new ArrayList<>();
            relationshipList = getRelationshipTag(SysDictTypeEnum.organization.label());
            vo.setVisceraList(relationshipList);
            DictUtils.setSysDictResultVoCache(cacheKeyi, vo);
        }
        return vo;
    }
}
