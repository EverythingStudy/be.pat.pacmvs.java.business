package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.domain.SysDictData;
import cn.staitech.anno.domain.project.ProjectExt;
import cn.staitech.anno.domain.vo.diagnosis.SysDictDataVo;
import cn.staitech.anno.mapper.ProjectExtMapper;
import cn.staitech.anno.mapper.SysDictDataMapper;
import cn.staitech.anno.service.SysDictDataService;
import cn.staitech.common.security.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专题选片表 服务实现类
 * </p>
 *
 * @author wanglibei
 * @since 2023-06-02
 */
@Service
public class SysDictDataServiceImpl implements SysDictDataService {
    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Resource
    private ProjectExtMapper projectExtMapper;

    @Override
    public List<SysDictData> getSysDictDataListByParm(Map<String, Object> map) {
        List<SysDictData> list = sysDictDataMapper.getSysDictDataListByParm(map);
        return list;
    }

    @Override
    public List<SysDictDataVo> getSysDictDataVoListByParm(Map<String, Object> map) {
        List<SysDictDataVo> list = new ArrayList<SysDictDataVo>();
        list = sysDictDataMapper.getSysDictDataVoListByParm(map);
        return list;
    }

    @Override
    public SysDictData getMaxDictSortByParm(Map<String, Object> map) {
        SysDictData sysDictData = sysDictDataMapper.getMaxDictSortByParm(map);
        return sysDictData;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveSysDictDataByParm(String dictValueCn, String dictValueEn, String dictType, String filter) {
        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictLabel(dictValueCn);
        sysDictData.setDictLabelEn(dictValueEn);
        sysDictData.setDictType(dictType);
        sysDictData.setCreateBy(SecurityUtils.getUserId());
        sysDictData.setCreateTime(DateUtil.date());
        if (StringUtils.isNoneEmpty(filter)) {
            sysDictData.setFilter(filter);
        }

        String dictValue = insertSysDictData(sysDictData, dictType);
        return dictValue;
    }

    private synchronized String insertSysDictData(SysDictData sysDictData, String dictType) {
        //获取最大dictSort
        Map<String, Object> map = new HashMap<>();
        map.put("dictType", dictType);
        SysDictData maxSysDictData = sysDictDataMapper.getMaxDictSortByParm(map);
        int currentDictSort = 1;
        String dictValue = "0";
        if (null != maxSysDictData) {
            currentDictSort = maxSysDictData.getDictSort();
            dictValue = maxSysDictData.getDictValue();
        }
        sysDictData.setDictSort(currentDictSort + 1);
        sysDictData.setDictValue(Integer.valueOf(dictValue) + 1 + "");
        sysDictDataMapper.insertSelective(sysDictData);
        return sysDictData.getDictValue();
    }

    @Override
    public ProjectExt getProjectExt(long projectId) {
        ProjectExt projectExt = projectExtMapper.selectById(projectId);
        return projectExt;
    }
}
