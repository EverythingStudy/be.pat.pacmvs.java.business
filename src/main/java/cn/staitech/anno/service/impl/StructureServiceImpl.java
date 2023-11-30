package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Structure;
import cn.staitech.anno.mapper.StructureMapper;
import cn.staitech.anno.service.StructureService;
import cn.staitech.common.security.utils.SecurityUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Structure> list = structureMapper.selectList(new Structure());
        Map<String, String> map = list.stream().collect(
                HashMap::new,
                (m, node) -> m.put(node.getStructureId(), node.getName()),
                HashMap::putAll
        );
        return map;
    }

    @Override
    public Map<String, String> selectMapEn() {
        List<Structure> list = structureMapper.selectList(new Structure());
        Map<String, String> map = list.stream().collect(
                HashMap::new,
                (m, node) -> m.put(node.getStructureId(), node.getNameEn()),
                HashMap::putAll
        );
        return map;
    }

    @Override
    public List<Structure> getStructureList(String speciesId, String organId) {
        Structure structure = new Structure();
        structure.setSpeciesId(speciesId);
        structure.setOrganId(organId);
        structure.setType(CommonConstant.STRUCTURE_RO);
        List<Structure> list = structureMapper.selectList(structure);
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

	@Override
	public List<Structure> getListByStructureId(String structureId) {
		Structure structure = new Structure();
		structure.setStructureId(structureId);

		QueryWrapper<Structure> queryWrapper = new QueryWrapper<>(structure);
		List<Structure> list = structureMapper.selectList(queryWrapper);
		return list;
	}
}
