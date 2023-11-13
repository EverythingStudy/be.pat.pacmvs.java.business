package cn.staitech.anno.config;

import cn.staitech.anno.service.*;
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
    /**
     * 种属
     */
    public static Map<String, String> SPECIES_MAP;
    public static Map<String, String> SPECIES_MAP_EN;
    public static Map<Long, String> GROUP_MAP;
    public static Map<Long, String> ROUND_MAP;
    public static Map<Long, String> ROUND_MAP_EN;
    public static Map<String, String> PROJECT_TYPE_MAP;
    public static Map<String, String> PROJECT_TYPE_MAP_EN;
    public static Map<Integer, String> PRODUCT_SERIES_MAP;
    public static Map<Integer, String> PRODUCT_SERIES_MAP_EN;
    public static Map<String, String> ORGAN_MAP;
    public static Map<String, String> ORGAN_MAP_EN;
    public static Map<String, String> STRUCTURE_MAP;
    public static Map<String, String> STRUCTURE_MAP_EN;
    @Resource
    private SpeciesService speciesService;
    @Resource
    private GroupService groupService;
    @Resource
    private RoundService roundService;
    @Resource
    private ProjectTypeService projectTypeService;
    @Resource
    private ProductSeriesService productSeriesService;
    @Resource
    private OrganService organService;
    @Resource
    private StructureService structureService;

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
     * 获取分组名称 - 不区分中英文
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
     * @param roundId
     * @return
     */
    public static String getRoundName(Long roundId) {
        if (ROUND_MAP.containsKey(roundId)) {
            return ROUND_MAP.get(roundId);
        }
        return "";
    }

    /**
     * 获取轮次名称 - en
     *
     * @param roundId
     * @return
     */
    public static String getRoundNameEn(Long roundId) {
        if (ROUND_MAP_EN.containsKey(roundId)) {
            return ROUND_MAP_EN.get(roundId);
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

    /**
     * 获取品系名称
     *
     * @param productSeriesId
     * @return
     */
    public static String getProductSeries(Integer productSeriesId) {
        if (PRODUCT_SERIES_MAP.containsKey(productSeriesId)) {
            return PRODUCT_SERIES_MAP.get(productSeriesId);
        }
        return "";
    }

    /**
     * 获取品系名称 - en
     *
     * @param productSeriesId
     * @return
     */
    public static String getProductSeriesEn(Integer productSeriesId) {
        if (PRODUCT_SERIES_MAP_EN.containsKey(productSeriesId)) {
            return PRODUCT_SERIES_MAP_EN.get(productSeriesId);
        }
        return "";
    }

    /**
     * 获取脏器名称
     *
     * @param speciesIdConnectOrganId
     * @return
     */
    public static String getOrgan(String speciesIdConnectOrganId) {
        if (ORGAN_MAP.containsKey(speciesIdConnectOrganId)) {
            return ORGAN_MAP.get(speciesIdConnectOrganId);
        }
        return "";
    }

    /**
     * 获取脏器名称 - en
     *
     * @param speciesIdConnectOrganId
     * @return
     */
    public static String getOrganEn(String speciesIdConnectOrganId) {
        if (ORGAN_MAP_EN.containsKey(speciesIdConnectOrganId)) {
            return ORGAN_MAP_EN.get(speciesIdConnectOrganId);
        }
        return "";
    }

    /**
     * 获取结构名称
     *
     * @param structureId
     * @return
     */
    public static String getStructureName(String structureId) {
        if (STRUCTURE_MAP.containsKey(structureId)) {
            return STRUCTURE_MAP.get(structureId);
        }
        return "";
    }

    /**
     * 获取结构名称 - en
     *
     * @param structureId
     * @return
     */
    public static String getStructureNameEn(String structureId) {
        if (STRUCTURE_MAP_EN.containsKey(structureId)) {
            return STRUCTURE_MAP_EN.get(structureId);
        }
        return "";
    }

    @PostConstruct
    public void init() {
        // 分组
        GROUP_MAP = groupService.selectMap();

        // 种属
        SPECIES_MAP = speciesService.selectMap();
        SPECIES_MAP_EN = speciesService.selectMapEn();

        // 轮次
        ROUND_MAP = roundService.selectMap();
        ROUND_MAP_EN = roundService.selectMapEn();

        // 项目类型
        PROJECT_TYPE_MAP = projectTypeService.selectMap();
        PROJECT_TYPE_MAP_EN = projectTypeService.selectMapEn();

        // 品系
        PRODUCT_SERIES_MAP = productSeriesService.selectMap();
        PRODUCT_SERIES_MAP_EN = productSeriesService.selectMapEn();

        // 脏器
        ORGAN_MAP = organService.selectMap();
        ORGAN_MAP_EN = organService.selectMapEn();

        // 结构
        STRUCTURE_MAP = structureService.selectMap();
        STRUCTURE_MAP_EN = structureService.selectMapEn();
    }

}
