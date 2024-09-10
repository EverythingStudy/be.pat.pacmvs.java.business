package cn.staitech.anno.service;


import cn.staitech.anno.vo.organization.SysConfigOut;

import java.util.Map;

/**
 * @author staitech
 */

public interface SysOrganizationService {


    /**
     * 查询所有机构Map
     *
     * @return
     */
    Map<Long, String> selectMap();

}