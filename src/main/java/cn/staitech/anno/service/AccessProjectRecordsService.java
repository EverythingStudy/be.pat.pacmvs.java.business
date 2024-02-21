package cn.staitech.anno.service;

import cn.staitech.anno.vo.accessprojectrecords.AccessProjectRecordsOut;

import java.util.List;

/**
 * AccessProjectRecordsService接口
 *
 * @author zmj
 * @date 2024-02-20
 */
public interface AccessProjectRecordsService {

    /**
     * 查询
     * */
     List<AccessProjectRecordsOut> accessRecords();

}
