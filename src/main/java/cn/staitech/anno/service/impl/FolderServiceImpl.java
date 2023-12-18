package cn.staitech.anno.service.impl;


import cn.staitech.anno.domain.Folder;
import cn.staitech.anno.mapper.FolderMapper;
import cn.staitech.anno.service.FolderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 业务层处理：AI预测-算法项目-眼科拼接Viewer-View图像
 *
 * @author wangfeng
 * @date 2023-11-10
 */
@Slf4j
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService {

    /**
     * 获取单个Folder，有则查询，无则添加
     *
     * @param folder
     * @return
     */
    public Folder selectOne(Folder folder) throws Exception {
        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getFolderUrl, folder.getFolderUrl());
        queryWrapper.orderByDesc(Folder::getFolderId);
        queryWrapper.last("limit 1");
        Folder destFolder = this.baseMapper.selectOne(queryWrapper);

        // 有则返回
        if (destFolder != null) {
            return destFolder;
        } else {
            // 无则添加
            try {
                folder.setCreateTime(new Date());
                folder.setDeleteFlag("1");
                this.baseMapper.insert(folder);
            } catch (DuplicateKeyException e) {
                log.info("添加文件夹-主键冲突 {}", e);
                return this.baseMapper.selectOne(queryWrapper);
            }
        }
        return folder;
    }
}
