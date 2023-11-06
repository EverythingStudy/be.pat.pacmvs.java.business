package cn.staitech.anno.project.mapper;

import cn.staitech.anno.project.domain.Marking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author 86186
 * @description 针对表【tb_marking】的数据库操作Mapper
 * @createDate 2023-09-20 09:33:57
 * @Entity cn.staitech.anno.project.domain.Marking
 */
public interface MarkingMapperV1 extends BaseMapper<Marking> {

    List<Marking> selectMarkings();

}




