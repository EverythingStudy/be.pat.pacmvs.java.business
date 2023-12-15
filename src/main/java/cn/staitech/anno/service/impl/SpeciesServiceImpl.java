package cn.staitech.anno.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.staitech.anno.domain.Species;
import cn.staitech.anno.mapper.SpeciesMapper;
import cn.staitech.anno.service.SpeciesService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.Species.InsertSpeciesVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.bean.BeanUtils;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 种属
 */
@Service
class SpeciesServiceImpl extends ServiceImpl<SpeciesMapper, Species> implements SpeciesService {

	@Resource
	private SpeciesMapper speciesMapper;

    @Override
    public Map<String, String> selectMap() {
        return select(false);
    }

    @Override
    public Map<String, String> selectMapEn() {
        return select(true);
    }
    
    public Map<String, String> select(boolean en) {
        List<Species> list = speciesMapper.selectList();
        if (en) {
            return list.stream().collect(Collectors.toMap(Species::getSpeciesId, Species::getNameEn));
        } else {
            return list.stream().collect(Collectors.toMap(Species::getSpeciesId, Species::getName));
        }
    }


	@Override
	public R<Species> add(InsertSpeciesVO req) {
		//校验种属名称是否已经重复
		QueryWrapper<Species> queryNameWrapper = new QueryWrapper<>();
		queryNameWrapper.eq("name", req.getName());
		List<Species> nameList = speciesMapper.selectList(queryNameWrapper);
		if(CollectionUtils.isNotEmpty(nameList)){
			return R.fail(MessageSource.M("INSERTSPECIESVO.NAME.EXIST"));
		}

		//校验种属编码 是否已经重复
		QueryWrapper<Species> querySpeciesIdWrapper = new QueryWrapper<>();
		querySpeciesIdWrapper.eq("species_id", req.getSpeciesId());
		List<Species> speciesIdWrapperList = speciesMapper.selectList(querySpeciesIdWrapper);
		if(CollectionUtils.isNotEmpty(speciesIdWrapperList)){
			return R.fail(MessageSource.M("INSERTSPECIESVO.SPECIESID.EXIST"));
		}

		Species species = new Species();
		if(StringUtils.isEmpty(req.getNameEn())){
			species.setNameEn(req.getName());
		}
		BeanUtils.copyProperties(req, species);
		int insertStatus = speciesMapper.insert(species);
		if (insertStatus > 0) {
			return R.ok(species, MessageSource.M("INSERT_SUCCESS"));
		}
		return R.fail(MessageSource.M("INSERT_FAILURE"));
	}

}
