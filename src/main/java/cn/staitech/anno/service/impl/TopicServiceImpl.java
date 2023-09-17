package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.domain.topic.TopicIdName;
import cn.staitech.anno.domain.topic.in.TopicQueryIn;
import cn.staitech.anno.mapper.TopicMapper;
import cn.staitech.anno.service.TopicService;
import cn.staitech.anno.utils.PageMaster;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author: wangfeng
 * @create: 2023-06-02 14:06:14
 * @Description: 切片（原图片）专题
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    private TopicMapper topicMapper;

    @Override
    public Map<Long, String> selectMap() {
        List<TopicIdName> list = topicMapper.selectIdNameList();
        Map<Long, String> map = list.stream()
                .collect(Collectors.toMap(TopicIdName::getTopicId, TopicIdName::getTopicName));
        return map;
    }

    /**
     * 列表查询
     *
     * @param req Topic带分页请求实体对象
     * @return
     */
    public PageMaster<Topic> pagelist(TopicQueryIn req) {
        Page<Topic> page = new Page<>(req.getPageNum(), req.getPageSize());
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 1).orderByDesc("topic_id");
        if (req.getTopicName() != null && req.getTopicName() != "" && req.getTopicName() != "null") {
            queryWrapper.like("topic_name", req.getTopicName());
        }

        this.baseMapper.selectPage(page, queryWrapper);
        List<Topic> list = page.getRecords();

        //构建分页对象
        PageMaster<Topic> pageMaster = PageMaster.of(list);
        pageMaster.setTotal(page.getTotal());

        return pageMaster;
    }
}
