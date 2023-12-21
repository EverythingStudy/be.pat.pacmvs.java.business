package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.pathologicaltissue.PathologicalTissueVO;

import java.util.List;

public interface PathologicalTissueMapper {

    /**
     * 查询项目类型下的病理组织
     */
    List<PathologicalTissueVO> selectByPrimaryKey(Long projectTypeId);


}