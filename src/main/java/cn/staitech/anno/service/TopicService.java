package cn.staitech.anno.service;

import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.topic.TopicQueryIn;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author: wangfeng
 * @create: 2023-09-15 14:00:08
 * @Description: 专题
 */
@Service
public interface TopicService extends IService<Topic> {

    /**
     * 查询所有专题Map
     *
     * @return
     */
    Map<Long, String> selectMap(Integer projectTypeId);


    /**
     * 列表查询
     *
     * @param req Topic带分页请求实体对象
     * @return
     */
    PageMaster<Topic> pagelist(TopicQueryIn req);


    /**
     * 获取单个Topic，有则获取，无则添加
     *
     * @param topicName
     * @return
     */
    Topic selectOne(String topicName, Integer projectTypeId) throws Exception;

}
