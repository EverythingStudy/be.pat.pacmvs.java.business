package cn.staitech.anno.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.staitech.anno.domain.ProjectLabelStatistics;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectLabelOut;

/**
 * <p>
 * 标注统计-项目/标签 统计 Mapper 接口
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-24
 */
public interface ProjectLabelStatisticsMapper extends BaseMapper<ProjectLabelStatistics> {
	List<ProjectLabelOut> getProjectLabelStatistics(ProjectLabelStatistics projectLabelStatistics);

}
