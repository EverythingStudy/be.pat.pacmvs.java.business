package cn.staitech.anno.service;

import cn.staitech.anno.vo.pathologicaltissue.PathologicalTissueVO;

import java.util.List;

public interface PathologicalTissueService {

    /**
     * 查询项目类型下的病理组织和算法模型
     */
    List<PathologicalTissueVO> selectByPrimaryKey(Long projectTypeId);
}
