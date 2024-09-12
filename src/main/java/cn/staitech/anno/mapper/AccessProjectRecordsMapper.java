package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.accessprojectrecords.AccessProjectRecordsIn;
import cn.staitech.anno.vo.accessprojectrecords.AccessProjectRecordsOut;

import java.util.List;

public interface AccessProjectRecordsMapper {
    /**
     * 查询
     * */

    List<AccessProjectRecordsOut> accessRecords(AccessProjectRecordsIn accessProjectRecordsIn);

    /**
     * 删除一个月前的数据
     * */
    int delAccessRecords(AccessProjectRecordsIn accessProjectRecordsIn);



}