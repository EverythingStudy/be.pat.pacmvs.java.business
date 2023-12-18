package cn.staitech.anno.service;


import cn.staitech.anno.domain.Folder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * FolderService接口
 *
 * @author wangfeng
 * @date 2023-12-18
 */
public interface FolderService extends IService<Folder> {
    /**
     * 获取单个Folder，有则查询，无则添加
     *
     * @param folder
     * @return
     */
    Folder selectOne(Folder folder) throws Exception;
}
