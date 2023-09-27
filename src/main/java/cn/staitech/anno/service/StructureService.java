package cn.staitech.anno.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.staitech.anno.domain.organ.Organ;
import cn.staitech.anno.domain.structure.Structure;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 结构
 */
public interface StructureService extends IService<Structure> {

    Map<String, String> selectMap();
    
    List<Structure> getStructureList(String speciesId,String organId);
    
    List<Organ> getOrganBySpeciesId(String speciesId);

    Structure getOneStructure(String speciesId,String organId,String structureId);
}
