package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.RecentlyVisited;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 最近访问表 数据层 .
 *
 * @author staitech
 */
public interface RecentlyVisitedMapper extends BaseMapper<RecentlyVisited> {

    /**
     * 查询用户的最近访问
     *
     * @param userId
     * @return List
     */
    List<RecentlyVisited> selectLists(RecentlyVisited recentlyVisited);

    /**
     * 查询更新时间不为空
     *
     * @param userId
     * @return List
     */
    List<RecentlyVisited> selectUpdateIsTrue(RecentlyVisited recentlyVisited);

    /**
     * 根据条件查询
     *
     * @param userId
     * @return List
     */
    RecentlyVisited selectQueryBy(RecentlyVisited recentlyVisited);


    /**
     * 根据切片id查询专题、图片、切片信息
     *
     * @param slideId 切片id
     * @return list
     */
    RecentlyVisited selectBy(Long slideId);

    /**
     * 添加最近访问
     *
     * @param slideId 切片id
     * @return list
     */
    int insert(RecentlyVisited recentlyVisited);

    /**
     * 更新创建时间和更新时间
     *
     * @param slideId 切片id
     * @return list
     */
    int update(Long recentlyVisitedId);

    /**
     * 删除最小时间
     *
     * @param slideId 切片id
     * @return list
     */
    int deleteMinCreateTime(RecentlyVisited recentlyVisited);

    /**
     * 根据专题和userId将更新时间
     *
     * @param slideId 切片id
     * @return list
     */
    int updateTime(RecentlyVisited recentlyVisited);

    List<RecentlyVisited> selectSpecial(RecentlyVisited recentlyVisited);

    /**
     * 根据专题id和用户id删除信息
     *
     * @param slideId 切片id
     * @return list
     */
    int deleteSpecial(RecentlyVisited recentlyVisited);

    /**
     * 查询医学切片信息
     * */
    RecentlyVisited selectSlideInfo(Long slideId);

}