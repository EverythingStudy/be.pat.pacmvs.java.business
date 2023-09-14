package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.species.Species;
import cn.staitech.anno.mapper.SpeciesMapper;
import cn.staitech.anno.service.SpeciesService;
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
class SpeciesServiceImpl extends ServiceImpl<SpeciesMapper, Species> implements SpeciesService {

    @Resource
    SpeciesMapper speciesMapper;

    @Override
    public Map<String, String> selectMap() {

        List<Species> list = speciesMapper.selectList();
        Map<String, String> map = list.stream()
                .collect(Collectors.toMap(Species::getSpeciesId, Species::getName));
        return map;
    }
}
