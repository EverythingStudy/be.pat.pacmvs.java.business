package cn.staitech.anno.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.staitech.anno.domain.ProjectStatistics;
import cn.staitech.anno.mapper.ProjectInStatisticsMapper;
import cn.staitech.anno.service.ProjectInStatisticsService;

/**
 * <p>
 * 标注统计-项目统计 服务实现类
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-26
 */
@Service
public class ProjectInStatisticsServiceImpl extends ServiceImpl<ProjectInStatisticsMapper, ProjectStatistics> implements ProjectInStatisticsService {

}
