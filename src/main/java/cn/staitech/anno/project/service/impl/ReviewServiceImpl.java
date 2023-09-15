package cn.staitech.anno.project.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.staitech.anno.project.vo.ReviewVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.staitech.anno.project.domain.Review;
import cn.staitech.anno.project.service.ReviewService;
import cn.staitech.anno.project.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}




