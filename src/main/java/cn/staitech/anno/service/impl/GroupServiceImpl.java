package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Group;
import cn.staitech.anno.mapper.GroupMapper;
import cn.staitech.anno.service.GroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分组 服务层实现
 *
 * @author wangfeng
 */
@Service
class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Resource
    private GroupMapper groupMapper;

    @Override
    public Map<Long, String> selectMap() {

        List<Group> list = groupMapper.selectList();
        Map<Long, String> map = list.stream()
                .collect(Collectors.toMap(Group::getGroupId, Group::getGroupName));
        return map;
    }
}
