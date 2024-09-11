package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Structure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-10 13:08:31
 * @Description: 结构Mapper
 */

public interface StructureMapper extends BaseMapper<Structure> {
    List<Structure> selectList(Structure structure);
}
