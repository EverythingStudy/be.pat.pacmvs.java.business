package cn.staitech.anno.mapper;


import cn.staitech.anno.domain.SysDictData;
import cn.staitech.anno.vo.diagnosis.SysDictDataVo;
import cn.staitech.anno.vo.diagnosis.SysDictTagVo;

import java.util.List;
import java.util.Map;

public interface SysDictDataMapper {
    int deleteByPrimaryKey(Long dictCode);

    int insert(SysDictData record);

    int insertSelective(SysDictData record);

    SysDictData selectByPrimaryKey(Long dictCode);

    int updateByPrimaryKeySelective(SysDictData record);

    int updateByPrimaryKey(SysDictData record);

    public List<SysDictData> getSysDictDataListByParm(Map<String, Object> map);

    public SysDictData getMaxDictSortByParm(Map<String, Object> map);

    public List<SysDictDataVo> getSysDictDataVoListByParm(Map<String, Object> map);

    public SysDictData getLabelNameByParm(SysDictTagVo sysDictTagVo);
}