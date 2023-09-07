package cn.staitech.anno.mapper;


import cn.staitech.anno.domain.organization.OrganizationIdName;

import java.util.List;

/**
 * @author gjt
 */
public interface SysOrganizationMapper {

    /**
     * 查询组织表
     *
     * @return
     */
    List<OrganizationIdName> selectIdNameList();
}