package cn.staitech.anno.service;

import cn.staitech.anno.domain.RecentlyVisited;
import cn.staitech.anno.domain.vo.RecentlyVisitedVO.RecentlyVisitedSelectVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author gjt.
 * @data 2023/5/25 14:08
 */
public interface RecentlyVisitedService extends IService<RecentlyVisited> {


    /**
     * 查询用户的最近访问
     *
     * @param userId
     * @return List
     */
    List<RecentlyVisitedSelectVO> selectList();


    /**
     * 根据切片id查询专题、图片、切片信息
     *
     * @param slideId 切片id
     * @return list
     */
    void selectBy(Long slideId);
}
