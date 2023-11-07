package cn.staitech.anno.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.*;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.examination.ExaminationListVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvGetPagerVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvGetVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvListVO;
import cn.staitech.anno.vo.marking.Marking;
import cn.staitech.anno.vo.project.ProjectExt;
import cn.staitech.anno.vo.project.ProjectListOutVO;
import cn.staitech.anno.vo.project.ProjectStatisticsVO;
import cn.staitech.anno.vo.slide.*;
import cn.staitech.anno.vo.special.Special;
import cn.staitech.anno.vo.statistic.StatisticSlideListInVO;
import cn.staitech.anno.vo.statistic.StatisticSlideListOutVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.annotations.Param;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.staitech.anno.aspect.LogFileAspect.response;

/**
 * 切片 服务层实现
 *
 * @author staitech
 */
@Slf4j
@Service
public class SlideServiceImpl extends ServiceImpl<SlideMapper, Slide> implements SlideService {
    @Resource
    private SlideMapper slideMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private MarkingMapper markingMapper;

    @Resource
    private SpecialMapper specialMapper;

    @Resource
    private ProjectExtMapper projectExtMapper;

    @Resource
    private GroupMapper groupMapper;

    @Resource
    private RecentlyVisitedMapper recentlyVisitedMapper;

    @Resource
    private MarkingService markingService;

    @Resource(name = "redissonClient")
    private RedissonClient client;

    @Resource
    private ImageCsvMapper imageCsvMapper;

    @Resource
    private ImageMapper imageMapper;

    /**
     * 查询单条切片详情
     *
     * @param slideId
     * @return
     */
    @Override
    public Slide selectById(Long slideId) {
        return slideMapper.selectById(slideId);
    }

    /**
     * 更新切片表人工标注数及标注状态
     *
     * @param examinationListVo
     * @return
     */
    @Override
    public int updateSlideHumanAnnotationQuantity(ExaminationListVO examinationListVo) {
        return slideMapper.updateSlideHumanAnnotationQuantity(examinationListVo);
    }

    /**
     * 根据项目ID列表查询图像列表
     *
     * @param projectIdList 项目ID列表
     * @return
     */
    public List<StatisticSlideListOutVO> selectSlideListByProjectIdList(StatisticSlideListInVO projectIdList) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            projectIdList.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        }
        return slideMapper.selectSlideListByProjectIdList(projectIdList);
    }

    /**
     * 通过切片ID查询切片信息
     *
     * @param slideId
     * @return
     */
    public Slide selectByPrimaryKey(Long slideId) {
        return slideMapper.selectByPrimaryKey(slideId);
    }

    /**
     * 删除图片
     *
     * @param slideIdList
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProjectImage(Long[] slideIdList) throws Exception {
        int count = 0;
        for (Long slideId : slideIdList) {
            Slide slide = slideMapper.selectById(slideId);
            int processFlag = slide.getProcessFlag();
            if (processFlag == 0) {
                count += slideMapper.deleteProjectImage(slideId);
                Project project = new Project();
                project.setUpdateBy(SecurityUtils.getUserId());
                project.setProjectId(slide.getProjectId());
                //更新项目表数据（更新项目图像数量）
                projectMapper.updateProject(project);

            } else if (processFlag == 1) {
                throw new Exception(MessageSource.M("SERIAL_NO") + slideId + MessageSource.M("IN_DIMENSION"));
            } else if (processFlag == 2) {
                throw new Exception(MessageSource.M("SERIAL_NO") + slideId + MessageSource.M("DIMENSIONING_COMPLETE"));
            } else if (processFlag == 3) {
                throw new Exception(MessageSource.M("SERIAL_NO") + slideId + MessageSource.M("SUBMITTED_FOR_REVIEW"));
            }
        }
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取项目信息
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Override
    public List<Slide> getProjectInformation(Long projectId) {
        return slideMapper.getProjectInformation(projectId);

    }

    /**
     * 根据项目id删除信息
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Override
    public int deleteProjectInformation(Long projectId) {
        return slideMapper.deleteProjectInformation(projectId);
    }

    /**
     * 项目id 和图片id 查询是否存在
     *
     * @param
     * @return 结果
     */
    @Override
    public List<Slide> selectImageExist(Slide slide) {
        return slideMapper.selectImageExist(slide);
    }

    /**
     * 通过项目id 查询审核过的数量
     *
     * @param projectId
     * @return 结果
     */
    @Override
    public Integer selectCheckNum(Long projectId) {
        return slideMapper.selectCheckNum(projectId);
    }

    /**
     * 通过项目id 和状态查询
     *
     * @param slideSelectVo
     * @return 结果
     */
    @Override
    public List<ProjectListOutVO> selectByStatus(SlideSelectVO slideSelectVo) {
        return slideMapper.selectByStatus(slideSelectVo);
    }

    /**
     * 项目批量添加图片
     *
     * @param slideList
     * @return
     */
    @Override
    public int insertSlide(@Param("slideList") List<Slide> slideList) {
        return slideMapper.insertSlide(slideList);
    }

    /**
     * 项目批量添加图片
     *
     * @param slide
     * @return
     */
    @Override
    public int updateDescription(Slide slide) {
        return slideMapper.updateDescription(slide);
    }

    @Override
    public Boolean markIsNotFinish(Long slideId) {
        Slide slide = slideMapper.selectById(slideId);
        if (slide == null) {
            return false;
        }
        return slide.getProcessFlag() < 2;
    }

    /**
     * 更新切片时间
     *
     * @param slide
     * @return
     */
    @Override
    public int updateSlideTime(Slide slide) {
        return slideMapper.updateSlideTime(slide);
    }

    /**
     * 根据项目、分组及图像更新切片关系表
     * todo 逻辑待完善
     *
     * @param slideList
     * @return
     */
    @Transactional
    @Override
    public int updateBatchByCondition(List<Slide> slideList) {
        return slideMapper.updateBatchByCondition(slideList);
    }


    /**
     * 查询组内切片报表摘要
     *
     * @param params
     * @return
     */
    @Override
    public R<SlideReportSummaryVO> querySlideByProjectAndGroup(Map params) {
        SlideReportSummaryVO vo = getBaseMapper().selectSlideByProjectAndGroup(params);
        if (vo != null && vo.getFinishTotal() == null) {
            vo.setFinishTotal(0);
        } else {
            vo = new SlideReportSummaryVO();
            ProjectExt projectExt = projectExtMapper.selectById(MapUtils.getLong(params, "projectId"));
            Group group = groupMapper.selectById(MapUtils.getLong(params, "groupId"));
            vo.setProjectName(projectExt.getProjectName());
            vo.setGroupName(group.getGroupName());
            // vo.setGender(group.getGender() == 0 ? "雌" : "雄");
            vo.setFinishTotal(0);
            vo.setTotal(0);
            // vo.setDescription(group.getDescription());
        }
        return R.ok(vo);
    }

    /**
     * 组内切片报表分页查询
     *
     * @param params
     * @return
     */
    @Override
    public R<PageMaster<SlideReportVO>> pageSlideWithSubImage(Map params) {
        Page<SlideReportVO> page = new Page<>(MapUtils.getInteger(params, "pageNum", 1), MapUtils.getInteger(params, "pageSize", 10));
        getBaseMapper().pageSlideWithSubImage(page, params);
        List<SlideReportVO> list = page.getRecords();
        list.forEach(vo -> {
            //从redis查询编辑状态
            RMap<String, Map> slideLocks = client.getMap("SlideLocks");
            Map user = slideLocks.get(vo.getSlideId());
            //默认可编辑
            vo.setStatus(0);
            if (user != null) {
                vo.setOperatorId(MapUtils.getLong(user, "userId"));
                vo.setOperatorName(MapUtils.getString(user, "userName"));
                //设置不可编辑
                vo.setStatus(1);
            }
        });
        //构建分页对象
        PageMaster<SlideReportVO> pageMaster = PageMaster.of(list);
        pageMaster.setTotal(page.getTotal());
        return R.ok(pageMaster);
    }

    /**
     * 项目内切片统计
     *
     * @param params
     * @return
     */
    @Override
    public R<PageMaster<ProjectStatisticsVO>> pageSlideStatisticsByProject(Map params) {
        Page<ProjectStatisticsVO> page = new Page<>(MapUtils.getInteger(params, "pageNum", 1), MapUtils.getInteger(params, "pageSize", 10));
        //构建项目查询条件
        List<Long> specialIds = new ArrayList<>();
        Long specialId = MapUtils.getLong(params, "specialId");
        if (specialId == null) {
            //查询专题
            Map<String, Object> querySpecialParams = ImmutableMap.of("delFlag", 0, "userId", SecurityUtils.getUserId());
            List<Special> specialList = specialMapper.selectByUserId(querySpecialParams);
            if (!specialList.isEmpty()) {
                specialList.forEach(s -> {
                    specialIds.add(s.getSpecialId());
                });
            }
        } else {
            specialIds.add(specialId);
        }
        params.put("specialIds", specialIds);
        getBaseMapper().pageSlideStatisticsByProject(page, params);
        //构建分页对象
        PageMaster<ProjectStatisticsVO> pageMaster = PageMaster.of(page.getRecords());
        pageMaster.setTotal(page.getTotal());
        return R.ok(pageMaster);
    }

    /**
     * 切片报表分页查询
     *
     * @param params
     * @return
     */
    @Override
    public R<PageMaster<SlideReportVO>> pageSlideStatistics(Map params) {

        //构建项目查询条件
        List<Long> specialIds = new ArrayList<>();
        List<Long> projectIds = new ArrayList<>();
        Long specialId = MapUtils.getLong(params, "specialId");
        if (specialId == null) {
            //查询专题
            Map<String, Object> querySpecialParams = ImmutableMap.of("delFlag", 0, "userId", SecurityUtils.getUserId());
            List<Special> specialList = specialMapper.selectByUserId(querySpecialParams);
            if (!specialList.isEmpty()) {
                specialList.forEach(s -> {
                    specialIds.add(s.getSpecialId());
                });
            }
        } else {
            specialIds.add(specialId);
        }
        params.put("specialIds", specialIds);
        //查询项目
        Wrapper wrapper = Wrappers.query().eq("del_flag", 0).in("special_id", specialIds);
        List<ProjectPo> projectList = projectExtMapper.selectList(wrapper);
        //构建分组查询条件
        if (!projectList.isEmpty()) {
            projectList.forEach(p -> {
                projectIds.add(p.getProjectId());
            });
        }
        //没有项目直接返回空分页对象
        if (projectIds.isEmpty()) {
            return R.ok(PageMaster.EMPTY);
        }
        Page<SlideReportVO> page = new Page<>(MapUtils.getInteger(params, "pageNum", 1), MapUtils.getInteger(params, "pageSize", 10));
        params.put("projectIds", projectIds);
        getBaseMapper().pageSlideStatistics(page, params);
        List<SlideReportVO> list = page.getRecords();
        //分析预测进度：当前切片的分析进度+人工诊断进度展示
        //（1）待阅片：所有切片的初始进度都为待阅片，页面显示参考列表第一行
        //（2）AI分析中：依据各分组内的切片列表后的状态对应显示
        //（3）AI分析完成：依据各分组内的切片列表后的状态对应显示
        //（4）AI分析失败：依据各分组内的切片列表后的状态对应显示
        //（5）人工诊断：依据各分组内的切片列表后的状态对应显示
        //（6）当没有算法可以匹配的模型时，便只能人工诊断，此时前端显示参考（列表最后一行）
        list.forEach(vo -> {
            vo.addTask(ImmutableMap.of("code", "1", "name", "待分析", "isFinish", "true", "Color", "blue"));
            int aiAnalyzed = vo.getAiAnalyzed();
            if (aiAnalyzed == 1) {
                vo.addTask(ImmutableMap.of("code", "2", "name", "AI分析中", "isFinish", "true", "Color", "blue"));
                vo.addTask(ImmutableMap.of("code", "3", "name", "AI分析成功", "isFinish", "true", "Color", "grey"));
            } else if (aiAnalyzed == 2) {
                vo.addTask(ImmutableMap.of("code", "2", "name", "AI分析中", "isFinish", "true", "Color", "blue"));
                vo.addTask(ImmutableMap.of("code", "3", "name", "AI分析成功", "isFinish", "true", "Color", "green"));
            } else if (aiAnalyzed == 3) {
                vo.addTask(ImmutableMap.of("code", "2", "name", "AI分析中", "isFinish", "true", "Color", "blue"));
                vo.addTask(ImmutableMap.of("code", "4", "name", "AI分析失败", "isFinish", "true", "Color", "red"));
            }
            int diagnosis = vo.getDiagnosis();
            if (diagnosis == 1) {
                vo.addTask(ImmutableMap.of("code", "5", "name", "已诊断", "isFinish", "true", "Color", "yellow"));
            } else {
                vo.addTask(ImmutableMap.of("code", "2", "name", "AI分析中", "isFinish", "true", "Color", "grey"));
                vo.addTask(ImmutableMap.of("code", "3", "name", "AI分析成功", "isFinish", "true", "Color", "grey"));
                vo.addTask(ImmutableMap.of("code", "5", "name", "未诊断", "isFinish", "true", "Color", "grey"));
            }
        });
        //构建分页对象
        PageMaster<SlideReportVO> pageMaster = PageMaster.of(list);
        pageMaster.setTotal(page.getTotal());
        return R.ok(pageMaster);
    }


    @Override
    public void jsonExport(List<Long> slideList, Long projectId, Integer status) throws Exception {
        StringBuilder res = new StringBuilder();
        ProjectExt projectExt = projectExtMapper.selectById(projectId);
        if (projectExt == null) {
            throw new Exception(MessageSource.M("NOT_FOND_PROJECT"));
        }
        if (slideList.size() == 0) {
            throw new Exception(MessageSource.M("PLEASE_SELECT_A_SLICE"));
        }
        for (Long slide : slideList) {
            QueryWrapper<Marking> markingQueryWrapper = new QueryWrapper<>();
            markingQueryWrapper.eq("slide_id", slide);
            Integer markingCount = markingMapper.selectCount(markingQueryWrapper);
            if (markingCount > 0) {
                // 将文件生成在本地
                String fileUrl = markingService.slideJsonExport(slide);
                res.append(fileUrl).append("\r\n");
            }
        }
        if (status == 1) {
            try {
                // 清空response
                response.reset();
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                response.setCharacterEncoding(CommonConstant.CHARACTER_SET_UTF8);
                response.setContentType(CommonConstant.CONTENT_TYPE);
                response.setHeader(CommonConstant.HEADER, "attachment;filename=" + URLEncoder.encode(projectExt.getProjectName(), "utf-8") + CommonConstant.FILE_SUFFIX_TXT);
                outputStream.write(res.toString().getBytes());
                // 关闭流
                outputStream.close();
            } catch (Exception e) {
                log.error(MessageSource.M("DOWNLOAD_ERROR"), e);
            }
        }
    }


    // ==================================================

    /**
     * 添加标注切片
     *
     * @param addSlideVO
     * @return
     */
    @Override
    public boolean addAnnoSlidesBatch(AddSlideVO addSlideVO) {
        Long projectId = addSlideVO.getProjectId();
        List<Long> topicIds = addSlideVO.getTopicIds();

        Long reviewRoundId = addSlideVO.getReviewRoundId() != null ? addSlideVO.getReviewRoundId() : 0;
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        Image imageQuery = new Image();
        QueryWrapper queryWrapper = new QueryWrapper<>(imageQuery);
        // 只查可用的图片
        queryWrapper.eq("status", 1);
        queryWrapper.eq("delete_flag", 1);
        queryWrapper.in(CollectionUtils.isNotEmpty(topicIds), "topic_id", topicIds);
        List<Image> imageList = imageMapper.selectList(queryWrapper);

        for (Image imageObj : imageList) {
            // 匹配图片
            QueryWrapper<ImageCsv> csvQueryWrapper = Wrappers.query();
            csvQueryWrapper.eq("image_name", imageObj.getFileName());
            csvQueryWrapper.orderByDesc("id");
            csvQueryWrapper.last("limit 1");

            ImageCsv imageCsv = imageCsvMapper.selectOne(csvQueryWrapper);

            Slide slide = new Slide();
            if (imageCsv != null) {
                BeanUtil.copyProperties(imageCsv, slide);
                slide.setImageCsvId(imageCsv.getId());
            }

            slide.setTopicId(imageObj.getTopicId());
            slide.setProjectId(projectId);
            slide.setImageId(imageObj.getImageId());
            slide.setCreateBy(sysUser.getUserId());
            slide.setCreateTime(new Date());
            slide.setReviewRoundId(reviewRoundId);
            slideMapper.insert(slide);
        }
        return true;
    }

    /**
     * 选片 - 添加切片（新） .
     *
     * @param addSlideIdsVO
     * @return
     */
    @Override
    public boolean addSlidesBatch(AddSlideIdsVO addSlideIdsVO) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        Long projectId = addSlideIdsVO.getProjectId();
        Long reviewRoundId = addSlideIdsVO.getReviewRoundId() != null ? addSlideIdsVO.getReviewRoundId() : 0;

        List<Long> imageIds = addSlideIdsVO.getImageIds();

        Image imageQuery = new Image();
        QueryWrapper queryWrapper = new QueryWrapper<>(imageQuery);
        // 只查可用的图片
        queryWrapper.eq("status", 1);
        queryWrapper.eq("delete_flag", 1);
        queryWrapper.in(CollectionUtils.isNotEmpty(imageIds), "image_id", imageIds);
        List<Image> imageList = imageMapper.selectList(queryWrapper);

        for (Image imageObj : imageList) {
            // 匹配图片
            QueryWrapper<ImageCsv> csvQueryWrapper = Wrappers.query();
            csvQueryWrapper.eq("image_name", imageObj.getFileName());
            csvQueryWrapper.orderByDesc("id");
            csvQueryWrapper.last("limit 1");

            ImageCsv imageCsv = imageCsvMapper.selectOne(csvQueryWrapper);

            Slide slide = new Slide();
            if (imageCsv != null) {
                BeanUtil.copyProperties(imageCsv, slide);
                slide.setImageCsvId(imageCsv.getId());
            }

            slide.setTopicId(imageObj.getTopicId());
            slide.setProjectId(projectId);
            slide.setImageId(imageObj.getImageId());
            slide.setCreateBy(sysUser.getUserId());
            slide.setCreateTime(new Date());
            slide.setReviewRoundId(reviewRoundId);
            slideMapper.insert(slide);
        }
        return true;
    }

    /**
     * 批量删除切片
     *
     * @param slideIds
     * @return 未删除个数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delSlidesBatch(List<Long> slideIds) {
        AtomicInteger count = new AtomicInteger(0);
        for (Long slideId : slideIds) {
            // 匹配图片
            QueryWrapper<Slide> queryWrapper = Wrappers.query();
            queryWrapper.eq("slide_id", slideId);
            queryWrapper.orderByDesc("slide_id");
            queryWrapper.last("limit 1");
            Slide slide = slideMapper.selectOne(queryWrapper);

            if (slide != null && "1".equals(slide.getStatus()) && slideMapper.deleteById(slideId) > 0) {
                updateRecentlyVisited(slideId);
                count.getAndIncrement();
            }
        }
        return slideIds.size() - count.get();
    }


    @Override
    public List<ImageCsvListVO> pageSlides(ImageCsvGetVO request) {

        List<ImageCsvListVO> list = slideMapper.pageImageCsvListVOList(request);
        return list;
    }

    @Override
    public List<ImageCsvListVO> pageSlides1(ImageCsvGetVO request) {

        List<ImageCsvListVO> list = slideMapper.pageImageCsvListVOList1(request);
        return list;
    }


    @Override
    public PageMaster<ImageCsvListVO> pageReviewRoundSSlides(ImageCsvGetPagerVO request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize()).setReasonable(true);
        ImageCsvGetVO imageCsvGetVO = new ImageCsvGetVO();
        BeanUtil.copyProperties(request, imageCsvGetVO);
        List<ImageCsvListVO> list = slideMapper.pageImageCsvListVOList(imageCsvGetVO);
        PageMaster<ImageCsvListVO> pageMaster = new PageMaster<>(list);
        PageHelper.clearPage();
        return pageMaster;
    }

    @Override
    public SlideSelectBy pageImageCsvListVOBy(Long slideId) {
        return slideMapper.pageImageCsvListVOBy(slideId);
    }

    public void updateRecentlyVisited(Long slideId) {
        // 查询出列表
        QueryWrapper<RecentlyVisited> recentlyVisitedQueryWrapper = new QueryWrapper<>();
        recentlyVisitedQueryWrapper.eq("slide_id", slideId);
        List<RecentlyVisited> recentlyVisitedList = recentlyVisitedMapper.selectList(recentlyVisitedQueryWrapper);
        //        // 根据项目和用户查询是否是最后一条，如果是，直接删除，如果不是，查询时间最大的一条数据，将更新时间进行赋值过去
        if (recentlyVisitedList.size() > 0) {
            for (RecentlyVisited recentlyVisited1 : recentlyVisitedList) {
                QueryWrapper<RecentlyVisited> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("project_id", recentlyVisited1.getProjectId()).eq("user_id", recentlyVisited1.getUserId());
                List<RecentlyVisited> recentlyVisiteds = recentlyVisitedMapper.selectList(queryWrapper);

                if (recentlyVisiteds.size() > 1) {
                    QueryWrapper<RecentlyVisited> queryWrapperBy = new QueryWrapper<>();
                    queryWrapperBy
                            .eq("project_id", recentlyVisited1.getProjectId())
                            .eq("user_id", recentlyVisited1.getUserId())
                            .eq("slide_id", recentlyVisited1.getSlideId());
                    // 即将删除的切片信息
                    RecentlyVisited recentlyVisitedMapper1 = recentlyVisitedMapper.selectOne(queryWrapperBy);
                    // 查询当前数据以外创建时间最近的一条数据，并将更新时间进行赋值
                    QueryWrapper<RecentlyVisited> delQueryWrapper = new QueryWrapper<>();
                    delQueryWrapper
                            .eq("project_id", recentlyVisited1.getProjectId())
                            .eq("user_id", recentlyVisited1.getUserId())
                            .ne("slide_id", recentlyVisited1.getSlideId())
                            .orderByDesc("create_time")
                            .last("limit 1");
                    RecentlyVisited recentlyVisited3 = recentlyVisitedMapper.selectOne(delQueryWrapper);
                    recentlyVisited3.setUpdateTime(recentlyVisitedMapper1.getUpdateTime());
                    recentlyVisitedMapper.updateById(recentlyVisited3);
                    // 删除数据
                    recentlyVisitedMapper.delete(queryWrapperBy);
                } else {
                    // 删除
                    QueryWrapper<RecentlyVisited> deleteQueryWrapper = new QueryWrapper<>();
                    deleteQueryWrapper.eq("project_id", recentlyVisited1.getProjectId()).eq("user_id", recentlyVisited1.getUserId()).eq("slide_id", recentlyVisited1.getSlideId());
                    recentlyVisitedMapper.delete(deleteQueryWrapper);
                }
            }
        }
    }




//    /**
//     * 查询专题编号
//     * */
//    @Override
//    public List<TopicIdName>topicList(){
//        Long organizationId=SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
//        return slideMapper.topicList(organizationId);
//    }


}
