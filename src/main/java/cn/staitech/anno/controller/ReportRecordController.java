package cn.staitech.anno.controller;

import cn.staitech.anno.domain.ReportRecord;
import cn.staitech.anno.domain.vo.ProjectAllVO;
import cn.staitech.anno.domain.vo.reportRecord.*;
import cn.staitech.anno.domain.vo.special.SpecialResVo;
import cn.staitech.anno.enums.ReportRecordEnum;
import cn.staitech.anno.service.GroupService;
import cn.staitech.anno.service.ReportRecordService;
import cn.staitech.anno.service.ReportService;
import cn.staitech.anno.service.SpecialService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresSpecialPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static cn.staitech.anno.aspect.LogFileAspect.response;

/**
 * @Description ：报告
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：ReportController
 * @Author ：zmj
 */
@Slf4j
@Api(tags = "报告管理")
@RestController
@RequestMapping("/report")
public class ReportRecordController {

    @Resource
    private SpecialService specialService;

    @Resource
    private ReportRecordService reportRecordService;

    @Resource
    private GroupService groupService;

    @Resource
    private ReportService reportService;


    /**
     * 报告管理查询专题详情
     */
    @ApiOperation(value = "专题详情")
//    @RequiresPermissions("read-special:exportReport:download")
    @ApiOperationSupport(author = "zmj")
    @ApiImplicitParams({@ApiImplicitParam(name = "specialId", value = "专题id", required = true, dataType = "Long", paramType = "query")})
    @GetMapping("/selectById")
    public R<SpecialResVo> selectById(Long specialId) {
        return R.ok(specialService.selectSpecialId(specialId));
    }

    @ApiOperation(value = "查询专题下的项目")
    @ApiOperationSupport(author = "zmj")
    @PostMapping("/selectProject")
    public R<List<ProjectAllVO>> selectProject(@Validated @RequestBody ReportRecordVO reportRecordVO) {
        ProjectAllVO projectAllVO = ProjectAllVO.builder().specialId(reportRecordVO.getSpecialId()).systemCode(reportRecordVO.getSystemCode()).build();
        List<ProjectAllVO> projectList = reportRecordService.selectProjectList(projectAllVO);
        return R.ok(projectList);
    }

    /*
        @ApiOperation(value = "查询专题下的分组")
        @ApiOperationSupport(author = "zmj")
        @ApiImplicitParams({@ApiImplicitParam(name = "specialId", value = "专题id", required = true, dataType = "Long", paramType = "query")})
        @GetMapping("/selectGroup")
        public R<List<GroupListVO>> selectGroup(Long specialId) {
            Group group = Group.builder().delFlag(0).specialId(specialId).build();
            List<GroupListVO> groups = groupService.selectAllGroup(group);
            return R.ok(groups);
        }
    */
    @ApiOperation(value = "查询切片编号")
    @ApiOperationSupport(author = "zmj")
    @PostMapping("/selectSlide")
    public R<List<ReportRecordExportVO>> selectSlide(@Validated @RequestBody ReportRecordSingleVO recordSingleVO) {
        List<ReportRecordExportVO> recordExportVOS = reportRecordService.selectSlideNumber(recordSingleVO);
        return R.ok(recordExportVOS);
    }


    /**
     * 报告管理添加记录
     *
     * @throws Exception
     */
    @ApiOperation(value = "添加记录")
    @RequiresSpecialPermissions("read-special:exportReport:download")
    @ApiOperationSupport(author = "zmj")
    @Log(title = "下载", menu = "阅片配置", subMenu = "报告管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<String> add(@Validated @RequestBody ReportRecordAddVO recordAddVO) throws Exception {
        float fileSize = 0;
        String path;
        if (recordAddVO.getReportType() == 1) {
            return R.fail("单切片报告不能使用");
        }
        path = reportService.createRpt(recordAddVO);
        System.out.println(path + "：：：路径理解");
        //文件大小
        Long fileLength = new File(path).length();
        System.out.println("文件大小：" + fileLength);
        fileSize = (float) fileLength / 1024;
//        Random random = new Random();
//        int randomNumber = random.nextInt(100) + 1;
//        //随机两位数
//        String formattedNumber = String.format("%02d", randomNumber);
        ReportRecord reportRecord = ReportRecord.builder()
                .reportType(recordAddVO.getReportType())
                .format("word")
                .userId(SecurityUtils.getUserId())
                .specialId(recordAddVO.getSpecialId())
                .reasons(recordAddVO.getReasons())
                .fileSize(String.valueOf(fileSize))
                .reportUrl(path)
                .build();
        if (recordAddVO.getReasons() == 1 && recordAddVO.getReportType() == 2) {
            reportRecord.setFileName("表1组织病理学-病变组间分布 给药期结束安乐死");
        } else if (recordAddVO.getReasons() == 1 && recordAddVO.getReportType() == 3) {
            reportRecord.setFileName("附录1脏器病变表-给药期结束安乐死");
        } else if (recordAddVO.getReasons() == 2 && recordAddVO.getReportType() == 2) {
            reportRecord.setFileName("表2组织病理学-病变组间分布 恢复期结束安乐死");
        } else if (recordAddVO.getReasons() == 2 && recordAddVO.getReportType() == 3) {
            reportRecord.setFileName("附录2脏器病变表-恢复期结束安乐死");
        } else {
            reportRecord.setFileName("单切片报告");
        }
        //添加数据
        reportRecordService.insertSelective(reportRecord);
        //异步更新进度调
        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
            try {
/*                Group group = Group.builder().specialId(recordAddVO.getSpecialId()).reasons(recordAddVO.getReasons()).build();
                List<GroupListVO> groupListVOS = groupService.selectAllGroup(group);
                int groupLong = groupListVOS.size();
                int num = 0;
                for (GroupListVO groupListVO : groupListVOS) {
                    num++;
                    float pace = ((float) num / groupLong) * 100;
                    if (0 <= pace && pace < 50) {
                        ReportRecord record = ReportRecord.builder().reportId(reportRecord.getReportId()).pace(30).build();
                        reportRecordService.updateByPrimaryKeySelective(record);
                    } else if (50 <= pace && pace < 100) {
                        ReportRecord record = ReportRecord.builder().reportId(reportRecord.getReportId()).pace(70).build();
                        reportRecordService.updateByPrimaryKeySelective(record);
                    } else {
                        ReportRecord record = ReportRecord.builder().reportId(reportRecord.getReportId()).pace(100).status(1).build();
                        reportRecordService.updateByPrimaryKeySelective(record);
                    }
                }*/
            } catch (Exception e) {
                ReportRecord record = ReportRecord.builder().reportId(reportRecord.getReportId()).status(2).build();
                reportRecordService.updateByPrimaryKeySelective(record);
                throw new RuntimeException(e);
            }
            return 1;
        });


        return R.ok(null, "添加成功");
    }


    /**
     * 报告管理下载记录查询
     */
    @ApiOperation(value = "查询记录")
    @ApiOperationSupport(author = "zmj")
    @PostMapping("/select")
    public R<PageMaster<ReportRecordAllVO>> getList(@Validated @RequestBody ReportRecordViewVO recordViewVO) {
        PageHelper.startPage(recordViewVO.getPageNum(), recordViewVO.getPageSize()).setReasonable(true);
        List<ReportRecordAllVO> recordAllVOS = reportRecordService.selectByPrimaryKey(recordViewVO);
        for (ReportRecordAllVO recordAllVO : recordAllVOS) {
            recordAllVO.setReportTypeName(ReportRecordEnum.getEnumLabelByValue(recordAllVO.getReportType()));
        }
        PageMaster<ReportRecordAllVO> pageMaster = new PageMaster<>(recordAllVOS);
        return R.ok(pageMaster);
    }


    /**
     * 报告管理删除
     */
    @ApiOperation(value = "删除记录")
    @RequiresSpecialPermissions("read-special:downloadHistory:delete")
    @ApiOperationSupport(author = "zmj")
    @Log(title = "下载记录-删除", menu = "阅片配置", subMenu = "报告管理", businessType = BusinessType.DELETE)
    @GetMapping("/remove")
    public R<String> del(ReportRecordDelVO recordDelVO) {
        ReportRecordAllVO recordAllVO = reportRecordService.selectReport(recordDelVO.getReportId());
        if (!Objects.equals(recordAllVO.getUserId(), SecurityUtils.getUserId())) {
            return R.fail("您无权删除他人的下载任务");
        }
        ReportRecord record = ReportRecord.builder().reportId(recordDelVO.getReportId()).delFlag(1).build();
        Integer num = reportRecordService.updateByPrimaryKeySelective(record);
        if (num < 1) {
            return R.fail("删除失败");
        }
        return R.ok(null, "删除成功");
    }


    /**
     * 报告管理word导出
     */
    @ApiOperation(value = "word下载")
    @RequiresSpecialPermissions("read-special:downloadHistory:download")
    @Log(title = "下载记录-下载", menu = "阅片配置", subMenu = "报告管理", businessType = BusinessType.QUERY)
    @PostMapping(value = "downLoadWord", name = "word下载")
    public void downLoadWord(ReportRecordDelVO recordDelVO) throws Exception {
        ReportRecordAllVO recordAllVO = reportRecordService.selectReport(recordDelVO.getReportId());
//            // path是指欲下载的文件的路径。
        File file = new File(recordAllVO.getReportUrl());
        //获取文件名
        String filename = file.getName();
        //获取后缀名
        int i = filename.lastIndexOf(".");
        String extension = filename.substring(i + 1);

        //设置响应的信息
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf8"));
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        //设置浏览器接受类型为流
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            // 将文件写入输入流
//            byte[] bytes = FileUtils.readFileToByteArray(file);
            //InputStream in = new FileInputStream(file);
            // 将文件写入输入流
            OutputStream out = response.getOutputStream();
            if (file.length() == 0) {
                IOUtils.write("", out);
            } else {
                // 将文件写入输入流
                byte[] bytes = FileUtils.readFileToByteArray(file);
                IOUtils.write(bytes, out);
            }

            /*if("docx".equals(extension) || "doc".equals(extension)) {
                //docx文件就以XWPFDocument创建
                XWPFDocument docx = new XWPFDocument(in);
                docx.write(out);
                docx.close();
            } else {
                //其他类型的文件，按照普通文件传输 如（zip、rar等压缩包）
                int len;
                //一次传输1M大小字节
                byte[] bytes = new byte[1024];
                while ((len = in.read(bytes)) != -1) {
                    out.write(bytes  , 0 , len);
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
