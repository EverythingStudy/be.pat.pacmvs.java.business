package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Organ;
import cn.staitech.anno.mapper.OrganMapper;
import cn.staitech.anno.service.OrganService;
import cn.staitech.anno.utils.LanguageUtils;
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
class OrganServiceImpl extends ServiceImpl<OrganMapper, Organ> implements OrganService {

    @Resource
    OrganMapper organMapper;

    @Override
    public Map<String, String> selectMap() {
        List<Organ> list = organMapper.selectList();
        Map<String, String> map = list.stream()
                .collect(Collectors.toMap(item -> item.getSpeciesCode().toString().concat(item.getOrganId().toString()), Organ::getName));
        return map;
    }

    @Override
    public Map<String, String> selectMapEn() {
        List<Organ> list = organMapper.selectList();
        Map<String, String> map = list.stream()
                .collect(Collectors.toMap(item -> item.getSpeciesCode().toString().concat(item.getOrganId().toString()), Organ::getNameEn));
        return map;
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

}
