package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.notice.out.NoticeQueryOut;
import cn.staitech.anno.domain.project.out.SystemDictOut;

import java.util.List;

public interface SystemDictMapper {
    List<SystemDictOut> selectSystemDict();

    List<SystemDictOut> selectFirst(Long parentId);

    /**
     *
     * @param reclaimBy
     * @return 消息列表
     */
    List<NoticeQueryOut> selectList(Long reclaimBy);


}
