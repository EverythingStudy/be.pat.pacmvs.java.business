package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.domain.topic.TopicIdName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author wangfeng
 * @version 2.0
 * @description 切片（原图像）专题
 * @date 2023/06/02 13:59:59
 */
public interface TopicMapper extends BaseMapper<Topic> {
    /**
     * 专题列表
     *
     * @param projectTypeId
     * @return
     */
    List<TopicIdName> selectIdNameList(Integer projectTypeId);
}

