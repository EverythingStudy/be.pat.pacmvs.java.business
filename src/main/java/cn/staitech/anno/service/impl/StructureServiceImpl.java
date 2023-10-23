package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.structure.Structure;
import cn.staitech.anno.mapper.StructureMapper;
import cn.staitech.anno.service.StructureService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 种属
 */
@Service
class StructureServiceImpl extends ServiceImpl<StructureMapper, Structure> implements StructureService {
    @Resource
    StructureMapper structureMapper;

    @Override
    public Map<String, String> selectMap() {

        List<Structure> list = structureMapper.selectList();
        Map<String, String> map = list.stream()
                .collect(Collectors.toMap(Structure::getStructureId, Structure::getName));
        return map;
    }

    @Override
    public List<Structure> getStructureList(String speciesId, String organId) {
        Structure structure = new Structure();
        structure.setSpeciesId(speciesId);
        structure.setOrganId(organId);
        List<Structure> list = structureMapper.getStructureList(structure);
        if (list.size() == 0) {
            Structure obj = new Structure();
            obj.setName("无关联");
            obj.setNameEn("Unrelated");
            obj.setSpeciesId(speciesId.toString());
            obj.setStructureId(organId.toString());
            list.add(obj);
        }
        return list;
    }

    @Override
    public Structure getOneStructure(String speciesId, String organId, String structureId) {
        Structure structure = new Structure();
        structure.setSpeciesId(speciesId);
        structure.setOrganId(organId);
        structure.setStructureId(structureId);

        QueryWrapper<Structure> queryWrapper = new QueryWrapper<>(structure);
        Structure structureResp = structureMapper.selectOne(queryWrapper);
        return structureResp;
    }
}
