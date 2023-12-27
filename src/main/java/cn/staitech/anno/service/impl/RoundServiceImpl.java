package cn.staitech.anno.service.impl;

import cn.staitech.anno.mapper.RoundMapper;
import cn.staitech.anno.service.RoundService;
import cn.staitech.anno.vo.round.Round;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 轮次
 */
@Service
class RoundServiceImpl extends ServiceImpl<RoundMapper, Round> implements RoundService {

    @Resource
    private RoundMapper roundMapper;

    @Override
    public Map<Long, String> selectMap() {
        List<Round> list = roundMapper.selectList();
        Map<Long, String> map = list.stream()
                .collect(Collectors.toMap(Round::getRoundId, Round::getRoundName));
        return map;
    }

    @Override
    public Map<Long, String> selectMapEn() {
        List<Round> list = roundMapper.selectList();
        Map<Long, String> map = list.stream()
                .collect(Collectors.toMap(Round::getRoundId, Round::getRoundNameEn));
        return map;
    }
}
