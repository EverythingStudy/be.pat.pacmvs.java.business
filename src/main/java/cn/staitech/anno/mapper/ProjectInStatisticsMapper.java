package cn.staitech.anno.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.staitech.anno.domain.ProjectStatistics;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectLabelOut;

/**
 * <p>
 * 标注统计-项目统计 Mapper 接口
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-26
 */
public interface ProjectInStatisticsMapper extends BaseMapper<ProjectStatistics> {
	List<ProjectLabelOut> getProjectStatistics(ProjectStatistics projectStatistics);
}
