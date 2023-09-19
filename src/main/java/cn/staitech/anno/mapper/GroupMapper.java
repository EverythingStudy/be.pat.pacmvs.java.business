package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 分组 数据层
 *
 * @author zmj
 */
public interface GroupMapper extends BaseMapper<Group> {


    List<Group> selectList();
}