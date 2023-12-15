package cn.staitech.anno.mapper;


import cn.staitech.anno.domain.ImageVisited;

import java.util.List;

/**
 * 最近访问表 数据层 .
 *
 * @author staitech
 */
public interface ImageVisitedMapper {

    /**
     * 查询最近访问下的图像信息
     *
     * @param recentlyVisitedId 机构ID
     * @return list
     */
    List<ImageVisited> selectList(Long recentlyVisitedId);


}