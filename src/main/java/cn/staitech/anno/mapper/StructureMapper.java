package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.organ.Organ;
import cn.staitech.anno.domain.structure.Structure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-10 13:08:31
 * @Description: 结构Mapper
 */

public interface StructureMapper extends BaseMapper<Structure> {

    List<Structure> selectList();
    List<Structure> getStructureList(Structure structure);
}
