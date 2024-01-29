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


    /**
     * ai拼接
     * */
    SysConfigOut aiMontage(String key);

    /**
     * 更新ai拼接
     * */
    int updateAiMontage(SysConfigOut sysConfigOut);
}