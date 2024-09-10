package cn.staitech.anno.mapper;


import cn.staitech.anno.vo.organization.OrganizationIdName;
import cn.staitech.anno.vo.organization.SysConfigOut;

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