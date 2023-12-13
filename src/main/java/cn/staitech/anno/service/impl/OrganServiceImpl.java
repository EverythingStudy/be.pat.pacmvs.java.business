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

import cn.staitech.anno.domain.Organ;
import cn.staitech.anno.mapper.OrganMapper;
import cn.staitech.anno.service.OrganService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.organ.InsertOrganVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.bean.BeanUtils;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 种属
 */
@Service
class OrganServiceImpl extends ServiceImpl<OrganMapper, Organ> implements OrganService {

    @Resource
    OrganMapper organMapper;

    @Override
    public Map<String, String> selectMap() {
        return select(false);
    }

    @Override
    public Map<String, String> selectMapEn() {
        return select(true);
    }

    /**
     * 根据种属编号获取脏器列表
     *
     * @param speciesId
     * @return
     */
    @Override
    public List<Organ> getOrganBySpeciesId(String speciesId) {
        List<Organ> list = organMapper.getOrganBySpeciesId(speciesId);
        for (Organ organ : list) {
            // 中英文
            if (LanguageUtils.isEn()) {
                organ.setName(organ.getNameEn());
            }
        }
        return list;
    }

    public Map<String, String> select(boolean en) {
        List<Organ> list = organMapper.selectList();
        if (en) {
            return list.stream().collect(Collectors.toMap(item -> item.getSpeciesCode().concat(item.getOrganId()), Organ::getNameEn));
        } else {
            return list.stream().collect(Collectors.toMap(item -> item.getSpeciesCode().concat(item.getOrganId()), Organ::getName));
        }
    }
    
	@Override
	public R<Organ> add(InsertOrganVO req) {
		//校验种属名称是否已经重复
		QueryWrapper<Organ> queryNameWrapper = new QueryWrapper<>();
		queryNameWrapper.eq("name", req.getName());
		List<Organ> nameList = organMapper.selectList(queryNameWrapper);
		if(CollectionUtils.isNotEmpty(nameList)){
			return R.fail(MessageSource.M("InsertOrganVO.NAME.EXIST"));
		}

		//校验种属编码 是否已经重复
		QueryWrapper<Organ> queryOrganIdWrapper = new QueryWrapper<>();
		queryOrganIdWrapper.eq("organ_id", req.getOrganId());
		List<Organ> organWrapperList = organMapper.selectList(queryOrganIdWrapper);
		if(CollectionUtils.isNotEmpty(organWrapperList)){
			return R.fail(MessageSource.M("InsertOrganVO.ORGANID.EXIST"));
		}

		Organ organ = new Organ();
		if(StringUtils.isEmpty(req.getNameEn())){
			organ.setNameEn(req.getName());
		}
		BeanUtils.copyProperties(req, organ);
		int insertStatus = organMapper.insert(organ);
		if (insertStatus > 0) {
			return R.ok(organ, MessageSource.M("INSERT_SUCCESS"));
		}
		return R.fail(MessageSource.M("INSERT_FAILURE"));
	}

}
