package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.constant.Container;
import cn.staitech.anno.mapper.LabelStatisticsMapper;
import cn.staitech.anno.service.LabelStatisticsService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.labelprojectstatistics.*;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LabelStatisticsServiceImpl implements LabelStatisticsService {

    @Resource
    private LabelStatisticsMapper labelStatisticsMapper;

    /**
     * 获取项目列表
     */
    @Override
    public List<ProjectListOut> projectList(ImageMarkingIn imageMarkingIn) {
        ProjectInVO projectInVO = ProjectInVO.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).projectType("1").statusList(imageMarkingIn.getStatusList()).build();
        return labelStatisticsMapper.projectList(projectInVO);
    }

    /**
     * 查询标准集
     */
    @Override
    public List<LabelSetOut> projectLabelSet(LabelSetIn labelSetIn) {
        //没有项目参数时
        if (CollectionUtils.isNotEmpty(labelSetIn.getProjectIds())) {
            return labelStatisticsMapper.projectLabelSet(labelSetIn);
        }
        labelSetIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        labelSetIn.setUserId(SecurityUtils.getUserId());
        return labelStatisticsMapper.projectLabelSet(labelSetIn);
    }


    /**
     * 查询标签
     */
    @Override
    public List<LabelOut> labelList(LabelIn labelIn) {
        //无属性标签
        LabelOut labelOut = LabelOut.builder().categoryId(0L).categoryName(MessageSource.M("NO_ATTRIBUTE")).build();
        //只传项目id
        if (CollectionUtils.isNotEmpty(labelIn.getProjectIds()) && labelIn.getIndicatorIds().isEmpty()) {
            List<LabelOut> labelOuts = labelStatisticsMapper.labelList(labelIn);
            labelOuts.add(labelOut);
            return labelOuts;
        }
        //传项目id和标签集id 或只传标签集id
        if (CollectionUtils.isNotEmpty(labelIn.getIndicatorIds())) {
            labelIn.setProjectIds(null);
            List<LabelOut> labelOuts = labelStatisticsMapper.labelList(labelIn);
            labelOuts.add(labelOut);
            return labelOuts;
        }
        //不传项目id和标签集id
        labelIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        labelIn.setUserId(SecurityUtils.getUserId());
        List<LabelOut> labelOuts = labelStatisticsMapper.labelList(labelIn);
        labelOuts.add(labelOut);
        return labelOuts;
    }


    /**
     * 查询项目标签
     */
    @Override
    public R<PageMaster<ProjectLabelOut>> projectLabelList(ProjectLabelIn projectLabelIn) {
        projectLabelIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        projectLabelIn.setUserId(SecurityUtils.getUserId());
        ProjectInVO projectInVO = ProjectInVO.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).projectType("1").build();
        List<ProjectListOut> projectList = labelStatisticsMapper.projectList(projectInVO);
        //项目id列表
        List<Long> projectIdList = projectList.stream().map(ProjectListOut::getProjectId).collect(Collectors.toList());
        LabelIn labelIn = LabelIn.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).build();
        List<LabelOut> labelOuts = labelStatisticsMapper.labelList(labelIn);
        //标签id列表
        List<Long> categoryIdList = labelOuts.stream().map(LabelOut::getCategoryId).collect(Collectors.toList());
        categoryIdList.add(0L);

        PageHelper.startPage(projectLabelIn.getPageNum(), projectLabelIn.getPageSize()).setReasonable(true);
        List<ProjectLabelOut> projectLabelOuts = labelStatisticsMapper.projectLabelList(projectLabelIn);
        //查询标注数
        ImageMarkingIn imageMarkingIn = ImageMarkingIn.builder().projectIds(projectIdList).categoryIds(categoryIdList).build();
        List<ImageMarkingOut> markingNums = labelStatisticsMapper.markingNums(imageMarkingIn);
        //查询图像数量
        List<ImageMarkingOut> imageNums = labelStatisticsMapper.imageNums(imageMarkingIn);
        for (ProjectLabelOut projectLabelOut : projectLabelOuts) {
            projectLabelOut.setMarkingNum("0");
            projectLabelOut.setImageNum("0");
            //添加标注数量
            for (ImageMarkingOut marking : markingNums) {
                if (projectLabelOut.getProjectId().equals(marking.getProjectId()) && projectLabelOut.getCategoryId().equals(marking.getCategoryId())) {
                    projectLabelOut.setMarkingNum(marking.getMarkingNum().toString());
                }
            }
            //添加图像数量
            for (ImageMarkingOut markingOut : imageNums) {
                if (projectLabelOut.getProjectId().equals(markingOut.getProjectId()) && projectLabelOut.getCategoryId().equals(markingOut.getCategoryId())) {
                    projectLabelOut.setImageNum(markingOut.getImageNum().toString());
                }
            }
        }
        PageMaster<ProjectLabelOut> pageMaster = new PageMaster<>(projectLabelOuts);
        return R.ok(pageMaster);
    }


    /**
     * 项目创建者
     */
    @Override
    public List<ProjectCreateByOut> userList(LabelSetIn labelSetIn) {
        //有项目信息的查询项目创建者信息
        if (CollectionUtils.isNotEmpty(labelSetIn.getProjectIds())) {
            return labelStatisticsMapper.userList(labelSetIn);
        }
        //若未选项目，则下拉框包含该机构下所有用户
        labelSetIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        labelSetIn.setCurrentUserId(SecurityUtils.getLoginUser().getSysUser().getUserId());
        return labelStatisticsMapper.userList(labelSetIn);
    }


    /**
     * 查询项目
     */
    @Override
    public R<PageMaster<ProjectLabelOut>> itemList(ProjectListIn projectListIn) {
        PageHelper.startPage(projectListIn.getPageNum(), projectListIn.getPageSize()).setReasonable(true);
        projectListIn.setProjectType("1");
        projectListIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        projectListIn.setUsers(SecurityUtils.getUserId());
        List<ProjectLabelOut> itemList = labelStatisticsMapper.itemList(projectListIn);
        ProjectInVO projectInVO = ProjectInVO.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).projectType("1").build();
        List<ProjectListOut> projectList = labelStatisticsMapper.projectList(projectInVO);
        //项目id列表
        List<Long> projectIdList = projectList.stream().map(ProjectListOut::getProjectId).collect(Collectors.toList());
        //
        ImageMarkingIn imageMarkingIn = ImageMarkingIn.builder().projectIds(projectIdList).build();
        List<ImageMarkingOut> marking = labelStatisticsMapper.projectMarking(imageMarkingIn);
        //循环添加图像数量和标注数
        for (ProjectLabelOut projectLabelOut : itemList) {
            projectLabelOut.setMarkingNum("0");
            for (ImageMarkingOut markingOut : marking) {
                if (projectLabelOut.getProjectId().equals(markingOut.getProjectId())) {
                    projectLabelOut.setMarkingNum(markingOut.getMarkingNum().toString());
                }
            }
            ImageMarkingIn imageIn = ImageMarkingIn.builder().projectId(projectLabelOut.getProjectId()).build();
            ImageMarkingOut imageOut = labelStatisticsMapper.slideNum(imageIn);
            projectLabelOut.setImageNum(imageOut.getImageNum().toString());
        }
        PageMaster<ProjectLabelOut> pageMaster = new PageMaster<>(itemList);
        return R.ok(pageMaster);
    }


    /**
     * 标签导出
     */
    @Override
    public void labelExport(ProjectLabelIn projectLabelIn, HttpServletResponse response) throws Exception {
        projectLabelIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        projectLabelIn.setUserId(SecurityUtils.getUserId());
        ProjectInVO projectInVO = ProjectInVO.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).projectType("1").build();
        List<ProjectListOut> projectList = labelStatisticsMapper.projectList(projectInVO);
        //项目id列表
        List<Long> projectIdList = projectList.stream().map(ProjectListOut::getProjectId).collect(Collectors.toList());
        LabelIn labelIn = LabelIn.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).build();
        List<LabelOut> labelOuts = labelStatisticsMapper.labelList(labelIn);
        //标签id列表
        List<Long> categoryIdList = labelOuts.stream().map(LabelOut::getCategoryId).collect(Collectors.toList());
        categoryIdList.add(0L);
        List<ProjectLabelOut> projectLabelOuts = labelStatisticsMapper.projectLabelList(projectLabelIn);
        //查询标注数
        ImageMarkingIn imageMarkingIn = ImageMarkingIn.builder().projectIds(projectIdList).categoryIds(categoryIdList).build();
        List<ImageMarkingOut> markingNums = labelStatisticsMapper.markingNums(imageMarkingIn);
        //查询图像数量
        List<ImageMarkingOut> imageNums = labelStatisticsMapper.imageNums(imageMarkingIn);
        for (ProjectLabelOut projectLabelOut : projectLabelOuts) {
            projectLabelOut.setStatusName(Container.PROJECT_STATUS.get(projectLabelOut.getStatus()));
            projectLabelOut.setMarkingNum("0");
            projectLabelOut.setImageNum("0");
            //添加标注数量
            for (ImageMarkingOut marking : markingNums) {
                if (projectLabelOut.getProjectId().equals(marking.getProjectId()) && projectLabelOut.getCategoryId().equals(marking.getCategoryId())) {
                    projectLabelOut.setMarkingNum(marking.getMarkingNum().toString());
                }
            }
            //添加图像数量
            for (ImageMarkingOut markingOut : imageNums) {
                if (projectLabelOut.getProjectId().equals(markingOut.getProjectId()) && projectLabelOut.getCategoryId().equals(markingOut.getCategoryId())) {
                    projectLabelOut.setImageNum(markingOut.getImageNum().toString());
                }
            }
        }
        List<Map<String, String>> titleList = getTitleList(CommonConstant.LABEL_STATISTICS_KEY, CommonConstant.LABEL_STATISTICS_VALUE);
        ExcelTool excelTool = new ExcelTool<>(MessageSource.M("EXCEL_FILE_PATH"), 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(String.valueOf(System.currentTimeMillis()), "UTF-8") + CommonConstant.FILE_SUFFIX_XLSX);
        excelTool.exportExcel(titleData, projectLabelOuts, response.getOutputStream(), true, false);

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
     * 项目导出
     */
    @Override
    public void projectExport(ProjectListIn projectListIn, HttpServletResponse response) throws Exception {
        projectListIn.setProjectType("1");
        projectListIn.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        projectListIn.setUsers(SecurityUtils.getUserId());
        List<ProjectLabelOut> itemList = labelStatisticsMapper.itemList(projectListIn);
        ProjectInVO projectInVO = ProjectInVO.builder().organizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId()).userId(SecurityUtils.getUserId()).projectType("1").build();
        List<ProjectListOut> projectList = labelStatisticsMapper.projectList(projectInVO);
        //项目id列表
        List<Long> projectIdList = projectList.stream().map(ProjectListOut::getProjectId).collect(Collectors.toList());
        //
        ImageMarkingIn imageMarkingIn = ImageMarkingIn.builder().projectIds(projectIdList).build();
        List<ImageMarkingOut> marking = labelStatisticsMapper.projectMarking(imageMarkingIn);
        //循环添加图像数量和标注数
        for (ProjectLabelOut projectLabelOut : itemList) {
            projectLabelOut.setMarkingNum("0");
            for (ImageMarkingOut markingOut : marking) {
                if (projectLabelOut.getProjectId().equals(markingOut.getProjectId())) {
                    projectLabelOut.setMarkingNum(markingOut.getMarkingNum().toString());
                }
            }
            ImageMarkingIn imageIn = ImageMarkingIn.builder().projectId(projectLabelOut.getProjectId()).build();
            ImageMarkingOut imageOut = labelStatisticsMapper.slideNum(imageIn);
            projectLabelOut.setImageNum(imageOut.getImageNum().toString());
            projectLabelOut.setStatusName(Container.PROJECT_STATUS.get(projectLabelOut.getStatus()));
        }

        // 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = getTitleList(CommonConstant.PROJECT_STATISTICS_KEY, CommonConstant.PROJECT_STATISTICS_VALUE);
        ExcelTool excelTool = new ExcelTool<>(MessageSource.M("EXCEL_FILE_PATH"), 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(String.valueOf(System.currentTimeMillis()), "UTF-8") + CommonConstant.FILE_SUFFIX_XLSX);
        excelTool.exportExcel(titleData, itemList, response.getOutputStream(), true, false);


    }


}
