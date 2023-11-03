package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.round.Round;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-10 13:08:31
 * @Description: 轮次Mapper
 */

public interface RoundMapper extends BaseMapper<Round> {

    List<Round> selectList();
}
