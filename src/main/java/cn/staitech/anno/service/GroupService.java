package cn.staitech.anno.service;

import cn.staitech.anno.domain.Group;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 分组服务层
 *
 * @author wangfeng
 */
public interface GroupService extends IService<Group> {
    Map<Long, String> selectMap();
}
