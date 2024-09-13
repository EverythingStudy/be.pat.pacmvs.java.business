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
    public static Map<String, String> SPECIES_MAP;
    public static Map<String, String> SPECIES_MAP_EN;
    public static Map<String, String> ORGAN_MAP;
    public static Map<String, String> ORGAN_MAP_EN;
    public static Map<String, String> STRUCTURE_MAP;
    public static Map<String, String> STRUCTURE_MAP_EN;

    @Resource
    private SysOrganizationService sysOrganizationService;
    
    @Resource
    private SpeciesService speciesService;
    @Resource
    private OrganService organService;
    @Resource
    private StructureService structureService;

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
        
     // 种属
        SPECIES_MAP = speciesService.selectMap();
        SPECIES_MAP_EN = speciesService.selectMapEn();


        // 脏器
        ORGAN_MAP = organService.selectMap();
        ORGAN_MAP_EN = organService.selectMapEn();

        // 结构
        STRUCTURE_MAP = structureService.selectMap();
        STRUCTURE_MAP_EN = structureService.selectMapEn();
    }
}
