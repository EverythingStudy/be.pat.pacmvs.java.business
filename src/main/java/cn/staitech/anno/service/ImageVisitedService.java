package cn.staitech.anno.service;

import cn.staitech.anno.domain.ImageVisited;

import java.util.List;

/**
 * @author gjt.
 * @data 2023/5/25 16:15
 */
public interface ImageVisitedService {

    /**
     * 查询最近访问下的图像信息
     *
     * @param recentlyVisitedId 机构id
     * @return list
     */
    List<ImageVisited> selectList(Long recentlyVisitedId);
}
