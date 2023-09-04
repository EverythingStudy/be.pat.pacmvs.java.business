package cn.staitech.anno.service;

import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.domain.topic.in.TopicQueryIn;
import cn.staitech.anno.utils.PageMaster;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;


/**
 * @author: wangfeng
 * @create: 2023-06-02 14:00:08
 * @Description: 切片（原图片）专题
 */
@Service
public interface TopicService extends IService<Topic> {

    /**
     * 列表查询
     *
     * @param req Topic带分页请求实体对象
     * @return
     */
    PageMaster<Topic> pagelist(TopicQueryIn req);
}
