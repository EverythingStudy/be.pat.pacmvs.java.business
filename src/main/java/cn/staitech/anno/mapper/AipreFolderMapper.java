package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.AipreFolder;

public interface AipreFolderMapper {
    int deleteByPrimaryKey(Long folderId);

    int insert(AipreFolder record);

    int insertSelective(AipreFolder record);

    AipreFolder selectByPrimaryKey(Long folderId);

    int updateByPrimaryKeySelective(AipreFolder record);

    int updateByPrimaryKey(AipreFolder record);
}