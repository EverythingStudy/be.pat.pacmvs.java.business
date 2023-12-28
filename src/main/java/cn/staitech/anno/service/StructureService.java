package cn.staitech.anno.service;

import cn.staitech.anno.domain.Structure;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 结构
 */
public interface StructureService extends IService<Structure> {

    Map<String, String> selectMap();

    Map<String, String> selectMapEn();

    List<Structure> getStructureList(String speciesId, String organId);

    Structure getOneStructure(String speciesId, String organId, String structureId);

    List<Structure> getListByStructureId(String structureId);
}
