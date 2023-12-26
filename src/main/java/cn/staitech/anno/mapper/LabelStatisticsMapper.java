package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.labelprojectstatistics.*;

import java.util.List;

public interface LabelStatisticsMapper {
    /**
     * 获取项目列表
     * */
   List<ProjectListOut> projectList(ProjectInVO project);


    /**
     * 查询标准集
     * */
    List<LabelSetOut> projectLabelSet(LabelSetIn labelSetIn);

    /**
     * 查询标签
     * */
    List<LabelOut>labelList(LabelIn labelIn);

    /**
     * 查询项目标签
     * */
    List<ProjectLabelOut>projectLabelList(ProjectLabelIn projectLabelIn);

    /**
     * 查询图像数量
     * */
    ImageMarkingOut imageNum(ImageMarkingIn imageMarkingIn);

    /**
     * 查询标注数量
     * */
    ImageMarkingOut markingNum(ImageMarkingIn imageMarkingIn);

    /**
     * 项目创建者
     * */
    List<ProjectCreateByOut>userList(LabelSetIn labelSetIn);

   /**
    * 查询项目
    * */
     List<ProjectLabelOut>itemList(ProjectListIn projectListIn);

    /**
     * 查询图像数量
     * */
    ImageMarkingOut slideNum(ImageMarkingIn imageMarkingIn);
}
