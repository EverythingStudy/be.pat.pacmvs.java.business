package cn.staitech.anno.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.staitech.anno.constant.ExaminationConstant;
import cn.staitech.anno.constant.ProjectConstant;
import cn.staitech.anno.constant.R.MeasureResponseConstant;
import cn.staitech.anno.domain.*;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.po.ProjectPo;
import cn.staitech.anno.domain.project.ProjectExt;
import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.vo.ExaminationListVO;
import cn.staitech.anno.domain.vo.ProjectListOutVO;
import cn.staitech.anno.domain.vo.SlideSelectVO;
import cn.staitech.anno.domain.vo.image.ProjectStatisticsVo;
import cn.staitech.anno.domain.vo.image.SlideReportSummaryVo;
import cn.staitech.anno.domain.vo.image.SlideReportVo;
import cn.staitech.anno.domain.vo.imageCsv.ImageCsvGetPagerVO;
import cn.staitech.anno.domain.vo.imageCsv.ImageCsvGetVO;
import cn.staitech.anno.domain.vo.imageCsv.ImageCsvListVO;
import cn.staitech.anno.domain.vo.statistic.StatisticSlideListInVO;
import cn.staitech.anno.domain.vo.statistic.StatisticSlideListOutVO;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.PageMaster;
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

import static cn.staitech.anno.aspect.LogFileAspect.response;

/**
 * 切片 服务层实现
 *
 * @author staitech
 */
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

//    @SuppressWarnings("checkstyle:WhitespaceAfter")

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
                throw new Exception(ProjectConstant.SERIAL_NO + slideId + ProjectConstant.IN_DIMENSION);
            } else if (processFlag == 2) {
                throw new Exception(ProjectConstant.SERIAL_NO + slideId + ProjectConstant.DIMENSIONING_COMPLETE);
            } else if (processFlag == 3) {
                throw new Exception(ProjectConstant.SERIAL_NO + slideId + ProjectConstant.SUBMITTED_FOR_REVIEW);
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
    public R<SlideReportSummaryVo> querySlideByProjectAndGroup(Map params) {
        SlideReportSummaryVo vo = getBaseMapper().selectSlideByProjectAndGroup(params);
        if (vo != null && vo.getFinishTotal() == null) {
            vo.setFinishTotal(0);
        } else {
            vo = new SlideReportSummaryVo();
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
    public R<PageMaster<SlideReportVo>> pageSlideWithSubImage(Map params) {
        Page<SlideReportVo> page = new Page<>(MapUtils.getInteger(params, "pageNum", 1), MapUtils.getInteger(params, "pageSize", 10));
        getBaseMapper().pageSlideWithSubImage(page, params);
        List<SlideReportVo> list = page.getRecords();
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
        PageMaster<SlideReportVo> pageMaster = PageMaster.of(list);
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
    public R<PageMaster<ProjectStatisticsVo>> pageSlideStatisticsByProject(Map params) {
        Page<ProjectStatisticsVo> page = new Page<>(MapUtils.getInteger(params, "pageNum", 1), MapUtils.getInteger(params, "pageSize", 10));
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
        PageMaster<ProjectStatisticsVo> pageMaster = PageMaster.of(page.getRecords());
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
    public R<PageMaster<SlideReportVo>> pageSlideStatistics(Map params) {

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
        Page<SlideReportVo> page = new Page<>(MapUtils.getInteger(params, "pageNum", 1), MapUtils.getInteger(params, "pageSize", 10));
        params.put("projectIds", projectIds);
        getBaseMapper().pageSlideStatistics(page, params);
        List<SlideReportVo> list = page.getRecords();
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
        PageMaster<SlideReportVo> pageMaster = PageMaster.of(list);
        pageMaster.setTotal(page.getTotal());
        return R.ok(pageMaster);
    }


    @Override
    public void jsonExport(List<Long> slideList, Long projectId, Integer status) throws Exception {
        StringBuilder res = new StringBuilder();
        ProjectExt projectExt = projectExtMapper.selectById(projectId);
        if (projectExt == null) {
            throw new Exception("未发现项目信息");
        }
        if (slideList.size() == 0) {
            throw new Exception("请选择切片");
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
                response.setCharacterEncoding(ExaminationConstant.CHARACTER_ENCODING);
                response.setContentType(ExaminationConstant.CONTENT_TYPE);
                response.setHeader(ExaminationConstant.HEADER, "attachment;filename=" + URLEncoder.encode(projectExt.getProjectName(), "utf-8") + MeasureResponseConstant.FILE_SUFFIX_TXT);
                outputStream.write(res.toString().getBytes());
                // 关闭流
                outputStream.close();
            } catch (Exception e) {
                log.error(MeasureResponseConstant.DOWNLOAD_ERROR, e);
            }
        }
    }


    // =========================

    /**
     * 添加标注切片
     *
     * @param projectId
     * @param topicIds
     * @return
     */
    @Override
    public boolean addAnnoSlidesBatch(Long projectId, List<Long> topicIds) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        QueryWrapper<ImageCsv> query = Wrappers.query();
        query.in("topic_id", topicIds);
        List<ImageCsv> imageCsvList = imageCsvMapper.selectList(query);
        // List<Slide> slideList = new ArrayList<>(imageCsvList.size());
        for (ImageCsv imageCsv : imageCsvList) {

            // 匹配图片
            QueryWrapper<Image> imageQueryWrapper = Wrappers.query();
            imageQueryWrapper.eq("file_name", imageCsv.getImageName());
            imageQueryWrapper.eq("topic_id", imageCsv.getTopicId());
            imageQueryWrapper.eq("organization_id", sysUser.getOrganizationId());
            imageQueryWrapper.eq("status", 1);
            imageQueryWrapper.eq("delete_flag", 1);

            imageQueryWrapper.orderByDesc("image_id");
            Image image = imageMapper.selectOne(imageQueryWrapper);

            if (image != null) {
                Slide slide = new Slide();
                slide.setProjectId(projectId);
                slide.setImageId(image.getImageId());

                slide.setImageCsvId(imageCsv.getId());
                slide.setCreateBy(sysUser.getUserId());
                slide.setCreateTime(new Date());

                slideMapper.insert(slide);
                // slideList.add(slide);
            }
        }

        // slideMapper.insertSlide(slideList);

        return true;
    }


    @Override
    public int delSlidesBatch(List<Long> slideIds) {
        return slideMapper.deleteBatchIds(slideIds);
    }


    @Override
    public List<ImageCsvListVO> pageSlides(ImageCsvGetVO request) {

        List<ImageCsvListVO> list = slideMapper.pageImageCsvListVOList(request);


/*

        ReviewRound reviewRound = new ReviewRound();
        reviewRound.setProjectId(projectId);
        QueryWrapper queryWrapper = new QueryWrapper<>(reviewRound);
        List<ReviewRound> list = this.list(queryWrapper);
        PageMaster pageMaster = new PageMaster<>(list);

        List<ReviewRoundOutVO> respList = new ArrayList<>(list.size());

        Map<Long, String> topicMap = topicService.selectMap();

        for (ReviewRound round : list) {
            ReviewRoundOutVO reviewRoundOutVO = new ReviewRoundOutVO();
            BeanUtils.copyProperties(round, reviewRoundOutVO);

            // 评审轮次
            reviewRoundOutVO.setRoundName(MapConstant.getRoundName(round.getRoundId()));
            // 组别
            reviewRoundOutVO.setGroupName(MapConstant.getGroupName(round.getGroupId()));
            //专题编号
            if (topicMap.containsKey(round.getTopicId())) {
                reviewRoundOutVO.setTopicName(topicMap.get(round.getTopicId()));
            }
            // 创建者
            reviewRoundOutVO.setCreateByName(sysUserService.selectUserById(round.getCreateBy()).getUserName());
            respList.add(reviewRoundOutVO);
        }*/

        return list;
    }


    @Override
    public PageMaster<ImageCsvListVO> pageReviewRoundSSlides(ImageCsvGetPagerVO request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize()).setReasonable(true);
        ImageCsvGetVO imageCsvGetVO = new ImageCsvGetVO();
        BeanUtil.copyProperties(request, imageCsvGetVO);
        List<ImageCsvListVO> list = slideMapper.pageImageCsvListVOList(imageCsvGetVO);
        PageMaster<ImageCsvListVO> pageMaster = new PageMaster<>(list);


/*

        ReviewRound reviewRound = new ReviewRound();
        reviewRound.setProjectId(projectId);
        QueryWrapper queryWrapper = new QueryWrapper<>(reviewRound);
        List<ReviewRound> list = this.list(queryWrapper);
        PageMaster pageMaster = new PageMaster<>(list);

        List<ReviewRoundOutVO> respList = new ArrayList<>(list.size());

        Map<Long, String> topicMap = topicService.selectMap();

        for (ReviewRound round : list) {
            ReviewRoundOutVO reviewRoundOutVO = new ReviewRoundOutVO();
            BeanUtils.copyProperties(round, reviewRoundOutVO);

            // 评审轮次
            reviewRoundOutVO.setRoundName(MapConstant.getRoundName(round.getRoundId()));
            // 组别
            reviewRoundOutVO.setGroupName(MapConstant.getGroupName(round.getGroupId()));
            //专题编号
            if (topicMap.containsKey(round.getTopicId())) {
                reviewRoundOutVO.setTopicName(topicMap.get(round.getTopicId()));
            }
            // 创建者
            reviewRoundOutVO.setCreateByName(sysUserService.selectUserById(round.getCreateBy()).getUserName());
            respList.add(reviewRoundOutVO);
        }*/

        // pageMaster.setList(respList);
        PageHelper.clearPage();
        return pageMaster;
    }
}
