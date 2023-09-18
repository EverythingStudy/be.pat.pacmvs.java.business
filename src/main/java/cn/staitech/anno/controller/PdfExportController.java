package cn.staitech.anno.controller;

import cn.hutool.core.io.FileUtil;
import cn.staitech.anno.domain.Project;
import cn.staitech.anno.domain.vo.ExaminationListVO;
import cn.staitech.anno.domain.vo.ExaminationSelectVO;
import cn.staitech.anno.domain.vo.ProjectListVO;
import cn.staitech.anno.service.ExaminationService;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.utils.PdfFontUtil;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.annotation.RequiresPermissions;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * pdf导出
 */
@Api(value = "pdf导出接口", tags = "pdf导出")
@RestController
@RequestMapping("pdf")
public class PdfExportController {
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ExaminationService reviewService;
    
    //    @Value("${pdfFilePath}")
    //    private String path;
    
    public File pdfAddress() throws Exception {
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        //创建pdf存储路径
        File f0 = new File("/home/pat_saas/Data/pdfFile/");
        if (!f0.exists()) {
            f0.mkdir();
        }
        //创建pdf文件
        File f1 = new File("/home/pat_saas/Data/pdfFile/" + fmt.format(date) + ".pdf");
        return f1;
    }
    
    
    @ApiOperation(value = "项目列表导出pdf")
    @RequiresPermissions("system:project:export")
    @PostMapping(value = "/project")
    public R getinfo(HttpServletResponse response, Project project) throws Exception {
        //字体
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font CONTENT_FONT = new Font(bfChinese, 20, Font.BOLD, BaseColor.BLACK);
        //临时存储地址
        File f1 = pdfAddress();
        //        Date date = new Date();
        //        DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        //        File f1 = new File(path + fmt.format(date) + ".pdf");
        String PDF_SITE = String.valueOf(f1);
        
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_SITE));
        document.open();
        
        //设置表格几列
        PdfPTable dataTable = PdfFontUtil.getPdfPTable01(8, 500);
        //字段名称
        List<String> tableHeadList = new ArrayList<>();
        tableHeadList.add("Serial number");
        tableHeadList.add("ProjectName");
        tableHeadList.add("ProjectId");
        tableHeadList.add("ImageNum");
        tableHeadList.add("MarkType");
        tableHeadList.add("IndicatorsId");
        tableHeadList.add("ManagerId");
        tableHeadList.add("CreateTime");
        
        PdfFontUtil.addTableCell(dataTable, CONTENT_FONT, tableHeadList);
        
        //获取数据库数据
        int num = 1;
        List<ProjectListVO> list = projectService.selectProjectList(project);
        for (ProjectListVO pro : list) {
            List<String> tableData = new ArrayList<>();
            tableData.add(String.valueOf(num));
            tableData.add(pro.getProjectName());
            tableData.add(String.valueOf(pro.getProjectId()));
            tableData.add(String.valueOf(pro.getImageTotal()));
            tableData.add(String.valueOf(pro.getIndicatorId()));
            tableData.add(String.valueOf(pro.getCreateTime()));
            
            PdfFontUtil.addTableCell(dataTable, CONTENT_FONT, tableData);
            num = num + 1;
        }
        
        document.add(dataTable);
        document.newPage();
        document.close();
        writer.close();
        
        //        File file = new File(String.valueOf(pdfAddress()));
        File file = new File(String.valueOf(f1));
        
        response.reset();
        // 设置响应类型
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + "test.pdf");
        
        byte[] readBytes = FileUtil.readBytes(file);
        OutputStream os = response.getOutputStream();
        os.write(readBytes);
        
        f1.delete();
        
        return null;
    }
    
    @ApiOperation(value = "复核图像列表导出pdf")
    @RequiresPermissions("system:ImageReview:list")
    @PostMapping("/list")
    public R list(ExaminationSelectVO imageReview, HttpServletResponse response) throws Exception {
        //字体
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font CONTENT_FONT = new Font(bfChinese, 20, Font.BOLD, BaseColor.BLACK);
        
        //临时存储地址
        File f1 = pdfAddress();
        //        Date date = new Date();
        //        DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        //        File f1 = new File(path + fmt.format(date) + ".pdf");
        String PDF_SITE = String.valueOf(f1);
        
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_SITE));
        document.open();
        
        //设置表格几列
        PdfPTable dataTable = PdfFontUtil.getPdfPTable01(7, 500);
        //字段名称
        List<String> tableHeadList = new ArrayList<>();
        tableHeadList.add("Serial number");
        tableHeadList.add("FileName");
        tableHeadList.add("Imageurl");
        tableHeadList.add("ImageId");
        tableHeadList.add("ProjectName");
        tableHeadList.add("ManualMarking");
        tableHeadList.add("ApprovedStatus");
        
        PdfFontUtil.addTableCell(dataTable, CONTENT_FONT, tableHeadList);
        
        List<ExaminationListVO> list = reviewService.selectExaminationList(imageReview);
        int num2 = 1;
        for (ExaminationListVO pro : list) {
            List<String> tableData = new ArrayList<>();
            tableData.add(String.valueOf(num2));
            tableData.add(pro.getImageName());
            tableData.add(pro.getImageUrl());
            tableData.add(String.valueOf(pro.getImageId()));
            tableData.add(pro.getProjectName());
            tableData.add(String.valueOf(pro.getHumanAnnotationTotal()));
            tableData.add(String.valueOf(pro.getExaminationFlag()));
            
            PdfFontUtil.addTableCell(dataTable, CONTENT_FONT, tableData);
            num2 = num2 + 1;
        }
        
        document.add(dataTable);
        document.newPage();
        document.close();
        writer.close();
        
        File file = new File(String.valueOf(f1));
        
        response.reset();
        // 设置响应类型
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + "test.pdf");
        
        byte[] readBytes = FileUtil.readBytes(file);
        OutputStream os = response.getOutputStream();
        os.write(readBytes);
        
        f1.delete();
        
        return null;
        
        
    }
    
}
