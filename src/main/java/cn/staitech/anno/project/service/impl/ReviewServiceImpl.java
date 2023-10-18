package cn.staitech.anno.project.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.reviewround.ReviewRoundOutVO;
import cn.staitech.anno.project.constants.Constants;
import cn.staitech.anno.project.domain.DownTask;
import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.mapper.DownTaskMapper;
import cn.staitech.anno.project.mapper.ReviewMapper;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.project.service.ReviewService;
import cn.staitech.anno.project.vo.ReviewIN;
import cn.staitech.anno.project.vo.ReviewRoundIN;
import cn.staitech.anno.project.vo.ReviewUP;
import cn.staitech.anno.project.vo.ReviewVO;
import cn.staitech.anno.service.FileService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ibm.icu.text.SimpleDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @author 86186
 * @description 针对表【tb_review】的数据库操作Service实现
 * @createDate 2023-09-15 13:05:15
 */
@Slf4j
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
        implements ReviewService {

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private SlideMapperV1 slideMapperV1;

    @Resource
    private FileService fileService;

    @Resource
    private DownTaskMapper downTaskMapper;

    private static ExecutorService executor = ExecutorBuilder.create()
            .setCorePoolSize(1)
            .setMaxPoolSize(1)
            .setKeepAliveTime(0)
            .build();

    @Override
    public void exportReview(Long projectId, Long slideId) throws Exception {
        Map params = new HashMap();
        if (projectId != null) {
            params.put("projectId", projectId);
        }
        if (slideId != null) {
            params.put("slideId", slideId);
        }
        List<ReviewVO> reviewVOS = getBaseMapper().exportReview(params);

        //通过hutool工具创建的excel的writer，默认为xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        //自定义excel标题和列名
        writer.addHeaderAlias("项目名称", "projectName");
        writer.addHeaderAlias("评审内容", "content");
        writer.addHeaderAlias("评审轮次", "round");
        writer.addHeaderAlias("专题编号", "topic");
        writer.addHeaderAlias("组别", "group");
        writer.addHeaderAlias("切片编号", "imageCode");
        writer.addHeaderAlias("分值", "score");
        writer.addHeaderAlias("详情", "details");
        writer.addHeaderAlias("评审人", "createName");
        writer.addHeaderAlias("评审时间", "createTime");
        writer.write(reviewVOS, true);
        httpServletResponse.setContentType("application/vnd.ms-excel;charset=utf-8");
        //name是下载对话框的名称，不支持中文，想用中文名称需要进行utf8编码
        String excelName = MessageSource.M("REVIEW_RESULT");
        excelName = URLEncoder.encode(excelName, "utf-8");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".xls");
        ServletOutputStream excelOut = null;
        // 将excel文件信息写入输出流，返回给调用者
        try {
            excelOut = httpServletResponse.getOutputStream();
            writer.flush(excelOut, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        IoUtil.close(excelOut);
    }

    @Transactional
    @Override
    public void csvExportReviewCurrent(Long projectId, List<Long> slideIds) throws Exception {
        String projectName = "";
        ServletOutputStream out = null;
        InputStream inputStream = null;
        String path = File.separator + "temp";
        File file = new File(path);
        try {
            Map params = new HashMap();
            params.put("projectId", projectId);
            params.put("slideIds", slideIds);
            List<ReviewVO> reviewVOS = getBaseMapper().exportReview(params);
            if (reviewVOS != null && !reviewVOS.isEmpty()) {
                CsvWriter writer = CsvUtil.getWriter(file, CharsetUtil.CHARSET_UTF_8);
                String[] header = new String[]{"项目名称", "评审内容", "评审轮次", "专题编号", "组别", "切片编号", "分值", "详情", "评审人", "评审时间"};
                writer.write(header);
                for (ReviewVO reviewVO : reviewVOS) {
                    projectName = reviewVO.getProjectName();
                    String[] body = new String[]{reviewVO.getProjectName(), reviewVO.getContent(), reviewVO.getRoundName(), reviewVO.getTopicName(),
                            reviewVO.getGroupName(), reviewVO.getImageCode(), String.valueOf(reviewVO.getScore()), reviewVO.getDetails() + "\t",
                            reviewVO.getCreateName(), DateUtil.format(reviewVO.getCreateTime(), "yyyy-MM-dd hh:mm:ss") + "\t"};
                    writer.write(body);
                }
                writer.flush();
                writer.close();
                inputStream = new FileInputStream(file);
                httpServletResponse.setContentType("text/csv;charset=utf-8");
                //name是下载对话框的名称，不支持中文，想用中文名称需要进行utf8编码
                String excelName = projectName.concat(MessageSource.M("REVIEW_RESULT"));
                excelName = URLEncoder.encode(excelName, "utf-8");
                httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".csv");
                httpServletResponse.setHeader("responseType", "blob");
                //将excel文件信息写入输出流，返回给调用者
                out = httpServletResponse.getOutputStream();
                out.write(IOUtils.toByteArray(inputStream));
                IoUtil.close(out);
                IoUtil.close(inputStream);
                file.delete();
            }
        } catch (Exception e) {
            log.error("切片projectId:{}；评审导出异常：{}", projectId, e.getMessage());
            IoUtil.close(out);
            IoUtil.close(inputStream);
            file.delete();
        } finally {
            IoUtil.close(out);
            IoUtil.close(inputStream);
            file.delete();
        }
    }

    @Transactional
    @Override
    public DownTask csvExportReview(Long projectId, List<Long> slideIds) {
        Snowflake snowflake = new Snowflake();
        Long userId = SecurityUtils.getUserId();
        DownTask task = DownTask.builder().code(snowflake.nextIdStr()).status(Constants.DOWN_STATE_RUNNING).createTime(new Date()).updateTime(new Date()).updateBy(userId).createBy(userId).build();
        downTaskMapper.insert(task);
        executor.submit(new TaskThread(task, projectId, slideIds));
        return task;
    }

    public class TaskThread implements Runnable {
        public Logger logger = LoggerFactory.getLogger(TaskThread.class);
        private DownTask downTask;
        private Long projectId;
        private List<Long> slideIds;

        public TaskThread(DownTask downTask, Long projectId, List<Long> slideIds) {
            this.downTask = downTask;
            this.projectId = projectId;
            this.slideIds = slideIds;
        }

        @Override
        public void run() {
            try {
                String projectName = "";
                JSONObject jsonObject = new JSONObject();
                Map<String, String> map = new HashMap<>();
                Map params = new HashMap();
                if (projectId != null) {
                    params.put("projectId", projectId);
                }
                if (slideIds == null || slideIds.isEmpty()) {
                    QueryWrapper<Slide> queryWrapper = Wrappers.query();
                    queryWrapper.eq("project_id", projectId);
                    queryWrapper.select("slide_id");
                    List<Slide> slideList = slideMapperV1.selectList(queryWrapper);
                    slideIds = new ArrayList<>();
                    slideList.forEach(slide -> {
                        slideIds.add(slide.getSlideId());
                    });
                }
                if (slideIds != null && !slideIds.isEmpty()) {
                    for (Long slideId : slideIds) {
                        try {
                            params.put("slideId", slideId);
                            List<ReviewVO> reviewVOS = getBaseMapper().exportReview(params);
                            String path = fileService.createFiles(slideId, ".csv");
                            File file = new File(path);
                            CsvWriter writer = CsvUtil.getWriter(file, CharsetUtil.CHARSET_UTF_8);
                            String[] header = new String[]{"项目名称", "评审内容", "评审轮次", "专题编号", "组别", "切片编号", "分值", "详情", "评审人", "评审时间"};
                            writer.write(header);
                            for (ReviewVO reviewVO : reviewVOS) {
                                projectName = reviewVO.getProjectName();
                                String[] body = new String[]{reviewVO.getProjectName(), reviewVO.getContent(), reviewVO.getRoundName(), reviewVO.getTopicName(),
                                        reviewVO.getGroupName(), reviewVO.getImageCode(), String.valueOf(reviewVO.getScore()), reviewVO.getDetails(),
                                        reviewVO.getCreateName(), DateUtil.format(reviewVO.getCreateTime(), "yyyy-MM-dd hh24:mm:ss")};
                                writer.write(body);
                            }
                            map.put(CommonConstant.PATH, path);
                            jsonObject.put(String.valueOf(slideId), map);
                            writer.flush();
                            writer.close();
                        } catch (Exception e) {
                            logger.error("切片slideId:{}；评审导出异常：{}", slideId, e.getMessage());
                            continue;
                        }
                    }
                }
                downTask.setProjectName(projectName);
                downTask.setProjectId(projectId);
                downTask.setPath(jsonObject);
                downTask.setStatus(Constants.DOWN_STATE_FINISH);
                int res = downTaskMapper.updateById(downTask);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }


    @Override
    public int insert(ReviewIN req) throws Exception {
        Slide slideBy = slideMapperV1.selectById(req.getSlideId());
        if (slideBy == null) {
            throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
        }
        Review reviewBys = reviewMapper.selectOne(Wrappers.query(Review.builder().slideId(req.getSlideId()).createBy(SecurityUtils.getUserId()).build()));
        if (reviewBys != null) {
            throw new Exception(MessageSource.M("RE_REVIEW_ERROR"));
        }
        Review reviewBy = reviewMapper.selectSlide(req.getSlideId());
        Review review = new Review();
        BeanUtils.copyProperties(reviewBy, review);
        review.setCreateName(SecurityUtils.getUsername());
        review.setCreateBy(SecurityUtils.getUserId());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        review.setCreateTime(sdf.format(date));
        review.setReviewPeople(SecurityUtils.getUsername());
        review.setReviewTime(new Date());
        review.setUpdateBy(SecurityUtils.getUserId());
        review.setScore(req.getScore());
        review.setDetails(req.getDetails());
        return reviewMapper.insert(review);
    }

    @Override
    public int update(ReviewUP req) throws Exception {
        Review reviewBy = reviewMapper.selectById(req.getReviewId());
        if (reviewBy == null) {
            throw new Exception(MessageSource.M("NO_REVIEW_DATA"));
        }
        if (!Objects.equals(reviewBy.getCreateBy(), SecurityUtils.getUserId())) {
            throw new Exception(MessageSource.M("FORBID_EDIT_OTHERS_INFO"));
        }
        Review review = new Review();
        review.setReviewId(req.getReviewId());
        review.setDetails(req.getDetails());
        review.setScore(req.getScore());
        review.setUpdateBy(SecurityUtils.getUserId());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        review.setUpdateTime(sdf.format(date));
        return reviewMapper.updateById(review);
    }

    @Override
    public PageMaster<ReviewRoundOutVO> pageReviewRound(Page page, ReviewRoundIN params) {
        getBaseMapper().pageReviewRound(page, params);
        PageMaster<ReviewRoundOutVO> pageMaster = PageMaster.of(page.getRecords());
        pageMaster.setTotal(page.getTotal());
        return pageMaster;
    }
}
