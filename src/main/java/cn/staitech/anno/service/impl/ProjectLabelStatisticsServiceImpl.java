package cn.staitech.anno.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.MarkingStatistic;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.ProjectAnnoStatistics;
import cn.staitech.anno.domain.ProjectLabelStatistics;
import cn.staitech.anno.domain.ProjectStatistics;
import cn.staitech.anno.domain.ProjectUserLabelStatistics;
import cn.staitech.anno.mapper.IndicatorMapper;
import cn.staitech.anno.mapper.MarkingMapper;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.mapper.ProjectLabelStatisticsMapper;
import cn.staitech.anno.mapper.ProjectMapper;
import cn.staitech.anno.project.mapper.SysUserMapperV1;
import cn.staitech.anno.project.vo.ImageAnnoStatisticsVO;
import cn.staitech.anno.service.ProjectAnnoStatisticsService;
import cn.staitech.anno.service.ProjectInStatisticsService;
import cn.staitech.anno.service.ProjectLabelStatisticsService;
import cn.staitech.anno.service.ProjectStatisticsService;
import cn.staitech.anno.service.ProjectUserLabelStatisticsService;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectLabelOut;
import cn.staitech.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 标注统计-项目/标签 统计 服务实现类
 * </p>
 *
 * @author wanglibei
 * @since 2024-01-24
 */

@Slf4j
@Service
public class ProjectLabelStatisticsServiceImpl extends ServiceImpl<ProjectLabelStatisticsMapper, ProjectLabelStatistics> implements ProjectLabelStatisticsService {

	@Resource
	private MarkingMapper markingMapper;
	@Resource
	private ProjectMapper projectMapper;
	//	@Resource
	//	private SysUserMapper  sysUserMapper;
	@Resource
	private SysUserMapperV1 userMapperV1;
	@Resource
	private IndicatorMapper  indicatorMapper;
	@Resource
	private PathologicalIndicatorCategoryMapper  pathologicalIndicatorCategoryMapper;
	@Resource
	private RedisService redisService;
	@Resource
	private ProjectAnnoStatisticsService projectAnnoStatisticsService;
	@Resource
	private ProjectUserLabelStatisticsService projectUserLabelStatisticsService;

	@Resource
	private ProjectInStatisticsService projectStatisticsService;

	/**
	 * 生成业务数据
	 */
	@Override
	public void generateData() {
		long startTime = System.currentTimeMillis();
		//加载用户+标签集+标签进入缓存
		initCache();
		Date currentDate = DateUtil.date();

		//查询所有标注项目  projectType项目类型:1标注2评审3标准训练集
		QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("project_type", 1);
		queryWrapper.eq("del_flag", 0);
		queryWrapper.isNotNull("indicator_id");
		// 测试 queryWrapper.eq("project_id", 551);
		List<Project> projectList = projectMapper.selectList(queryWrapper);
		//处理业务数据
		if(CollectionUtils.isNotEmpty(projectList)){
			try {
				//tb_project_statistics处理
				handlerProjectStatistics(projectList, currentDate);
				Thread.sleep(5L);
				//tb_project_label_statistics处理
				handlerProjectLabelStatistics(projectList, currentDate);
				Thread.sleep(5L);
				//tb_project_anno_statistics表处理
				handlerProjectAnnoStatistics(projectList, currentDate);
				Thread.sleep(5L);
				//tb_project_user_label_statistics表处理
				handlerProjectUserLabelStatistics(projectList, currentDate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
	}

	/**
	 * 
	 * @Title: initCache
	 * @Description: 缓存数据处理
	 * @param 
	 * @return void
	 * @throws
	 */
	private void initCache(){
		//所有用户信息查询
		QueryWrapper<cn.staitech.anno.project.domain.SysUser> queryUserWrapper = new QueryWrapper<>();
		queryUserWrapper.eq("del_flag", 0);
		List<cn.staitech.anno.project.domain.SysUser> userList = userMapperV1.selectList(queryUserWrapper);
		if(CollectionUtils.isNotEmpty(userList)){
			for(cn.staitech.anno.project.domain.SysUser user:userList){
				redisService.setCacheObject(CommonConstant.STATISTICS_USER+ user.getUserId(), user, 4L, TimeUnit.HOURS);
			}
		}
		//所有标签集信息查询
		Indicator indicator = new Indicator();
		indicator.setDelFlag(0);
		List<Indicator> indicatorList = indicatorMapper.selectIndicator(indicator);
		if(CollectionUtils.isNotEmpty(indicatorList)){
			for(Indicator indicatorP:indicatorList){
				redisService.setCacheObject(CommonConstant.STATISTICS_INDICATOR + indicatorP.getIndicatorId(), indicatorP, 4L, TimeUnit.HOURS);
			}
		}

		//所有标签集下的标签查询
		QueryWrapper<PathologicalIndicatorCategory> queryCategoryWrapper = new QueryWrapper<>();
		queryCategoryWrapper.eq("del_flag", 0);
		List<PathologicalIndicatorCategory> categoryList = pathologicalIndicatorCategoryMapper.selectList(queryCategoryWrapper);

		if(CollectionUtils.isNotEmpty(categoryList)){
			for(PathologicalIndicatorCategory category:categoryList){
				Long categoryId =  category.getCategoryId();
				redisService.setCacheObject(CommonConstant.STATISTICS_CATEGORY + categoryId, category, 4L, TimeUnit.HOURS);
			}
		}
	}

	/**
	 * 
	 * @Title: handlerProjectStatistics
	 * @Description: 项目统计
	 * @param @param projectList
	 * @param @param currentDate
	 * @return void
	 * @throws
	 */
	private void handlerProjectStatistics(List<Project> projectList,Date currentDate){
		delData(4);
		List<ProjectStatistics> plsList = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		for(Project project:projectList){
			Long projectId = project.getProjectId();
			String projectName = project.getProjectName();
			String description = project.getDescription();
			Date projectCreateTime = project.getCreateTime();
			int status = project.getStatus();
			Long projectCreateBy = project.getCreateBy();

			cn.staitech.anno.project.domain.SysUser userInformation =  getSysUserByUserId(projectCreateBy);
			String createNickName = "";
			if(null != userInformation){
				createNickName = userInformation.getNickName();
			}
			Long organizationId = userInformation.getOrganizationId();

			//根据创建人查询昵称
			Long indicatorId = project.getIndicatorId();
			Indicator indicatorInfo = getIndicatorByIndicatorId(indicatorId);
			String indicatorName = indicatorInfo.getIndicatorName();

			//查询当前项目下 标签集 的imageCount
			List<ProjectLabelOut> imageStatisList = markingMapper.getProjectImageNum(projectId);
			Map<Long,String> categImageNumMap = new HashMap<>();
			if(CollectionUtils.isNotEmpty(imageStatisList)){
				for(ProjectLabelOut imageS:imageStatisList){
					String imageNum = imageS.getImageNum();
					categImageNumMap.put(projectId, imageNum);
				}
			}


			//查询当前项目下 标签集+标签 的annoCount
			List<ProjectLabelOut> annoStatisList = markingMapper.getProjectMarkingNum(projectId);
			Map<Long,String> markingNumMap = new HashMap<>();
			if(CollectionUtils.isNotEmpty(annoStatisList)){
				for(ProjectLabelOut imageS:annoStatisList){
					String markingNum = imageS.getMarkingNum();
					markingNumMap.put(projectId, markingNum);
				}
			}


			//遍历所有的标签集进行数据填充
			if(!categImageNumMap.isEmpty() && categImageNumMap.size() >0){
				for (Map.Entry<Long, String> entry : categImageNumMap.entrySet()) {
					Long projectIdP = entry.getKey();
					String categImageNum = entry.getValue();
					ProjectStatistics projectStatistics = new ProjectStatistics();
					projectStatistics.setProjectId(projectIdP);
					projectStatistics.setProjectName(projectName);
					projectStatistics.setProjectStatus(status);
					projectStatistics.setDescription(description);
					projectStatistics.setProjectCreateBy(projectCreateBy);
					projectStatistics.setCreateNickName(createNickName);
					projectStatistics.setProjectCreateTime(projectCreateTime);
					projectStatistics.setIndicatorId(indicatorId);
					projectStatistics.setIndicatorName(indicatorName);

					projectStatistics.setImageNum(Long.valueOf(categImageNum));
					Long markingNum = 0L;
					if(markingNumMap.containsKey(projectIdP)){
						markingNum = Long.valueOf(markingNumMap.get(projectIdP));
					}
					projectStatistics.setMarkingNum(markingNum);
					projectStatistics.setOrganizationId(organizationId);;
					projectStatistics.setTaskCreateTime(currentDate);
					projectStatistics.setDelFlag("0");
					String key = projectIdP+"_"+indicatorId+"_"+organizationId;
					if(!map.containsKey(key)){
						map.put(key, key);
						plsList.add(projectStatistics);
					}
					if(plsList.size() > 2000){
						projectStatisticsService.saveBatch(plsList);
						plsList = new ArrayList<>();
						map = new HashMap<>();
					}
				}
			}
		}
		
		//剩下的统一保存
		if(CollectionUtils.isNotEmpty(plsList)){
			projectStatisticsService.saveBatch(plsList);
		}
	}


	/**
	 * 
	 * @Title: handlerProjectLabelStatistics
	 * @Description: tb_project_label_statistics处理
	 * @param @param projectList
	 * @param @param currentDate
	 * @return void
	 * @throws
	 */
	private void handlerProjectLabelStatistics(List<Project> projectList,Date currentDate){
		delData(1);
		List<ProjectLabelStatistics> plsList = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		for(Project project:projectList){
			Long projectId = project.getProjectId();
			String projectName = project.getProjectName();
			String description = project.getDescription();
			Date projectCreateTime = project.getCreateTime();
			int status = project.getStatus();
			Long projectCreateBy = project.getCreateBy();

			cn.staitech.anno.project.domain.SysUser userInformation =  getSysUserByUserId(projectCreateBy);
			String createNickName = "";
			if(null != userInformation){
				createNickName = userInformation.getNickName();
			}
			Long organizationId = userInformation.getOrganizationId();

			//根据创建人查询昵称
			Long indicatorId = project.getIndicatorId();
			Indicator indicatorInfo = getIndicatorByIndicatorId(indicatorId);
			String indicatorName = indicatorInfo.getIndicatorName();

			//查询当前项目下 标签集+标签 的imageCount
			List<ProjectLabelOut> imageStatisList = markingMapper.getProjectCategoryImageNum(projectId);
			Map<Long,String> categImageNumMap = new HashMap<>();
			if(CollectionUtils.isNotEmpty(imageStatisList)){
				for(ProjectLabelOut imageS:imageStatisList){
					Long categId = imageS.getCategoryId();
					String imageNum = imageS.getImageNum();
					categImageNumMap.put(categId, imageNum);
				}
			}


			//查询当前项目下 标签集+标签 的annoCount
			List<ProjectLabelOut> annoStatisList = markingMapper.getProjectCategoryMarkingNum(projectId);
			Map<Long,String> categMarkingNumMap = new HashMap<>();
			if(CollectionUtils.isNotEmpty(annoStatisList)){
				for(ProjectLabelOut imageS:annoStatisList){
					Long categId = imageS.getCategoryId();
					String markingNum = imageS.getMarkingNum();
					categMarkingNumMap.put(categId, markingNum);
				}
			}


			//遍历所有的标签集进行数据填充
			if(!categImageNumMap.isEmpty() && categImageNumMap.size() >0){
				for (Map.Entry<Long, String> entry : categImageNumMap.entrySet()) {
					Long categoryP = entry.getKey();
					String categImageNum = entry.getValue();
					ProjectLabelStatistics projectLabelStatistics = new ProjectLabelStatistics();
					projectLabelStatistics.setProjectId(projectId);
					projectLabelStatistics.setProjectName(projectName);
					projectLabelStatistics.setProjectStatus(status);
					projectLabelStatistics.setDescription(description);
					projectLabelStatistics.setProjectCreateBy(projectCreateBy);
					projectLabelStatistics.setCreateNickName(createNickName);
					projectLabelStatistics.setProjectCreateTime(projectCreateTime);
					projectLabelStatistics.setIndicatorId(indicatorId);
					projectLabelStatistics.setIndicatorName(indicatorName);
					projectLabelStatistics.setCategoryId(categoryP);

					PathologicalIndicatorCategory category = getPathologicalIndicatorCategoryByCategoryId(categoryP);

					String categoryName = "";
					if(null != category){
						categoryName = category.getCategoryName();
					}
					projectLabelStatistics.setCategoryName(categoryName);
					projectLabelStatistics.setImageNum(Long.valueOf(categImageNum));
					Long markingNum = 0L;
					if(categMarkingNumMap.containsKey(categoryP)){
						markingNum = Long.valueOf(categMarkingNumMap.get(categoryP));
					}
					projectLabelStatistics.setMarkingNum(markingNum);
					projectLabelStatistics.setOrganizationId(organizationId);;
					projectLabelStatistics.setTaskCreateTime(currentDate);
					projectLabelStatistics.setDelFlag("0");
					String key = projectId+"_"+indicatorId+"_"+categoryP+"_"+organizationId;
					if(!map.containsKey(key)){
						map.put(key, key);
						plsList.add(projectLabelStatistics);
					}
					if(plsList.size() > 2000){
						saveBatch(plsList);
						plsList = new ArrayList<>();
						map = new HashMap<>();
					}
				}
				//剩下的统一保存
				if(CollectionUtils.isNotEmpty(plsList)){
					saveBatch(plsList);
				}
			}

		}
	}

	/**
	 * 
	 * @Title: handlerProjectAnnoStatistics
	 * @Description: tb_project_anno_statistics表处理
	 * @param @param projectList
	 * @param @param currentDate
	 * @return void
	 * @throws
	 */
	private void handlerProjectAnnoStatistics(List<Project> projectList,Date currentDate){
		delData(2);
		List<ProjectAnnoStatistics> pasList = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		for(Project project:projectList){
			Long projectId = project.getProjectId();
			String projectName = project.getProjectName();
			int status = project.getStatus();

			// 查询当前项目所有用户标注的imageCount
			List<ImageAnnoStatisticsVO> imageCountList = markingMapper.getProjectAnnoImageNum(projectId);
			Map<Long,Integer> imageCountmMap = new HashMap<>();
			if(CollectionUtils.isNotEmpty(imageCountList)){
				for(ImageAnnoStatisticsVO ann0Vo:imageCountList){
					Long userIdP = ann0Vo.getUserId();
					int imageCount = ann0Vo.getImageCount();
					imageCountmMap.put(userIdP, imageCount);
				}
			}
			//查询当前项目所有用户标注的annoCount
			List<ImageAnnoStatisticsVO> annoCountList = markingMapper.getProjectAnnoMarkingNum(projectId);
			Map<Long,Integer> annoCountMap = new HashMap<>();
			if(CollectionUtils.isNotEmpty(annoCountList)){
				for(ImageAnnoStatisticsVO annCVo:annoCountList){
					Long userIdP = annCVo.getUserId();
					int markingNum = annCVo.getMarkingNum();
					annoCountMap.put(userIdP, markingNum);
				}
			}

			if(!imageCountmMap.isEmpty() && imageCountmMap.size() >0){
				for (Map.Entry<Long, Integer> entry : imageCountmMap.entrySet()) {
					Long userIdP = entry.getKey();
					Integer imageCount = entry.getValue();
					ProjectAnnoStatistics annoStatistics = new ProjectAnnoStatistics();
					annoStatistics.setProjectId(projectId);
					annoStatistics.setProjectName(projectName);
					annoStatistics.setProjectStatus(status);
					annoStatistics.setAnnoUserId(userIdP);
					cn.staitech.anno.project.domain.SysUser userInfo =  getSysUserByUserId(userIdP);
					String nickName = "";
					if(null != userInfo){
						nickName = userInfo.getNickName();
					}
					annoStatistics.setAnnoNickName(nickName);

					annoStatistics.setImageCount(Long.valueOf(imageCount));
					Long markingNum = 0L;
					if(annoCountMap.containsKey(userIdP)){
						markingNum = Long.valueOf(annoCountMap.get(userIdP));
					}
					annoStatistics.setMarkingNum(markingNum);
					Long organizationIdP = userInfo.getOrganizationId();
					annoStatistics.setOrganizationId(organizationIdP);;
					annoStatistics.setTaskCreateTime(currentDate);
					annoStatistics.setDelFlag("0");
					
					String key = projectId+"_"+userIdP+"_"+organizationIdP;
					if(!map.containsKey(key)){
						map.put(key, key);
						pasList.add(annoStatistics);
					}
					if(pasList.size() > 2000){
						projectAnnoStatisticsService.saveBatch(pasList);
						pasList = new ArrayList<>();
						map = new HashMap<>();
					}
				}
			}

		}
		//batchSave
		if(CollectionUtils.isNotEmpty(pasList)){
			projectAnnoStatisticsService.saveBatch(pasList);
		}

	}


	/**
	 * 
	 * @Title: handlerProjectUserLabelStatistics
	 * @Description: tb_project_user_label_statistics表处理开始
	 * @param @param projectList
	 * @param @param currentDate
	 * @return void
	 * @throws
	 */
	private void handlerProjectUserLabelStatistics(List<Project> projectList,Date currentDate){
		delData(3);
		List<ProjectUserLabelStatistics> pusList = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		for(Project project:projectList){
			Long projectId = project.getProjectId();
			String projectName = project.getProjectName();

			//根据创建人查询昵称
			Long indicatorId = project.getIndicatorId();
			Indicator indicatorInfo = getIndicatorByIndicatorId(indicatorId);
			String indicatorName = indicatorInfo.getIndicatorName();

			//查询当前项目下所有用户标注+ 标签集+标签 的annoCount
			List<MarkingStatistic> pulsList = markingMapper.getProjectUserLabelMarkingNum(projectId);
			if(CollectionUtils.isNotEmpty(pulsList)){
				for(MarkingStatistic ms:pulsList){
					Long userIdP = ms.getUserId();
					Long markingNum = ms.getMarkingNum();
					String caid = ms.getCategoryId();
					ProjectUserLabelStatistics pulStatistics = new ProjectUserLabelStatistics();
					pulStatistics.setProjectId(projectId);
					pulStatistics.setProjectName(projectName);
					pulStatistics.setAnnoUserId(userIdP);
					cn.staitech.anno.project.domain.SysUser userInfo = getSysUserByUserId(userIdP);
					String nickName = "";
					if(null != userInfo){
						nickName = userInfo.getNickName();
					}
					pulStatistics.setAnnoNickName(nickName);
					pulStatistics.setMarkingNum(markingNum);
					Long organizationIdP = userInfo.getOrganizationId();
					pulStatistics.setOrganizationId(organizationIdP);

					//标签
					PathologicalIndicatorCategory category = getPathologicalIndicatorCategoryByCategoryId(Long.valueOf(caid));
					String categoryName = "";
					if(null != category){
						categoryName = category.getCategoryName();
					}
					pulStatistics.setCategoryId(Long.valueOf(caid));
					pulStatistics.setCategoryName(categoryName);

					//查询标签的所属标签集
					pulStatistics.setIndicatorId(indicatorId);
					pulStatistics.setIndicatorName(indicatorName);

					pulStatistics.setTaskCreateTime(currentDate);
					pulStatistics.setDelFlag("0");

					String key = projectId+"_"+userIdP+"_"+indicatorId+"_"+caid+"_"+organizationIdP;

					if(!map.containsKey(key)){
						map.put(key, key);
						pusList.add(pulStatistics);
					}
					if(pusList.size() > 2000){
						projectUserLabelStatisticsService.saveBatch(pusList);
						pusList = new ArrayList<>();
						map = new HashMap<>();
					}
				}
			}
		}
		
		//剩下的统一保存
		if(CollectionUtils.isNotEmpty(pusList)){
			projectUserLabelStatisticsService.saveBatch(pusList);
		}
	}

	/**
	 * 
	 * @Title: getSysUserByUserId
	 * @Description: 查询用户信息
	 * @param @param userId
	 * @param @return
	 * @return SysUser
	 * @throws
	 */
	private cn.staitech.anno.project.domain.SysUser getSysUserByUserId(Long userId){
		cn.staitech.anno.project.domain.SysUser sysUser = redisService.getCacheObject(CommonConstant.STATISTICS_USER+ userId);
		if(null == sysUser){
			/*QueryWrapper<SysUser> queryUWrapper = new QueryWrapper<>();
			queryUWrapper.eq("del_flag", 0);
			List<SysUser> userAgainList = sysUserMapper.selectList(queryUWrapper);*/

			QueryWrapper<cn.staitech.anno.project.domain.SysUser> userQueryWrapper = new QueryWrapper<>();
			userQueryWrapper.in("del_flag", 0);
			List<cn.staitech.anno.project.domain.SysUser> userList = userMapperV1.selectList(userQueryWrapper);


			if(CollectionUtils.isNotEmpty(userList)){
				for(cn.staitech.anno.project.domain.SysUser user:userList){
					redisService.setCacheObject(CommonConstant.STATISTICS_USER+ user.getUserId(), user, 4L, TimeUnit.HOURS);
				}
				sysUser = redisService.getCacheObject(CommonConstant.STATISTICS_USER+ userId);
			}
		}
		return sysUser;
	}

	/**
	 * 
	 * @Title: getIndicatorByIndicatorId
	 * @Description: 查询标签集信息
	 * @param @param indicatorId
	 * @param @return
	 * @return Indicator
	 * @throws
	 */
	private Indicator getIndicatorByIndicatorId(Long indicatorId){
		Indicator indicator = redisService.getCacheObject(CommonConstant.STATISTICS_INDICATOR+ indicatorId);
		if(null == indicator){
			Indicator indicatorA = new Indicator();
			indicatorA.setDelFlag(0);
			List<Indicator> indicatorAList = indicatorMapper.selectIndicator(indicatorA);
			if(CollectionUtils.isNotEmpty(indicatorAList)){
				for(Indicator indicatorP:indicatorAList){
					redisService.setCacheObject(CommonConstant.STATISTICS_INDICATOR + indicatorP.getIndicatorId(), indicatorP, 4L, TimeUnit.HOURS);
				}
				indicator = redisService.getCacheObject(CommonConstant.STATISTICS_INDICATOR+ indicatorId);
			}
		}
		return indicator;
	}

	/**
	 * 
	 * @Title: getPathologicalIndicatorCategoryByCategoryId
	 * @Description: 查询标签信息
	 * @param @param categoryId
	 * @param @return
	 * @return PathologicalIndicatorCategory
	 * @throws
	 */
	private PathologicalIndicatorCategory getPathologicalIndicatorCategoryByCategoryId(Long categoryId){
		PathologicalIndicatorCategory category =  new PathologicalIndicatorCategory();
		if(categoryId.equals(0)){
			category.setCategoryName("无属性");
		}else{
			category = redisService.getCacheObject(CommonConstant.STATISTICS_CATEGORY  + categoryId);
			if(null == category){
				QueryWrapper<PathologicalIndicatorCategory> queryCategoryAWrapper = new QueryWrapper<>();
				queryCategoryAWrapper.eq("del_flag", 0);
				List<PathologicalIndicatorCategory> categoryAList = pathologicalIndicatorCategoryMapper.selectList(queryCategoryAWrapper);

				if(CollectionUtils.isNotEmpty(categoryAList)){
					for(PathologicalIndicatorCategory catP:categoryAList){
						Long categoryIds =  catP.getCategoryId();
						redisService.setCacheObject(CommonConstant.STATISTICS_CATEGORY + categoryIds, catP, 4L, TimeUnit.HOURS);
					}
					category = redisService.getCacheObject(CommonConstant.STATISTICS_CATEGORY  + categoryId);
				}
			}
		}
		return category;
	}


	private void delData(int type){
		if(type == 1){
			//tb_project_label_statistics处理
			UpdateWrapper<ProjectLabelStatistics> updateWrapper = Wrappers.update();
			// 修改条件为del_flag=0的数据
			updateWrapper.eq("del_flag", 0);
			ProjectLabelStatistics pls = new ProjectLabelStatistics();
			pls.setDelFlag("1");
			//修改分析状态为进行中
			update(pls, updateWrapper);
		}else if(type == 2){
			//tb_project_anno_statistics表处理
			UpdateWrapper<ProjectAnnoStatistics> updateWrapper = Wrappers.update();
			// 修改条件为del_flag=0的数据
			updateWrapper.eq("del_flag", 0);
			ProjectAnnoStatistics pas = new ProjectAnnoStatistics();
			pas.setDelFlag("1");
			//修改分析状态为进行中
			projectAnnoStatisticsService.update(pas, updateWrapper);
		}else if(type == 3){
			//tb_project_user_label_statistics表处理
			UpdateWrapper<ProjectUserLabelStatistics> updateWrapper = Wrappers.update();
			// 修改条件为del_flag=0的数据
			updateWrapper.eq("del_flag", 0);
			ProjectUserLabelStatistics puls = new ProjectUserLabelStatistics();
			puls.setDelFlag("1");
			//修改分析状态为进行中
			projectUserLabelStatisticsService.update(puls, updateWrapper);
		}else if(type == 4){
			//tb_project_statistics表处理
			UpdateWrapper<ProjectStatistics> updateWrapper = Wrappers.update();
			// 修改条件为del_flag=0的数据
			updateWrapper.eq("del_flag", 0);
			ProjectStatistics puls = new ProjectStatistics();
			puls.setDelFlag("1");
			//修改分析状态为进行中
			projectStatisticsService.update(puls, updateWrapper);
		}
	}

}
