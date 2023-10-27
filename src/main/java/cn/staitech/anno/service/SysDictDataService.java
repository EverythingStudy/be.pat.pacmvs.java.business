package cn.staitech.anno.service;

import cn.staitech.anno.domain.SysDictData;
import cn.staitech.anno.domain.project.ProjectExt;
import cn.staitech.anno.domain.vo.diagnosis.SysDictDataVo;

import java.util.List;
import java.util.Map;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SysDictDataService
 * @Description:系统字典处理
 * @date 2023年6月28日
 */
public interface SysDictDataService {

    public List<SysDictData> getSysDictDataListByParm(Map<String, Object> map);

    public SysDictData getMaxDictSortByParm(Map<String, Object> map);

    public String saveSysDictDataByParm(String dictValueCn, String dictValueEn, String dictType, String filter);

    public ProjectExt getProjectExt(long projectId);

    public List<SysDictDataVo> getSysDictDataVoListByParm(Map<String, Object> map);
}
