package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.ImageVisited;
import cn.staitech.anno.domain.RecentlyVisited;
import cn.staitech.anno.domain.vo.RecentlyVisitedVO.RecentlyVisitedSelectVO;
import cn.staitech.anno.mapper.RecentlyVisitedMapper;
import cn.staitech.anno.service.RecentlyVisitedService;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import cn.staitech.system.api.model.LoginUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cn.staitech.common.security.utils.SecurityUtils.getLoginUser;

/**
 * @author gjt.
 * @data 2023/5/25 14:08
 */
@Service
public class RecentlyVisitedServiceImpl implements RecentlyVisitedService {

    @Resource
    private RecentlyVisitedMapper recentlyVisitedMapper;


    @Override
    public List<RecentlyVisitedSelectVO> selectList() {

        RecentlyVisited recentlyVisited = new RecentlyVisited();
        recentlyVisited.setUserId(SecurityUtils.getUserId());
        // 根据用户id查询所有的该用户的所有访问
        // 查询出时间不为空的数据
        List<RecentlyVisited> recentlyVisitedList = recentlyVisitedMapper.selectUpdateIsTrue(recentlyVisited);

        List<RecentlyVisitedSelectVO> recentlyVisitedSelectVOS = new ArrayList<>();
        LoginUser loginUser = getLoginUser();
        // 判断用户为admin或者超级管理员
        if(SysUser.isAdmin(SecurityUtils.getUserId())){
            return recentlyVisitedSelectVOS;
        }
        for (RecentlyVisited recentlyVisited1 : recentlyVisitedList) {
            RecentlyVisitedSelectVO recentlyVisitedSelectVO = new RecentlyVisitedSelectVO();
            // 根据用户和切片进行倒排
            recentlyVisited.setProjectId(recentlyVisited1.getProjectId());
            List<ImageVisited> imageVisitedList = new ArrayList<>();

            List<RecentlyVisited> recentlyVisiteds = recentlyVisitedMapper.selectList(recentlyVisited);
            for (RecentlyVisited recentlyVisited2 : recentlyVisiteds) {
                ImageVisited imageVisited = new ImageVisited();
                imageVisited.setImageName(recentlyVisited2.getImageName());
                imageVisited.setThumbUrl(recentlyVisited2.getThumbUrl());
                imageVisited.setSlideId(recentlyVisited2.getSlideId());
                imageVisited.setCreateTime(recentlyVisited2.getCreateTime());
                imageVisitedList.add(imageVisited);
            }
            recentlyVisitedSelectVO.setRecentlyVisitedId(recentlyVisited1.getRecentlyVisitedId());
            recentlyVisitedSelectVO.setProjectName(recentlyVisited1.getProjectName());
            recentlyVisitedSelectVO.setProjectId(recentlyVisited1.getProjectId());
            recentlyVisitedSelectVO.setVisitTime(recentlyVisited1.getUpdateTime());
            recentlyVisitedSelectVO.setImageVisited(imageVisitedList);
            recentlyVisitedSelectVO.setUserId(recentlyVisited1.getUserId());
            recentlyVisitedSelectVOS.add(recentlyVisitedSelectVO);
        }
        return recentlyVisitedSelectVOS;

    }


    @Override
    public String selectBy(Long slideId) {
        // 根据切片查询项目、图片、切片信息
        RecentlyVisited req = recentlyVisitedMapper.selectBy(slideId);
//        long userId = SecurityUtils.getUserId();
        long userId = 2L;
        RecentlyVisited recentlyVisited = new RecentlyVisited();
        recentlyVisited.setUserId(userId);
        recentlyVisited.setProjectId(req.getProjectId());
        // 添加时间根据用户id项目id和专题id查询表中 ，如果结果数量小于3  直接添加
        List<RecentlyVisited> recentlyVisitedLIst = recentlyVisitedMapper.selectList(recentlyVisited);
        // 添加时将该专题下的更新时间清空
        recentlyVisited.setSlideId(slideId);
        recentlyVisitedMapper.updateTime(recentlyVisited);
        // 根据用户项目切片查询记录
        RecentlyVisited recentlyVisitedQuery = recentlyVisitedMapper.selectQueryBy(recentlyVisited);
        // 判断数据是否存在
        if (recentlyVisitedQuery != null) {
            // 如果存在,更新创建和更新时间即可
            recentlyVisitedMapper.update(recentlyVisitedQuery.getRecentlyVisitedId());
        } else {
            // 如果不存在
            if (recentlyVisitedLIst.size() > 3) {
                // 根据项目和用户为条件删除时间最早的一条数据
                recentlyVisitedMapper.deleteMinCreateTime(recentlyVisited);
            }
            // 添加数据
            req.setUserId(userId);
            recentlyVisitedMapper.insert(req);
        }
        return "ok";
    }

}
