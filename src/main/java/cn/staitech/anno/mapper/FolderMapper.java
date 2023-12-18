package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Folder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface FolderMapper extends BaseMapper<Folder> {
    int deleteByPrimaryKey(Long folderId);

    int insert(Folder record);

    int insertSelective(Folder record);

    Folder selectByPrimaryKey(Long folderId);

    int updateByPrimaryKeySelective(Folder record);

    int updateByPrimaryKey(Folder record);
}