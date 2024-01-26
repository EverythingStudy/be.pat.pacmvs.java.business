package cn.staitech.anno.service;

import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.projectstatistics.*;
import cn.staitech.common.core.domain.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProjectStatisticsService {
    /**
     * 当前项目和标签集
     * */
    ProjectInfoOut projectInfor(Long projectId);


    /**
     * 标注人员
     * */
    List<labelingPersonnelOut> labelingPersonnel(Long projectId);

    /**
     * 标签
     * */
    List<LabelOut>label(Long projectId);

    /**
     * 多标签统计
     * */
    List<ProjectLabelOut>projectLabel(ProjectLabelIn projectLabelIn);

    /**
     * 多用户统计
     * */
    List<ProjectLabelOut>projectUser(ProjectLabelIn projectLabelIn);


    /**
     * 项目图像
     * */
    List<ProjectImageOut>projectImage(Long projectId);


    /**
     * 单图标签统计
     * */
    R<PageMaster<ImageLabelOut>> imageLabel(ImageLabelIn imageLabelIn);

    /**
     * 单图标签统计导出
     * */
    void imageLabelExport(ImageLabelIn imageLabelIn,HttpServletResponse response)throws Exception;

    /**
     * 多标签统计导出
     * */
    void projectLabelExport(ProjectLabelIn projectLabelIn,HttpServletResponse response)throws Exception;

    void projectUserExport(ProjectLabelIn projectLabelIn,HttpServletResponse response)throws Exception;


}
