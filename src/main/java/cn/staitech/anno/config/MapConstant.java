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

    @Resource
    private SysOrganizationService sysOrganizationService;

    public static String getOrganizationName(Long organizationId) {
        if (ORGANIZATION_MAP.containsKey(organizationId)) {

            return ORGANIZATION_MAP.get(organizationId);
        }
        return "";
    }

    @PostConstruct
    public void init() {
        // 机构
        ORGANIZATION_MAP = sysOrganizationService.selectMap();
    }
}
