package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.labelprojectstatistics.*;
import com.baomidou.dynamic.datasource.annotation.DS;

import java.util.List;

@DS("sharding")
public interface LabelStatisticsMapper {
    /**
     * 获取项目列表
     */
    List<ProjectListOut> projectList(ProjectInVO project);


    /**
     * 查询标准集
     */
    List<LabelSetOut> projectLabelSet(LabelSetIn labelSetIn);

    /**
     * 查询标签
     */
    List<LabelOut> labelList(LabelIn labelIn);

    /**
     * 查询项目标签
     */
    List<ProjectLabelOut> projectLabelList(ProjectLabelIn projectLabelIn);

    /**
     * 项目创建者
     */
    List<ProjectCreateByOut> userList(LabelSetIn labelSetIn);

    /**
     * 查询项目
     */
    List<ProjectLabelOut> itemList(ProjectListIn projectListIn);

    /**
     * 标签统计中的项目信息
     */
    List<ProjectLabelOut> projectIdList(ProjectLabelIn projectLabelIn);

    /**
     * 查询标注数量
     */
    List<ImageMarkingOut> markingNums(ImageMarkingIn imageMarkingIn);

    /**
     * 查询图像数量
     */
    List<ImageMarkingOut> imageNums(ImageMarkingIn imageMarkingIn);

    /**
     * 项目统计-查询标注数
     */
    List<ImageMarkingOut> projectMarking(ImageMarkingIn imageMarkingIn);

    /**
     * 批量查询图像数量
     * */
    List<ImageMarkingOut>listSlideNum(ImageMarkingIn imageMarkingIn);
}
