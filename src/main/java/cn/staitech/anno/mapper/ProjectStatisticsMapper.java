package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.projectstatistics.*;

import java.util.List;

public interface ProjectStatisticsMapper {

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
     * 项目标签统计
     * */
    List<ProjectLabelOut>projectLabel(ProjectLabelIn projectLabelIn);

    /**
     * 项目图像
     * */
    List<ProjectImageOut>projectImage(Long projectId);

    /**
     * 单图标签统计
     * */
    List<ImageLabelOut>imageLabel(ImageLabelIn imageLabelIn);
}
