package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.organ.Organ;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-09-10 13:08:31
 * @Description: 脏器Mapper
 */

public interface OrganMapper extends BaseMapper<Organ> {

    List<Organ> selectList();
}
