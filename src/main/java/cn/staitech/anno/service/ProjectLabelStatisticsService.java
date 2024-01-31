package cn.staitech.anno.service;

import cn.staitech.anno.domain.ProjectLabelStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 标注统计-项目/标签 统计 服务类
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-24
 */
public interface ProjectLabelStatisticsService extends IService<ProjectLabelStatistics> {

	public void generateData();
}
