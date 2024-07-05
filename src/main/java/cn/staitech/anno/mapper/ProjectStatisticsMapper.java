package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.projectstatistics.*;
import com.baomidou.dynamic.datasource.annotation.DS;

import java.util.List;

@DS("sharding")
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
     * 项目图像
     * */
    List<ProjectImageOut>projectImage(Long projectId);

    /**
     * 单图标签统计
     * */
    List<ImageLabelOut>imageLabel(ImageLabelIn imageLabelIn);

    /**
     * 查询多标签统计标注数量
     * */
    List<ProjectLabelOut>labelsNumber(ProjectLabelIn projectLabelIn);

    /**
     * 多标签统计当前标签标注图像数量
     * */
    List<ProjectLabelOut>labelImageNumber(Long projectId);
    
    /**
    * 多标签统计当前项目下每个人的标注图像数量
    * */
   List<ProjectLabelOut> getLabelImageNumber(Long projectId);
}
