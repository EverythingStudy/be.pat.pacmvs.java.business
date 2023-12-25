package cn.staitech.anno.service;

import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.labelprojectstatistics.*;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectListVO;
import cn.staitech.common.core.domain.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface LabelStatisticsService {

    /**
     * 获取项目列表
     * */
    List<ProjectListVO> projectList();


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
   List<ProjectLabelOut> projectLabelList(ProjectLabelIn projectLabelIn);

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
    void labelExport(HttpServletResponse response)throws Exception;
}
