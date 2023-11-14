package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Folder;

public interface FolderMapper {
    int deleteByPrimaryKey(Long folderId);

    int insert(Folder record);

    int insertSelective(Folder record);

    Folder selectByPrimaryKey(Long folderId);

    int updateByPrimaryKeySelective(Folder record);

    int updateByPrimaryKey(Folder record);
}