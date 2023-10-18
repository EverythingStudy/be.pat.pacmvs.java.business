package cn.staitech.anno.config;

import cn.staitech.anno.service.GroupService;
import cn.staitech.anno.service.ProjectTypeService;
import cn.staitech.anno.service.RoundService;
import cn.staitech.anno.service.SpeciesService;
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
    @Resource
    private SpeciesService speciesService;

    @Resource
    private GroupService groupService;

    @Resource
    private RoundService roundService;
    @Resource
    private ProjectTypeService projectTypeService;

    /**
     * 种属
     */
    public static Map<Long, String> SPECIES_MAP;
    public static Map<Long, String> SPECIES_MAP_EN;
    public static Map<Long, String> GROUP_MAP;
    public static Map<Long, String> ROUND_MAP;
    public static Map<Long, String> ROUND_MAP_EN;
    public static Map<String, String> PROJECT_TYPE_MAP;
    public static Map<String, String> PROJECT_TYPE_MAP_EN;

    @PostConstruct
    public void init() {
        SPECIES_MAP = speciesService.selectMap();
        GROUP_MAP = groupService.selectMap();
        ROUND_MAP = roundService.selectMap();

        SPECIES_MAP_EN = speciesService.selectMapEn();
        ROUND_MAP_EN = roundService.selectMapEn();

        PROJECT_TYPE_MAP = projectTypeService.selectMap();
        PROJECT_TYPE_MAP_EN = projectTypeService.selectMap();
    }

    /**
     * 获取种属名称
     *
     * @param topicId
     * @return
     */
    public static String getSpeciesName(Long topicId) {
        if (SPECIES_MAP.containsKey(topicId)) {

            return SPECIES_MAP.get(topicId);
        }
        return "";
    }

    /**
     * 获取种属名称 - EN
     *
     * @param topicId
     * @return
     */
    public static String getSpeciesNameEn(Long topicId) {
        if (SPECIES_MAP_EN.containsKey(topicId)) {

            return SPECIES_MAP_EN.get(topicId);
        }
        return "";
    }

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

    /**
     * 获取轮次名称 - en
     *
     * @param RoundId
     * @return
     */
    public static String getRoundNameEn(Long RoundId) {
        if (ROUND_MAP_EN.containsKey(RoundId)) {
            return ROUND_MAP_EN.get(RoundId);
        }
        return "";
    }

    /**
     * 获取轮次名称
     *
     * @param getProjectTypeId
     * @return
     */
    public static String getProjectType(String getProjectTypeId) {
        if (PROJECT_TYPE_MAP.containsKey(getProjectTypeId)) {
            return PROJECT_TYPE_MAP.get(getProjectTypeId);
        }
        return "";
    }

    /**
     * 获取轮次名称 - en
     *
     * @param getProjectTypeId
     * @return
     */
    public static String getProjectTypeEn(String getProjectTypeId) {
        if (PROJECT_TYPE_MAP_EN.containsKey(getProjectTypeId)) {
            return PROJECT_TYPE_MAP_EN.get(getProjectTypeId);
        }
        return "";
    }
}
