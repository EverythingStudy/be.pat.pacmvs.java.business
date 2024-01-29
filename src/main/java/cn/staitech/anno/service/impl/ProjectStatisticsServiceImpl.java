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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;


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
     * 项目标签统计
     */
    public List<ProjectLabelOut> projectLabel(ProjectLabelIn projectLabelIn) {
        //查询当前项目下的所有标签
        List<LabelOut> labelOuts = projectStatisticsMapper.label(projectLabelIn.getProjectId());
        LabelOut labelOut = LabelOut.builder().categoryName("无属性").categoryId(0L).build();
        labelOuts.add(labelOut);

        //查询跟项目关联的标签集
        ProjectInfoOut projectInfoOut = projectStatisticsMapper.projectInfor(projectLabelIn.getProjectId());
        projectLabelIn.setIndicatorId(projectInfoOut.getIndicatorId());
        //查询项目下的标签统计信息
        List<ProjectLabelOut> projectLabelOuts = projectStatisticsMapper.projectLabel(projectLabelIn);

        //根据slideId和createBy组成的num2进行分组
        Map<String, List<ProjectLabelOut>> num2group = projectLabelOuts.stream().collect(Collectors.groupingBy(ProjectLabelOut::getNum2));
        //标注图像总数
        Map<String, Set<Long>> setMap = new HashMap<>();
        Iterator<String> iterator = num2group.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            List<ProjectLabelOut> values = num2group.get(key);
            List<Long> longSet = values.stream().map(ProjectLabelOut::getSlideId).collect(Collectors.toList());
            Set<Long> longSet1 = new HashSet<>(longSet);
            setMap.put(key, longSet1);
        }

        List<ProjectLabelOut> projectLabelOutList=labels(projectLabelOuts,setMap);
        return projectLabelOutList;

    }

    /**
     * 多标签统计
     * */
    //查询项目下的标签统计信息，标注图像总数
    public List<ProjectLabelOut> labels(List<ProjectLabelOut> projectLabelOuts,Map<String, Set<Long>> setMap) {
        List<ProjectLabelOut> projectLabelOutList=new ArrayList<>();
        //根据createBy进行分组
        Map<Long, List<ProjectLabelOut>> createByMap = projectLabelOuts.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCreateBy));
        //根据createBy求标注总数
        Map<Long, DoubleSummaryStatistics> map = projectLabelOuts.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCreateBy, Collectors.summarizingDouble(ProjectLabelOut::getMarkingNum)));
        //根据标签和createBy组成的num进行分组
        Map<String, DoubleSummaryStatistics> Maps = projectLabelOuts.stream().collect(Collectors.groupingBy(ProjectLabelOut::getNum, Collectors.summarizingDouble(ProjectLabelOut::getSlideId)));
        Iterator<Long> createByMaps=createByMap.keySet().iterator();
        while (createByMaps.hasNext()){
           Long entry=createByMaps.next();
            String labelImageNum="";
            for (ProjectLabelOut projectLabelOut1:createByMap.get(entry)){
                labelImageNum+=projectLabelOut1.getCategoryName()+"/"+Maps.get(projectLabelOut1.getNum()).getCount()+"/"+projectLabelOut1.getMarkingNum()+",";
            }
            ProjectLabelOut projectLabelOut=ProjectLabelOut.builder().markingTotal((int)map.get(entry).getSum()).imageNum(setMap.get(createByMap.get(entry).get(0).getNum2()).size())
                    .nickName(createByMap.get(entry).get(0).getNickName()).labelImageNums(labelImageNum).build();
            projectLabelOutList.add(projectLabelOut);
        }
        projectLabelOutList.stream().sorted(Comparator.comparing(ProjectLabelOut::getMarkingTotal).reversed()).collect(Collectors.toList());
        return projectLabelOutList;
    }


    /**
     * 多用户统计
     * */
    public List<ProjectLabelOut> users(List<ProjectLabelOut> projectLabelOuts){
        List<ProjectLabelOut> projectLabelOutList=new ArrayList<>();
        Map<Long, List<ProjectLabelOut>> listMap=projectLabelOuts.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCategoryId));
        //根据标签和slideId组成的num3进行分组
        Map<Long,List<ProjectLabelOut>> maps = projectLabelOuts.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCategoryId));
        Map<Long,Set<Long>> slideIdMaps=categoryImage(maps);
        //根据categoryId求标注总数
        Map<Long, DoubleSummaryStatistics> map = projectLabelOuts.stream().collect(Collectors.groupingBy(ProjectLabelOut::getCategoryId, Collectors.summarizingDouble(ProjectLabelOut::getMarkingNum)));
        Iterator<Long> longIterator=listMap.keySet().iterator();
        while (longIterator.hasNext()){
            Long entry=longIterator.next();
            String markingManNum="";
            for (ProjectLabelOut projectLabelOut:listMap.get(entry)){
                markingManNum+=projectLabelOut.getNickName()+"/"+projectLabelOut.getMarkingNum()+",";
            }
            ProjectLabelOut projectLabelOut=ProjectLabelOut.builder().categoryName(listMap.get(entry).get(0).getCategoryName()).imageNum(slideIdMaps.get(entry).size())
                    .markingTotal((int)map.get(entry).getSum()).labelImageNums(markingManNum).build();
            projectLabelOutList.add(projectLabelOut);
        }
        projectLabelOutList.stream().sorted(Comparator.comparing(ProjectLabelOut::getMarkingTotal).reversed()).collect(Collectors.toList());
        return projectLabelOutList;
    }

    /**
     * 筛选出每个标签的图像总数
     * */
    public Map<Long,Set<Long>> categoryImage(Map<Long,List<ProjectLabelOut>> maps){
        Iterator<Long> entry=maps.keySet().iterator();
        Map<Long,Set<Long>> slideIdMaps=new HashMap<>();
        while (entry.hasNext()){
            Long categoryId=entry.next();
            List<ProjectLabelOut> projectLabelOuts=maps.get(categoryId);
            List<Long>slideIds=projectLabelOuts.stream().map(ProjectLabelOut::getSlideId).collect(Collectors.toList());
            Set<Long> slideIdList=new HashSet<>(slideIds);
            slideIdMaps.put(categoryId,slideIdList);
        }
        return slideIdMaps;
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
        if (imageLabelIn.getUserIds().isEmpty()){
            //查询当前项目下的所有成员id
            List<labelingPersonnelOut> list=projectStatisticsMapper.labelingPersonnel(imageLabelIn.getProjectId());
            List<Long>userIds=list.stream().map(labelingPersonnelOut::getUserId).collect(Collectors.toList());
            imageLabelIn.setUserIds(userIds);
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
        if (imageLabelIn.getUserIds().isEmpty()){
            //查询当前项目下的所有成员id
            List<labelingPersonnelOut> list=projectStatisticsMapper.labelingPersonnel(imageLabelIn.getProjectId());
            List<Long>userIds=list.stream().map(labelingPersonnelOut::getUserId).collect(Collectors.toList());
            imageLabelIn.setUserIds(userIds);
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


}
