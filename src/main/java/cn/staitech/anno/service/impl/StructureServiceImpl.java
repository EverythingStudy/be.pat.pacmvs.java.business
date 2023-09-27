package cn.staitech.anno.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.staitech.anno.domain.organ.Organ;
import cn.staitech.anno.domain.structure.Structure;
import cn.staitech.anno.mapper.StructureMapper;
import cn.staitech.anno.service.StructureService;

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
		return list;
	}

	@Override
	public List<Organ> getOrganBySpeciesId(String speciesId) {
		Structure structure = new Structure();
		structure.setSpeciesId(speciesId);
		List<Organ> list = structureMapper.getOrganBySpeciesId(structure);
		return list;
	}

}
