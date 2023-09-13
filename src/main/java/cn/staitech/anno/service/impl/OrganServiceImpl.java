package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.organ.Organ;
import cn.staitech.anno.mapper.OrganMapper;
import cn.staitech.anno.service.OrganService;
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
                .collect(Collectors.toMap(Organ::getKey, Organ::getValue));
        return map;
    }
}
