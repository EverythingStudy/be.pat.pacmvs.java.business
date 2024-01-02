package cn.staitech.anno.service;

import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.labelprojectstatistics.*;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectListOut;
import cn.staitech.common.core.domain.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface LabelStatisticsService {

    /**
     * 获取项目列表
     * */
    List<ProjectListOut> projectList(ImageMarkingIn imageMarkingIn);


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
    R<PageMaster<ProjectLabelOut>> projectLabelList(ProjectLabelIn projectLabelIn);

    /**
     * 项目创建者
     * */
    List<ProjectCreateByOut>userList(LabelSetIn labelSetIn);


    /**
     * 查询项目
     * */
    R<PageMaster<ProjectLabelOut>>itemList(ProjectListIn projectListIn);

    /**
     * 标签导出
     * */
    void labelExport(ProjectLabelIn projectLabelIn,HttpServletResponse response)throws Exception;

    /**
     * 项目导出
     * */
    void projectExport(ProjectListIn projectListIn,HttpServletResponse response)throws Exception;


//    List<ImageMarkingOut>markingNums(ImageMarkingIn imageMarkingIn);
}
