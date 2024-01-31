package cn.staitech.anno.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.staitech.anno.domain.MarkingStatistic;
import cn.staitech.anno.domain.ProjectUserLabelStatistics;

/**
 * <p>
 * 标注统计-用户标签统计 Mapper 接口
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-24
 */
public interface ProjectUserLabelStatisticsMapper extends BaseMapper<ProjectUserLabelStatistics> {
	List<MarkingStatistic> getProjectUserLabelStatistics(ProjectUserLabelStatistics projectUserLabelStatistics);

}
