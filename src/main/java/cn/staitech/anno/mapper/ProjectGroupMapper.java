package cn.staitech.anno.mapper;


import cn.staitech.anno.domain.project.ProjectExt;
import cn.staitech.anno.domain.projectgroup.ProjectGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ProjectGroupMapper{
    /**
     * @param projectId
     * @return 项目分组信息
     */
    List<ProjectGroup> selectProjectGroupList(@Param("projectId")Long projectId,@Param("reasons")int reasons);

    /**
     * @param projectIds
     * @return 根据项目id集合查询分组列表
     */
    List<ProjectGroup> selectProjectGroupByProjectId(@Param("projectIds") List<Long> projectIds,@Param("groupName") String groupName,@Param("reasons") Long reasons);

    /**
     * 主键查询
     *
     * @param projectGroupId
     * @return 项目分组信息
     */
    ProjectGroup selectById(Long projectGroupId);

    /**
     * 修改项目分组状态置删除状态
     *
     * @param projectGroupId
     * @return
     */
    int updateState(@Param("projectGroupId") Long projectGroupId, @Param("createBy") Long createBy);

    /**
     * @param projectId
     * @return 修改项目下切片数
     */
    int updateProjectSlideTotal(Long projectId);

    /**
     * 修改项目分组下切片数
     *
     * @param projectGroupId
     * @return
     */
    int updateGroupSlideTotal(Long projectGroupId);

    /**
     * 添加項目分组表
     *
     * @return 添加数据
     */
    int insertProjectGroupByGroup(ProjectExt projectExt);

    /**
     * @param projectId
     * @return 项目分组切片数
     */
    int selectCountSlide(@Param("projectId") Long projectId, @Param("groupId") Long groupId);

    /**
     * @param projectId
     * @return 判断是否有 该组内的切片正在分析中，禁止清空
     */
    int selectProcessSlide(@Param("projectId") Long projectId, @Param("groupId") Long groupId);

    int updateStateSlide(@Param("groupId") Long groupId,@Param("projectId") Long projectId);

    /**
     *  修改项目分组为删除状态
     * @param projectId
     * @return
     */
    int updateDelFlag(@Param("projectId") Long projectId, @Param("createBy") Long createBy);
    /**
     *查询项目分组下的移走原因
     */
    List<Integer> selectReasonsList(Long projectId);


}