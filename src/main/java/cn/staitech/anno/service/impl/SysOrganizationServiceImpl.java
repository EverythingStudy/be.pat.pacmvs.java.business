package cn.staitech.anno.service.impl;


import cn.staitech.anno.domain.organization.OrganizationIdName;
import cn.staitech.anno.mapper.SysOrganizationMapper;
import cn.staitech.anno.service.SysOrganizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 查询组织表Map .
 *
 * @author staitech
 */
@Service
public class SysOrganizationServiceImpl implements SysOrganizationService {

    @Resource
    private SysOrganizationMapper organizationMapper;

    @Override
    public Map<Long, String> selectMap() {
        List<OrganizationIdName> list = organizationMapper.selectIdNameList();
        Map<Long, String> map = list.stream()
                .collect(Collectors.toMap(OrganizationIdName::getOrganizationId, OrganizationIdName::getOrganizationName));
        return map;
    }
}
