package cn.staitech.anno.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.staitech.anno.domain.ProjectAnnoStatistics;
import cn.staitech.anno.project.vo.ImageAnnoStatisticsVO;

/**
 * <p>
 * 标注统计-图像标注统计 Mapper 接口
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-24
 */
public interface ProjectAnnoStatisticsMapper extends BaseMapper<ProjectAnnoStatistics> {
	List<ImageAnnoStatisticsVO> getProjectAnnoStatistics(ProjectAnnoStatistics projectAnnoStatistics);

}
