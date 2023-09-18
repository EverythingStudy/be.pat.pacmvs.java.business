package cn.staitech.anno.project.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.project.vo.ReviewIN;
import cn.staitech.anno.project.vo.ReviewUP;
import cn.staitech.anno.project.vo.ReviewVO;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.service.ReviewService;
import cn.staitech.anno.project.mapper.ReviewMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
* @author 86186
* @description 针对表【tb_review】的数据库操作Service实现
* @createDate 2023-09-15 13:05:15
*/
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
    implements ReviewService{

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private SlideMapper slideMapper;

    @Override
    public void exportReview(Long projectId,Long slideId)throws Exception{
        Map params = new HashMap();
        if (projectId!= null){
            params.put("projectId",projectId);
        }
        if (slideId!= null){
            params.put("slideId",slideId);
        }
        List<ReviewVO> reviewVOS = getBaseMapper().exportReview(params);

        //通过hutool工具创建的excel的writer，默认为xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        //自定义excel标题和列名
        writer.addHeaderAlias("项目名称","projectName");
        writer.addHeaderAlias("评审内容","content");
        writer.addHeaderAlias("评审轮次","round");
        writer.addHeaderAlias("专题编号","topic");
        writer.addHeaderAlias("组别","group");
        writer.addHeaderAlias("切片编号","imageCode");
        writer.addHeaderAlias("分值","score");
        writer.addHeaderAlias("详情","details");
        writer.addHeaderAlias("评审人","createName");
        writer.addHeaderAlias("评审时间","createTime");
        writer.write(reviewVOS,true);
        httpServletResponse.setContentType("application/vnd.ms-excel;charset=utf-8");
        //name是下载对话框的名称，不支持中文，想用中文名称需要进行utf8编码
        String excelName = "评审结果";
        excelName = URLEncoder.encode(excelName, "utf-8");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + excelName +".xls");
        ServletOutputStream excelOut = null;
        //将excel文件信息写入输出流，返回给调用者
        try {
            excelOut = httpServletResponse.getOutputStream();
            writer.flush(excelOut,true);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
        IoUtil.close(excelOut);
    }


    @Override
    public int insert(ReviewIN req) throws Exception {
        Slide slideBy = slideMapper.selectById(req.getSlideId());
        if(slideBy == null){
            throw new Exception("未查询到切片信息");
        }
        Review reviewBys = reviewMapper.selectOne(Wrappers.query(Review.builder().slideId(req.getSlideId()).createBy(SecurityUtils.getUserId()).build()));
        if(reviewBys != null){
            throw new Exception("您已经进行过评审，禁止重复评审");
        }
        Review reviewBy = reviewMapper.selectSlide(req.getSlideId());
        Review review = new Review();
        review.setCreateName(SecurityUtils.getUsername());
        review.setCreateBy(SecurityUtils.getUserId());
        review.setCreateTime(new Date());
        review.setUpdateBy(SecurityUtils.getUserId());
        BeanUtils.copyProperties(reviewBy, review);
        reviewMapper.insert(review);
        return reviewMapper.insert(review);
    }

    @Override
    public int update(ReviewUP req) throws Exception {
        Review reviewBy = reviewMapper.selectById(req.getReviewId());
        if(reviewBy == null){
            throw new Exception("未查询到评审信息");
        }
        if(!Objects.equals(reviewBy.getCreateBy(), SecurityUtils.getUserId())){
            throw new Exception("不可编辑他人信息");
        }
        Review review = new Review();
        review.setReviewId(req.getReviewId());
        review.setDetails(req.getDetails());
        review.setScore(req.getScore());
        review.setUpdateTime(new Date());
        return reviewMapper.updateById(review);
    }

}




