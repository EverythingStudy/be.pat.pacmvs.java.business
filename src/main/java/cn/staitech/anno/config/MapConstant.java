package cn.staitech.anno.config;

import cn.staitech.anno.service.GroupService;
import cn.staitech.anno.service.RoundService;
import cn.staitech.anno.service.TopicService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: wangfeng
 * @create: 2023-09-19 14:38:24
 * @Description: 下拉列表
 */
@Component
public class MapConstant {
/*    @Resource
    private TopicService topicService;*/

    @Resource
    private GroupService groupService;

    @Resource
    private RoundService roundService;


    /**
     * 专题列表
     */
    // public static Map<Long, String> TOPIC_MAP;
    public static Map<Long, String> GROUP_MAP;
    public static Map<Long, String> ROUND_MAP;

    @PostConstruct
    public void init() {
        //TOPIC_MAP = topicService.selectMap();
        GROUP_MAP = groupService.selectMap();
        ROUND_MAP = roundService.selectMap();
    }

    /**
     * 获取专题名称
     *
     * @param topicId
     * @return
     */
/*    public static String getTopicName(Long topicId) {
        if (TOPIC_MAP.containsKey(topicId)) {

            return TOPIC_MAP.get(topicId);
        }
        return "";
    }*/


    /**
     * 获取分组名称
     *
     * @param groupId
     * @return
     */
    public static String getGroupName(Long groupId) {
        if (GROUP_MAP.containsKey(groupId)) {

            return GROUP_MAP.get(groupId);
        }
        return "";
    }

    /**
     * 获取轮次名称
     *
     * @param RoundId
     * @return
     */
    public static String getRoundName(Long RoundId) {
        if (ROUND_MAP.containsKey(RoundId)) {

            return ROUND_MAP.get(RoundId);
        }
        return "";
    }
}
