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
     * 机构
     */
    public static Map<Long, String> ORGANIZATION_MAP;
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
    public static Map<String, String> PRODUCT_SERIES_MAP;
    public static Map<String, String> PRODUCT_SERIES_MAP_EN;
    public static Map<String, String> ORGAN_MAP;
    public static Map<String, String> ORGAN_MAP_EN;
    public static Map<String, String> STRUCTURE_MAP;
    public static Map<String, String> STRUCTURE_MAP_EN;

    @Resource
    private SysOrganizationService sysOrganizationService;
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
     * @param organizationId
     * @return
     */
    public static String getOrganizationName(Long organizationId) {
        if (ORGANIZATION_MAP.containsKey(organizationId)) {

            return ORGANIZATION_MAP.get(organizationId);
        }
        return "";
    }

    /**
     * 获取种属名称
     *
     * @param organizationIdAddSpeciesId
     * @return
     */
    public static String getSpeciesName(String organizationIdAddSpeciesId) {
        if (SPECIES_MAP.containsKey(organizationIdAddSpeciesId)) {

            return SPECIES_MAP.get(organizationIdAddSpeciesId);
        }
        return "";
    }

    /**
     * 获取种属名称 - EN
     *
     * @param organizationIdAddSpeciesId organizationId+speciesId
     * @return
     */
    public static String getSpeciesNameEn(String organizationIdAddSpeciesId) {
        if (SPECIES_MAP_EN.containsKey(organizationIdAddSpeciesId)) {

            return SPECIES_MAP_EN.get(organizationIdAddSpeciesId);
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
     * @param organizationIdAddSpeciesIdAddoductSeriesId OrganizationId + SpeciesId + ProductSeriesId
     * @return
     */
    public static String getProductSeries(String organizationIdAddSpeciesIdAddoductSeriesId) {
        if (PRODUCT_SERIES_MAP.containsKey(organizationIdAddSpeciesIdAddoductSeriesId)) {
            return PRODUCT_SERIES_MAP.get(organizationIdAddSpeciesIdAddoductSeriesId);
        }
        return "";
    }

    /**
     * 获取品系名称 - en
     *
     * @param organizationIdAddSpeciesIdAddoductSeriesId OrganizationId + SpeciesId + ProductSeriesId
     * @return
     */
    public static String getProductSeriesEn(String organizationIdAddSpeciesIdAddoductSeriesId) {
        if (PRODUCT_SERIES_MAP_EN.containsKey(organizationIdAddSpeciesIdAddoductSeriesId)) {
            return PRODUCT_SERIES_MAP_EN.get(organizationIdAddSpeciesIdAddoductSeriesId);
        }
        return "";
    }

    /**
     * 获取脏器名称
     *
     * @param organizationIdSpeciesCodeOrganId organizationId + speciesCode + organId
     * @return
     */
    public static String getOrgan(String organizationIdSpeciesCodeOrganId) {
        if (ORGAN_MAP.containsKey(organizationIdSpeciesCodeOrganId)) {
            return ORGAN_MAP.get(organizationIdSpeciesCodeOrganId);
        }
        return "";
    }

    /**
     * 获取脏器名称 - en
     *
     * @param organizationIdSpeciesCodeOrganId organizationId + speciesCode + organId
     * @return
     */
    public static String getOrganEn(String organizationIdSpeciesCodeOrganId) {
        if (ORGAN_MAP_EN.containsKey(organizationIdSpeciesCodeOrganId)) {
            return ORGAN_MAP_EN.get(organizationIdSpeciesCodeOrganId);
        }
        return "";
    }

    /**
     * 获取结构名称
     *
     * @param organizationIdSpeciesIdOrganIdStructureId OrganizationId + SpeciesId + OrganId + StructureId
     * @return
     */
    public static String getStructureName(String organizationIdSpeciesIdOrganIdStructureId) {
        if (STRUCTURE_MAP.containsKey(organizationIdSpeciesIdOrganIdStructureId)) {
            return STRUCTURE_MAP.get(organizationIdSpeciesIdOrganIdStructureId);
        }
        return "";
    }

    /**
     * 获取结构名称 - en
     *
     * @param organizationIdSpeciesIdOrganIdStructureId OrganizationId + SpeciesId + OrganId + StructureId
     * @return
     */
    public static String getStructureNameEn(String organizationIdSpeciesIdOrganIdStructureId) {
        if (STRUCTURE_MAP_EN.containsKey(organizationIdSpeciesIdOrganIdStructureId)) {
            return STRUCTURE_MAP_EN.get(organizationIdSpeciesIdOrganIdStructureId);
        }
        return "";
    }

    @PostConstruct
    public void init() {
        // 机构
        ORGANIZATION_MAP = sysOrganizationService.selectMap();

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

        // 清空RocksDB
        // deleteAllColumnFamily();
    }
}
