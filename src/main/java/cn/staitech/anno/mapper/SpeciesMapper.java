package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.species.Species;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-10 13:08:31
 * @Description: 种属Mapper
 */

public interface SpeciesMapper extends BaseMapper<Species> {

    List<Species> selectList();
}
