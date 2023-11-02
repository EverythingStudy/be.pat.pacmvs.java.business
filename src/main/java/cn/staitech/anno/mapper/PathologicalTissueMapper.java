package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.PathologicalTissue;

public interface PathologicalTissueMapper {
    int deleteByPrimaryKey(Long tissueId);

    int insert(PathologicalTissue record);

    int insertSelective(PathologicalTissue record);

    PathologicalTissue selectByPrimaryKey(Long tissueId);

    int updateByPrimaryKeySelective(PathologicalTissue record);

    int updateByPrimaryKey(PathologicalTissue record);
}