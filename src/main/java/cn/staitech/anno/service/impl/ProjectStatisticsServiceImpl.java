package cn.staitech.anno.service.impl;

import cn.staitech.anno.project.constants.Constants;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.mapper.ProjectStatisticsMapper;
import cn.staitech.anno.service.ProjectStatisticsService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.projectstatistics.*;
import cn.staitech.common.core.domain.R;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectStatisticsServiceImpl implements ProjectStatisticsService {

    @Resource
    private ProjectStatisticsMapper projectStatisticsMapper;

    /**
     * 当前项目和标签集
     */
    @Override
    public ProjectInfoOut projectInfor(Long projectId) {
        return projectStatisticsMapper.projectInfor(projectId);
    }


    /**
     * 标注人员
     */
    @Override
    public List<labelingPersonnelOut> labelingPersonnel(Long projectId) {
        return projectStatisticsMapper.labelingPersonnel(projectId);
    }

    /**
     * 标签
     */
    @Override
    public List<LabelOut> label(Long projectId) {
        List<LabelOut> labelOuts = projectStatisticsMapper.label(projectId);
        LabelOut labelOut = LabelOut.builder().categoryName("无属性").categoryId(0L).build();
        labelOuts.add(labelOut);
        return labelOuts;
    }


    /**
     * 多标签统计
     */
    @Override
    public List<ProjectLabelOut> projectLabel(ProjectLabelIn projectLabelIn) {
        if (projectLabelIn.getCategoryIds()==null){
            List<LabelOut> labelOuts = projectStatisticsMapper.label(projectLabelIn.getProjectId());
            LabelOut labelOut = LabelOut.builder().categoryName("无属性").categoryId(0L).build();
            labelOuts.add(labelOut);
            List<Long>categoryIds=labelOuts.stream().map(LabelOut::getCategoryId).collect(Collectors.toList());
            projectLabelIn.setCategoryIds(categoryIds);
        }
        List<ProjectLabelOut> projectLabelOutList=projectStatisticsMapper.labelsNumber(projectLabelIn);
        //根据createBy求标注总数
        Map<Long, DoubleSummaryStatistics> maps = projectLabelOutList.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCreateBy, Collectors.summarizingDouble(ProjectLabelOut::getMarkingNum)));
        List<ProjectLabelOut> projectImageNum=projectStatisticsMapper.labelImageNumber(projectLabelIn.getProjectId());
        //根据createBy求图像总数
//        Map<Long, DoubleSummaryStatistics> mapsImage = projectImageNum.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCreateBy, Collectors.summarizingDouble(ProjectLabelOut::getLabelImageNum)));
        
        //统计当前项目下每个人的标注数量
        List<ProjectLabelOut> imageCountList = projectStatisticsMapper.getLabelImageNumber(projectLabelIn.getProjectId());
       //根据createBy求图像总数
        Map<Long,Integer> mapsImage = imageCountList.stream().collect(Collectors.toMap(ProjectLabelOut::getCreateBy,ProjectLabelOut::getLabelImageNum));

        //根据createBy和categoryId组成的num生成map
        Map<String,ProjectLabelOut> projectLabelOutMap=projectImageNum.stream().collect(Collectors.toMap(ProjectLabelOut::getNum,Function.identity()));
        for (ProjectLabelOut projectLabelOut:projectLabelOutList){
            //当前标签标注图象数量
            projectLabelOut.setLabelImageNum(projectLabelOutMap.get(projectLabelOut.getNum()).getLabelImageNum());
            //标注总数
            projectLabelOut.setMarkingTotal((int)maps.get(projectLabelOut.getCreateBy()).getSum());
            //标注图象总数
            projectLabelOut.setImageNum((int)mapsImage.get(projectLabelOut.getCreateBy()));
        }
        //根据标注总数排序
        List<ProjectLabelOut> labelOuts=projectLabelOutList.stream().sorted(Comparator.comparing(ProjectLabelOut::getMarkingTotal).reversed()).collect(Collectors.toList());
        return labelOuts;
    }

    @Override
    public List<ProjectLabelOut> projectUser(ProjectLabelIn projectLabelIn) {
        List<ProjectLabelOut> projectLabelOutList=projectStatisticsMapper.labelsNumber(projectLabelIn);
        //根据createBy求标注总数
        Map<Long, DoubleSummaryStatistics> maps = projectLabelOutList.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCategoryId, Collectors.summarizingDouble(ProjectLabelOut::getMarkingNum)));
        List<ProjectLabelOut> projectImageNum=projectStatisticsMapper.labelImageNumber(projectLabelIn.getProjectId());
        //根据createBy求图像总数
        Map<Long, DoubleSummaryStatistics> mapsImage = projectImageNum.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCategoryId, Collectors.summarizingDouble(ProjectLabelOut::getLabelImageNum)));
        //根据createBy和categoryId组成的num生成map
        Map<String,ProjectLabelOut> projectLabelOutMap=projectImageNum.stream().collect(Collectors.toMap(ProjectLabelOut::getNum,Function.identity()));
        for (ProjectLabelOut projectLabelOut:projectLabelOutList){
            //当前标签标注图象数量
            projectLabelOut.setLabelImageNum(projectLabelOutMap.get(projectLabelOut.getNum()).getLabelImageNum());
            //标注总数
            projectLabelOut.setMarkingTotal((int)maps.get(projectLabelOut.getCategoryId()).getSum());
            //标注图象总数
            projectLabelOut.setImageNum((int)mapsImage.get(projectLabelOut.getCategoryId()).getSum());
        }
        List<ProjectLabelOut> labelOutList=projectLabelOutList.stream().sorted(Comparator.comparing(ProjectLabelOut::getCategoryId).reversed()).collect(Collectors.toList());
        //根据标注总数排序
        return labelOutList.stream().filter(s -> s.getCategoryId() != 0).sorted(Comparator.comparing(ProjectLabelOut::getMarkingTotal).reversed()).collect(Collectors.toList());
    }




    /**
     * 项目图像
     */
    @Override
    public List<ProjectImageOut> projectImage(Long projectId) {
        return projectStatisticsMapper.projectImage(projectId);
    }


    /**
     * 单图标签统计
     */
    @Override
    public R<PageMaster<ImageLabelOut>> imageLabel(ImageLabelIn imageLabelIn) {
        if (imageLabelIn.getCategoryIds()==null){
            List<LabelOut> labelOuts = projectStatisticsMapper.label(imageLabelIn.getProjectId());
            LabelOut labelOut = LabelOut.builder().categoryName("无属性").categoryId(0L).build();
            labelOuts.add(labelOut);
            List<Long>categoryIds=labelOuts.stream().map(LabelOut::getCategoryId).collect(Collectors.toList());
            imageLabelIn.setCategoryIds(categoryIds);
        }
        PageHelper.startPage(imageLabelIn.getPageNum(), imageLabelIn.getPageSize()).setReasonable(true);
        List<ImageLabelOut> imageLabel = projectStatisticsMapper.imageLabel(imageLabelIn);
        PageMaster<ImageLabelOut> pageMaster = new PageMaster<>(imageLabel);
        return R.ok(pageMaster);
    }


    /**
     * 单图标签统计导出
     */
    @Override
    public void imageLabelExport(ImageLabelIn imageLabelIn, HttpServletResponse response) throws Exception {
        if (imageLabelIn.getCategoryIds()==null){
            List<LabelOut> labelOuts = projectStatisticsMapper.label(imageLabelIn.getProjectId());
            LabelOut labelOut = LabelOut.builder().categoryName("无属性").categoryId(0L).build();
            labelOuts.add(labelOut);
            List<Long>categoryIds=labelOuts.stream().map(LabelOut::getCategoryId).collect(Collectors.toList());
            imageLabelIn.setCategoryIds(categoryIds);
        }
        List<ImageLabelOut> imageLabel = projectStatisticsMapper.imageLabel(imageLabelIn);
        for (ImageLabelOut imageLabelOut : imageLabel) {
            imageLabelOut.setStatusName(Constants.STATUS.get(imageLabelOut.getStatus()));
        }
        // 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = getTitleList(CommonConstant.PROJECT_IN_STATISTICS_KEY, CommonConstant.PROJECT_IN_STATISTICS_VALUE);
        ExcelTool excelTool = new ExcelTool<>(MessageSource.M("EXCEL_FILE_PATH"), 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(String.valueOf(System.currentTimeMillis()), "UTF-8") + CommonConstant.FILE_SUFFIX_XLSX);
        excelTool.exportExcel(titleData, imageLabel, response.getOutputStream(), true, false);
    }

    public List<Map<String, String>> getTitleList(String[] colHeadKey, String[] colHeadValue) {
        // 定义表头
        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < colHeadKey.length; i++) {
            Map<String, String> map = new HashMap<String, String>(1);
            map.put(colHeadKey[i], colHeadValue[i]);
            list.add(map);
        }
        return list;
    }


    /**
     * 多用户统计
     * @param projectLabelIn
     * @param response
     * @throws Exception
     */
    @Override
    public void projectUserExport(ProjectLabelIn projectLabelIn,HttpServletResponse response)throws Exception{
        List<ProjectLabelOut> projectLabelOutList=projectStatisticsMapper.labelsNumber(projectLabelIn);
        //根据createBy求标注总数
        Map<Long, DoubleSummaryStatistics> maps = projectLabelOutList.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCategoryId, Collectors.summarizingDouble(ProjectLabelOut::getMarkingNum)));
        List<ProjectLabelOut> projectImageNum=projectStatisticsMapper.labelImageNumber(projectLabelIn.getProjectId());
        //根据createBy求图像总数
        Map<Long, DoubleSummaryStatistics> mapsImage = projectImageNum.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCategoryId, Collectors.summarizingDouble(ProjectLabelOut::getLabelImageNum)));
        //根据createBy和categoryId组成的num生成map
        Map<String,ProjectLabelOut> projectLabelOutMap=projectImageNum.stream().collect(Collectors.toMap(ProjectLabelOut::getNum,Function.identity()));
        for (ProjectLabelOut projectLabelOut:projectLabelOutList){
            //当前标签标注图象数量
            projectLabelOut.setLabelImageNum(projectLabelOutMap.get(projectLabelOut.getNum()).getLabelImageNum());
            //标注总数
            projectLabelOut.setMarkingTotal((int)maps.get(projectLabelOut.getCategoryId()).getSum());
            //标注图象总数
            projectLabelOut.setImageNum((int)mapsImage.get(projectLabelOut.getCategoryId()).getSum());
        }
        List<ProjectLabelOut> labelOutList=projectLabelOutList.stream().sorted(Comparator.comparing(ProjectLabelOut::getCategoryId).reversed()).collect(Collectors.toList());
        //根据标注总数排序
        List<ProjectLabelOut> labelOuts=labelOutList.stream().filter(s -> s.getCategoryId() != 0).sorted(Comparator.comparing(ProjectLabelOut::getMarkingTotal).reversed()).collect(Collectors.toList());


        Map<Long,List<ProjectLabelOut>> labelOutsMap=labelOuts.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCategoryId));
        List<ProjectLabelOut> labelsOut=new ArrayList<>();
        for (Long key:labelOutsMap.keySet()){
            ProjectLabelOut projectLabelOut=ProjectLabelOut.builder().categoryId(labelOutsMap.get(key).get(0).getCategoryId())
                    .imageNum(labelOutsMap.get(key).get(0).getImageNum()).markingTotal(labelOutsMap.get(key).get(0).getMarkingTotal()).build();
            labelsOut.add(projectLabelOut);
        }

        //创建poi导出数据对象
        SXSSFWorkbook sxssfWorkbook=new SXSSFWorkbook();

        //创建sheet页
        SXSSFSheet sheet=sxssfWorkbook.createSheet("sheet1");
        //创建表头
        SXSSFRow headRow=sheet.createRow(0);

        //设置样式
        CellStyle style = sxssfWorkbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setBorderBottom(BorderStyle.THIN);//下边框

        //设置表头信息
        Cell cellChannelId = headRow.createCell(0);
        cellChannelId.setCellValue("标签");
        cellChannelId.setCellStyle(style);

        Cell cellName = headRow.createCell(1);
        cellName.setCellValue("标签图像总数");
        cellName.setCellStyle(style);

        Cell cellType = headRow.createCell(2);
        cellType.setCellValue("标注人员");
        cellType.setCellStyle(style);

        Cell cellContent= headRow.createCell(3);
        cellContent.setCellValue("标注数量");
        cellContent.setCellStyle(style);

        Cell cellOutUrl = headRow.createCell(4);
        cellOutUrl.setCellValue("标注总数");
        cellOutUrl.setCellStyle(style);

        Map<Long,ProjectLabelOut> outMap=labelsOut.stream().collect(Collectors.toMap(ProjectLabelOut::getCategoryId,Function.identity(),(key1, key2) -> key2));

        Long categoryId=null;
        //定义一个值，标记行数
        int count=1;
        for (ProjectLabelOut projectLabelOut:labelOuts){
            SXSSFRow dataRow=sheet.createRow(count);
            if (!projectLabelOut.getCategoryId().equals(categoryId)){
                ProjectLabelOut labelOut=outMap.get(projectLabelOut.getCategoryId());
                categoryId=projectLabelOut.getCategoryId();
                Cell imageNum = dataRow.createCell(1);
                imageNum.setCellValue(labelOut.getImageNum());
                imageNum.setCellStyle(style);

                Cell markTotal = dataRow.createCell(4);
                markTotal.setCellValue(labelOut.getMarkingTotal());
                markTotal.setCellStyle(style);
            }
            Cell markUser = dataRow.createCell(0);
            markUser.setCellValue(projectLabelOut.getCategoryName());
            markUser.setCellStyle(style);

            Cell labels = dataRow.createCell(2);
            labels.setCellValue(projectLabelOut.getNickName());
            labels.setCellStyle(style);

            Cell labelImageNum = dataRow.createCell(3);
            labelImageNum.setCellValue(projectLabelOut.getMarkingNum());
            labelImageNum.setCellStyle(style);
            count++;
        }

        //筛选出合并行
        int firstRow=1;
        int lastRow;
        //从第二条开始
        Map<Integer, Integer> hbMap=new LinkedHashMap<>();
        for(int i=0;i<labelOuts.size();i++){
            if(i != 0){
            boolean flag = !labelOuts.get(i).getCategoryId().equals(labelOuts.get(i-1).getCategoryId()) || i>=labelOuts.size()-1;
            if(flag){
                //
                if(i!=labelOuts.size() - 1){
                    lastRow=i;
                }else{
                    if (i==1){
                        //i+1是因为前面的表头占了一行
                        lastRow=i+1;
                    }else{
                        lastRow=i;
                    }

                }
                hbMap.put(firstRow,lastRow);
                firstRow=i+1;
            }
        }}
        System.out.println(hbMap);
        //有一行数据的不进行合并
        for(Map.Entry<Integer, Integer> e:hbMap.entrySet()){
            if (Objects.equals(e.getKey(), e.getValue())){
                continue;
            }
            for(int i=0;i<=4;i++){
                if (i==1||i==4){
                    CellRangeAddress region2=new CellRangeAddress(e.getKey(),e.getValue(),i,i);
                    sheet.addMergedRegion(region2);
                    setBorderStyle(BorderStyle.THIN, region2, sheet);
                }
            }
        }
        // 下载导出
        Long filename=System.currentTimeMillis();
        // 设置头信息
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        //一定要设置成xlsx格式
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(filename+".xlsx","UTF-8"));
        //创建一个输出流
        ServletOutputStream outputStream=response.getOutputStream();
        //写入数据
        sxssfWorkbook.write(outputStream);
        // 关闭
        outputStream.close();
        sxssfWorkbook.close();


    }



    /**
     * 多标签统计导出
     * */
    @Override
    public void projectLabelExport(ProjectLabelIn projectLabelIn,HttpServletResponse response)throws Exception{
        if (projectLabelIn.getCategoryIds()==null){
            List<LabelOut> labelOuts = projectStatisticsMapper.label(projectLabelIn.getProjectId());
            LabelOut labelOut = LabelOut.builder().categoryName("无属性").categoryId(0L).build();
            labelOuts.add(labelOut);
            List<Long>categoryIds=labelOuts.stream().map(LabelOut::getCategoryId).collect(Collectors.toList());
            projectLabelIn.setCategoryIds(categoryIds);
        }
        List<ProjectLabelOut> projectLabelOutList=projectStatisticsMapper.labelsNumber(projectLabelIn);
        //根据createBy求标注总数
        Map<Long, DoubleSummaryStatistics> maps = projectLabelOutList.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCreateBy, Collectors.summarizingDouble(ProjectLabelOut::getMarkingNum)));
        List<ProjectLabelOut> projectImageNum=projectStatisticsMapper.labelImageNumber(projectLabelIn.getProjectId());
        //根据createBy求图像总数
//        Map<Long, DoubleSummaryStatistics> mapsImage = projectImageNum.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCreateBy, Collectors.summarizingDouble(ProjectLabelOut::getLabelImageNum)));
        
      //统计当前项目下每个人的标注数量
        List<ProjectLabelOut> imageCountList = projectStatisticsMapper.getLabelImageNumber(projectLabelIn.getProjectId());
       //根据createBy求图像总数
        Map<Long,Integer> mapsImage = imageCountList.stream().collect(Collectors.toMap(ProjectLabelOut::getCreateBy,ProjectLabelOut::getLabelImageNum));

        
        //根据createBy和categoryId组成的num生成map
        Map<String,ProjectLabelOut> projectLabelOutMap=projectImageNum.stream().collect(Collectors.toMap(ProjectLabelOut::getNum,Function.identity()));
        for (ProjectLabelOut projectLabelOut:projectLabelOutList){
            //当前标签标注图象数量
            projectLabelOut.setLabelImageNum(projectLabelOutMap.get(projectLabelOut.getNum()).getLabelImageNum());
            //标注总数
            projectLabelOut.setMarkingTotal((int)maps.get(projectLabelOut.getCreateBy()).getSum());
            //标注图象总数
            projectLabelOut.setImageNum((int)mapsImage.get(projectLabelOut.getCreateBy()));
        }
        //根据标注总数排序
        List<ProjectLabelOut> labelOuts=projectLabelOutList.stream().sorted(Comparator.comparing(ProjectLabelOut::getMarkingTotal).reversed()).collect(Collectors.toList());


        Map<Long,List<ProjectLabelOut>> labelOutsMap=labelOuts.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCreateBy));
        List<ProjectLabelOut> labelsOut=new ArrayList<>();
        for (Long key:labelOutsMap.keySet()){
            ProjectLabelOut projectLabelOut=ProjectLabelOut.builder().createBy(labelOutsMap.get(key).get(0).getCreateBy())
                    .imageNum(labelOutsMap.get(key).get(0).getImageNum()).markingTotal(labelOutsMap.get(key).get(0).getMarkingTotal()).build();
            labelsOut.add(projectLabelOut);
        }

        //创建poi导出数据对象
        SXSSFWorkbook sxssfWorkbook=new SXSSFWorkbook();

        //创建sheet页
        SXSSFSheet sheet=sxssfWorkbook.createSheet("sheet1");
        //创建表头
        SXSSFRow headRow=sheet.createRow(0);

        //设置样式
        CellStyle style = sxssfWorkbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setAlignment(HorizontalAlignment.CENTER); // 设置水平居中对齐
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 设置垂直居中对齐

        //设置表头信息
        Cell cellChannelId = headRow.createCell(0);
        cellChannelId.setCellValue("标注人员");
        cellChannelId.setCellStyle(style);

        Cell cellName = headRow.createCell(1);
        cellName.setCellValue("标注");
        cellName.setCellStyle(style);

        Cell cellType = headRow.createCell(2);
        cellType.setCellValue("当前标签标注图像数量");
        cellType.setCellStyle(style);

        Cell cellContent= headRow.createCell(3);
        cellContent.setCellValue("标注图像总数");
        cellContent.setCellStyle(style);

        Cell cellTpl= headRow.createCell(4);
        cellTpl.setCellValue("标注数量");
        cellTpl.setCellStyle(style);

        Cell cellOutUrl = headRow.createCell(5);
        cellOutUrl.setCellValue("标注总数");
        cellOutUrl.setCellStyle(style);

        Map<Long,ProjectLabelOut> outMap=labelsOut.stream().collect(Collectors.toMap(ProjectLabelOut::getCreateBy,Function.identity(),(key1, key2) -> key2));

        Long createBy=null;
        //定义一个值，标记行数
        int count=1;
        for (ProjectLabelOut projectLabelOut:labelOuts){
            SXSSFRow dataRow=sheet.createRow(count);
            if (!projectLabelOut.getCreateBy().equals(createBy)){
                ProjectLabelOut labelOut=outMap.get(projectLabelOut.getCreateBy());
                createBy=projectLabelOut.getCreateBy();
                Cell imageNum = dataRow.createCell(3);
                imageNum.setCellValue(labelOut.getImageNum());
                imageNum.setCellStyle(style);

                Cell markTotal = dataRow.createCell(5);
                markTotal.setCellValue(labelOut.getMarkingTotal());
                markTotal.setCellStyle(style);
            }
            Cell markUser = dataRow.createCell(0);
            markUser.setCellValue(projectLabelOut.getNickName());
            markUser.setCellStyle(style);

            Cell labels = dataRow.createCell(1);
            labels.setCellValue(projectLabelOut.getCategoryName());
            labels.setCellStyle(style);

            Cell labelImageNum = dataRow.createCell(2);
            labelImageNum.setCellValue(projectLabelOut.getLabelImageNum());
            labelImageNum.setCellStyle(style);

            Cell markNum = dataRow.createCell(4);
            markNum.setCellValue(projectLabelOut.getMarkingNum());
            markNum.setCellStyle(style);

            count++;
        }

        //筛选出合并行
        int firstRow=1;
        int lastRow;
        //从第二条开始
        Map<Integer, Integer> hbMap=new LinkedHashMap<>();
        for(int i=1;i<labelOuts.size();i++){
            boolean flag=!labelOuts.get(i).getCreateBy().equals(labelOuts.get(i-1).getCreateBy())||i>=labelOuts.size()-1;
            if(flag){
                if(i!=labelOuts.size()-1){
                    lastRow=i;
                }else{
                    //i+1是因为前面的表头占了一行
                    lastRow=i+1;
                }
                hbMap.put(firstRow,lastRow);
                firstRow=i+1;
            }
        }
        //有一行数据的不进行合并
        for(Map.Entry<Integer, Integer> e:hbMap.entrySet()){
            for(int i=0;i<=5;i++){
                if (i==3||i==5){
                    CellRangeAddress region2=new CellRangeAddress(e.getKey(),e.getValue(),i,i);
                    sheet.addMergedRegion(region2);
                    setBorderStyle(BorderStyle.THIN, region2, sheet);
                }
            }
        }
        // 下载导出
        Long filename=System.currentTimeMillis();
        // 设置头信息
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        //一定要设置成xlsx格式
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(filename+".xlsx","UTF-8"));
        //创建一个输出流
        ServletOutputStream outputStream=response.getOutputStream();
        //写入数据
        sxssfWorkbook.write(outputStream);
        // 关闭
        outputStream.close();
        sxssfWorkbook.close();


    }


    private void setBorderStyle(BorderStyle borderStyle, CellRangeAddress cellRangeTitle, Sheet sheet){
        RegionUtil.setBorderBottom(borderStyle, cellRangeTitle, sheet);//下边框
        RegionUtil.setBorderLeft(borderStyle, cellRangeTitle, sheet);//左边框
        RegionUtil.setBorderRight(borderStyle, cellRangeTitle, sheet);//右边框
        RegionUtil.setBorderTop(borderStyle, cellRangeTitle, sheet);//上边框
    }


}
