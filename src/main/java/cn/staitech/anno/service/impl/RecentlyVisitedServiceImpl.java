package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ImageVisited;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.domain.RecentlyVisited;
import cn.staitech.anno.mapper.ProjectMemberMapper;
import cn.staitech.anno.mapper.RecentlyVisitedMapper;
import cn.staitech.anno.service.RecentlyVisitedService;
import cn.staitech.anno.vo.recentlyvisited.RecentlyVisitedSelectVO;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author gjt.
 * @data 2023/5/25 14:08
 */
@Service
public class RecentlyVisitedServiceImpl extends ServiceImpl<RecentlyVisitedMapper, RecentlyVisited> implements RecentlyVisitedService {

    @Resource
    private RecentlyVisitedMapper recentlyVisitedMapper;

    @Resource
    private ProjectMemberMapper projectMemberMapper;


    @Override
    public List<RecentlyVisitedSelectVO> selectList(Long projectType) {

        RecentlyVisited recentlyVisited = new RecentlyVisited();
        recentlyVisited.setUserId(SecurityUtils.getUserId());
        recentlyVisited.setProjectType(projectType);
        // 根据用户id查询所有的该用户的所有访问
        // 查询出时间不为空的数据
        List<RecentlyVisited> recentlyVisitedList = recentlyVisitedMapper.selectUpdateIsTrue(recentlyVisited);
        List<RecentlyVisitedSelectVO> recentlyVisitedSelectVOS = new ArrayList<>();
        // 判断用户为admin或者超级管理员
        if (SysUser.isAdmin(SecurityUtils.getUserId())) {
            return recentlyVisitedSelectVOS;
        }
        for (RecentlyVisited recentlyVisited1 : recentlyVisitedList) {
            RecentlyVisitedSelectVO recentlyVisitedSelectVO = new RecentlyVisitedSelectVO();
            // 根据用户和切片进行倒排
            recentlyVisited.setProjectId(recentlyVisited1.getProjectId());
            List<ImageVisited> imageVisitedList = new ArrayList<>();

            List<RecentlyVisited> recentlyVisiteds = recentlyVisitedMapper.selectLists(recentlyVisited);
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
            recentlyVisitedSelectVO.setReviewRoundId(recentlyVisited1.getReviewRoundId());
            recentlyVisitedSelectVO.setImageVisited(imageVisitedList);
            recentlyVisitedSelectVO.setProjectType(recentlyVisited1.getProjectType());
            recentlyVisitedSelectVO.setUserId(recentlyVisited1.getUserId());
            recentlyVisitedSelectVOS.add(recentlyVisitedSelectVO);
        }
        return recentlyVisitedSelectVOS;

    }


    @Override
    public void selectBy(Long slideId) {
        // 根据切片查询项目、图片、切片信息
        RecentlyVisited req = recentlyVisitedMapper.selectBy(slideId);
        // 查询当前用户是否在当前项目中
        ProjectMember projectMember = new ProjectMember();
        projectMember.setUserId(SecurityUtils.getUserId());
        projectMember.setProjectId(req.getProjectId());
        ProjectMember projectMemberBy = projectMemberMapper.selectUserBy(projectMember);
        // 用户在当前项目中，添加访问记录
        if (projectMemberBy != null) {
            long userId = SecurityUtils.getUserId();
            RecentlyVisited recentlyVisited = new RecentlyVisited();
            recentlyVisited.setUserId(userId);
            recentlyVisited.setProjectId(req.getProjectId());
            // 添加时间根据用户id项目id和专题id查询表中 ，如果结果数量小于4  直接添加
            List<RecentlyVisited> recentlyVisitedLIst = recentlyVisitedMapper.selectLists(recentlyVisited);
            // 添加时将该专题下的更新时间清空
            recentlyVisited.setSlideId(slideId);
            recentlyVisitedMapper.updateTime(recentlyVisited);
            // 根据用户项目切片查询记录,查询当前切片是否存在
            RecentlyVisited recentlyVisitedQuery = recentlyVisitedMapper.selectQueryBy(recentlyVisited);
            // 判断数据是否存在
            if (recentlyVisitedQuery != null) {
                // 如果存在,更新创建和更新时间即可
                recentlyVisitedMapper.update(recentlyVisitedQuery.getRecentlyVisitedId());
            } else {
                // 如果不存在，并且数据大于四条，根据项目和用户为条件删除时间最早的一条数据
                if (recentlyVisitedLIst.size() > 4) {
                    recentlyVisitedMapper.deleteMinCreateTime(recentlyVisited);
                }
                // 添加数据
                req.setUserId(userId);
                recentlyVisitedMapper.insert(req);
            }
            // 添加之后根据用户和项目查询 如果大于十条，根据用户和项目删除数据
            recentlyVisited.setProjectType(req.getProjectType());
            List<RecentlyVisited> recentlyVisitedList = recentlyVisitedMapper.selectSpecial(recentlyVisited);
            if (recentlyVisitedList.size() > 10) {
                // 找出时间最小的一条数据
                RecentlyVisited recentlyVisited1 = recentlyVisitedList.stream().min(Comparator.comparing(RecentlyVisited::getUpdateTime)).get();
                recentlyVisited.setProjectId(recentlyVisited1.getProjectId());
                recentlyVisited.setUserId(userId);
                recentlyVisitedMapper.deleteMinCreateTime(recentlyVisited);
            }
        }
    }

}
