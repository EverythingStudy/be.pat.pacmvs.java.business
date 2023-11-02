package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.AiprePathologicalTissue;

public interface AiprePathologicalTissueMapper {
    int deleteByPrimaryKey(Long tissueId);

    int insert(AiprePathologicalTissue record);

    int insertSelective(AiprePathologicalTissue record);

    AiprePathologicalTissue selectByPrimaryKey(Long tissueId);

    int updateByPrimaryKeySelective(AiprePathologicalTissue record);

    int updateByPrimaryKey(AiprePathologicalTissue record);
}