package cn.staitech.anno.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.MarkingStatistic;
import cn.staitech.anno.domain.ProjectMember;
import cn.staitech.anno.domain.ProjectUserLabelStatistics;
import cn.staitech.anno.mapper.MarkingMapper;
import cn.staitech.anno.mapper.ProjectUserLabelStatisticsMapper;
import cn.staitech.anno.project.vo.SelectProjectVO;
import cn.staitech.anno.service.MarkingStatisticService;
import cn.staitech.anno.service.ProjectMemberService;
import cn.staitech.anno.service.ProjectUserLabelStatisticsService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.anno.utils.ExcelUtil;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.marking.Marking;
import cn.staitech.anno.vo.marking.MarkingStatisticSelectVO;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import lombok.extern.slf4j.Slf4j;

/**
 * 标注 - 标签统计
 *
 * @author wangfeng
 */
@Service
@Slf4j
public class MarkingStatisticServiceImpl implements MarkingStatisticService {
	@Resource
	private MarkingMapper markingMapper;
	@Resource
	private ProjectUserLabelStatisticsService projectUserLabelStatisticsService;
	@Resource
	private ProjectMemberService projectMemberService;
	@Resource
	private ProjectUserLabelStatisticsMapper projectUserLabelStatisticsMapper;

	/**
	 * 标签统计
	 *
	 * @param selectVO
	 * @return
	 */
	@Override
	public PageMaster<MarkingStatistic> selectMarkingStatistic(MarkingStatisticSelectVO selectVO) {

		SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
		Long organizationId = sysUser.getOrganizationId();
		Long currentUserId = sysUser.getUserId();

		List<Long> projectIds = selectVO.getProjectIds();
		List<Long> createBys = selectVO.getCreateBys();
		List<Long> indicatorIds = selectVO.getIndicatorIds();
		List<Long> categoryIds = selectVO.getCategoryIds();

		if (null == projectIds || CollectionUtils.isEmpty(projectIds)) {
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}

		ProjectUserLabelStatistics projectUserLabelStatistics = new ProjectUserLabelStatistics();
		if (CollectionUtils.isNotEmpty(projectIds)) {
			projectUserLabelStatistics.setProjectIdList(projectIds);
		}
		if (CollectionUtils.isNotEmpty(createBys)) {
			projectUserLabelStatistics.setProjectAnnoUseridList(createBys);
		}
		if (CollectionUtils.isNotEmpty(indicatorIds)) {
			projectUserLabelStatistics.setIndicatorIdList(indicatorIds);
		}
		if (CollectionUtils.isNotEmpty(categoryIds)) {
			projectUserLabelStatistics.setCategoryIdList(categoryIds);
		}
		projectUserLabelStatistics.setDelFlag("0");
		projectUserLabelStatistics.setOrganizationId(organizationId);

		PageHelper.startPage(selectVO.getPageNum(), selectVO.getPageSize()).setReasonable(true);
		List<MarkingStatistic> queryList = projectUserLabelStatisticsMapper
				.getProjectUserLabelStatistics(projectUserLabelStatistics);

		PageMaster<MarkingStatistic> pageMaster = new PageMaster<>(queryList);
		// 清除分页缓存
		// PageHelper.clearPage();
		return pageMaster;
	}

	/**
	 * 标签统计 - 导出execl
	 *
	 * @param selectVO
	 * @param response
	 * @throws Exception
	 */
	@Override
	public void execlExport(MarkingStatisticSelectVO selectVO, HttpServletResponse response) throws Exception {
		SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
		Long organizationId = sysUser.getOrganizationId();
		Long currentUserId = sysUser.getUserId();

		/*
		 * // 获取总记录数 long total =
		 * markingMapper.selectMarkingStatisticTotal(selectVO); if (total == 0)
		 * { return; }
		 * 
		 * // 分页->线程池异步处理 ExecutorService executorService = new
		 * ThreadPoolExecutor( Runtime.getRuntime().availableProcessors(),
		 * Runtime.getRuntime().availableProcessors() * 2, // 空闲线程等待工作的超时时间 0,
		 * TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(4096), new
		 * ThreadFactory() { public Thread newThread(Runnable r) { return new
		 * Thread(r, "标签统计 - 导出execl - " + r.hashCode()); } }, new
		 * ThreadPoolExecutor.DiscardOldestPolicy());
		 * 
		 * int pageSize = 1000; int totalPages = (int) (total / pageSize);
		 * 
		 * List<MarkingStatistic> list = new ArrayList<>((int) total);
		 * 
		 * // 遍历所有页码 for (int i = 0; i <= totalPages; i++) { final int pageNum =
		 * i + 1; executorService.execute(() -> { try { // 获取指定页码的数据
		 * Page<cn.staitech.anno.project.domain.Marking> page =
		 * PageHelper.startPage(pageNum, pageSize); List<MarkingStatistic>
		 * currentPageRecords = markingMapper.selectMarkingStatistic(selectVO);
		 * 
		 * synchronized (this) { // 将数据添加到最终结果集中（这里list为共享变量） if
		 * (currentPageRecords.size() > 0) { currentPageRecords =
		 * setCount(currentPageRecords, organizationId);
		 * list.addAll(currentPageRecords); } } } catch (Exception e) {
		 * log.error("Error occurred while fetching data from page {}", e); }
		 * }); }
		 * 
		 * // 关闭线程池 executorService.shutdown(); // 等待所有任务完成
		 * executorService.awaitTermination(Long.MAX_VALUE,
		 * TimeUnit.NANOSECONDS);
		 */

		List<Long> projectIds = selectVO.getProjectIds();
		List<Long> createBys = selectVO.getCreateBys();
		List<Long> indicatorIds = selectVO.getIndicatorIds();
		List<Long> categoryIds = selectVO.getCategoryIds();

		if (null == projectIds || CollectionUtils.isEmpty(projectIds)) {
			projectIds = getProjectIdStatistics(currentUserId, organizationId);
		}

		ProjectUserLabelStatistics projectUserLabelStatistics = new ProjectUserLabelStatistics();
		if (CollectionUtils.isNotEmpty(projectIds)) {
			projectUserLabelStatistics.setProjectIdList(projectIds);
		}
		if (CollectionUtils.isNotEmpty(createBys)) {
			projectUserLabelStatistics.setProjectAnnoUseridList(createBys);
		}
		if (CollectionUtils.isNotEmpty(indicatorIds)) {
			projectUserLabelStatistics.setIndicatorIdList(indicatorIds);
		}
		if (CollectionUtils.isNotEmpty(categoryIds)) {
			projectUserLabelStatistics.setCategoryIdList(categoryIds);
		}
		projectUserLabelStatistics.setDelFlag("0");
		projectUserLabelStatistics.setOrganizationId(organizationId);

		List<MarkingStatistic> queryList = projectUserLabelStatisticsMapper
				.getProjectUserLabelStatistics(projectUserLabelStatistics);

		// 构造表头的每个列头 定义表头
		List<Map<String, String>> titleList = ExcelUtil.getTitleList(CommonConstant.MARKING_STATISTICS_KEY,
				CommonConstant.MARKING_STATISTICS_VALUE);
		// 生成excel文件
		ExcelTool excelTool = new ExcelTool(MessageSource.M("MARKING_STATISTICS_EXCEL_TITLE"), 20, 20);
		List<Column> titleData = excelTool.columnTransformer(titleList);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("utf-8");
		excelTool.exportExcel(titleData, queryList, response.getOutputStream(), true, false);
	}

	/**
	 * 更新记录数
	 *
	 * @param list
	 * @param organizationId
	 * @return
	 */
	public List<MarkingStatistic> setCount(List<MarkingStatistic> list, Long organizationId) {
		for (MarkingStatistic markingStatistic : list) {
			Marking marking = new Marking();
			marking.setCategory_id(Long.valueOf(markingStatistic.getCategoryId()));
			marking.setCreate_by(markingStatistic.getUserId());
			marking.setProject_id(markingStatistic.getProjectId());
			marking.setOrganization_id(organizationId);
			markingStatistic.setMarkingNum(markingMapper.selectMarkingNum(marking));
		}
		return list;
	}

	private List<Long> getProjectIdStatistics(Long userId, Long organizationId) {
		// 查询当前用户参与的所有项目
		List<Long> projectIdList = new ArrayList<>();
		ProjectMember projectMember = new ProjectMember();
		projectMember.setUserId(userId);
		projectMember.setOrganizationId(organizationId);
		List<SelectProjectVO> list = projectMemberService.getProjectListByPM(projectMember);
		if (CollectionUtils.isNotEmpty(list)) {
			for (SelectProjectVO vo : list) {
				projectIdList.add(vo.getProjectId());
			}
		}
		return projectIdList;
	}
}
