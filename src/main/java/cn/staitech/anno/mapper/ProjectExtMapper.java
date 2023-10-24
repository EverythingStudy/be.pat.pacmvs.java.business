package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.po.ProjectPo;
import cn.staitech.anno.domain.project.ProjectExt;
import cn.staitech.anno.domain.project.out.CreateStatusOut;
import cn.staitech.anno.domain.project.out.ViscusQueryOut;
import cn.staitech.anno.domain.project.out.data.NavigationBarData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目表 数据层
 *
 * @author staitech
 */
public interface ProjectExtMapper extends BaseMapper<ProjectPo> {
    /**
     * 列表查询
     *
     * @param project
     * @return 项目列表
     */
    List<ProjectExt> selectProjectList(ProjectExt project);

    /**
     * 新增数据
     *
     * @param rojectExt 实例对象
     * @return 影响行数
     */
    int insert(ProjectExt rojectExt);

    /**
     * 更新数据
     *
     * @param rojectExt 实例对象
     * @return 影响行数
     */
    int update(ProjectExt rojectExt);

    /**
     * @param rojectExt
     * @return 项目列表
     */

    List<ProjectExt> selectByProject(ProjectExt rojectExt);


    /**
     * 根据专题id查询关联的项目总数
     *
     * @param specialId 专题id
     * @return 项目详情
     */
    int selectSpecialId(Long specialId);

    /**
     * 根据主键id查询项目详情
     *
     * @param projectId
     * @return 项目详情
     */
    ProjectExt selectById(Long projectId);

    /**
     * 修改项目为删除状态
     *
     * @param projectId
     * @return
     */
    int updateDelFlag(@Param("projectId") Long projectId, @Param("createBy") Long createBy);

    /**
     * @param specialId
     * @return 导航栏数据
     */
    List<NavigationBarData> selectProjectAll(Long specialId);

    /**
     * @param projectId
     * @return 项目切片数
     */
    int selectCountSlide(Long projectId);

    /**
     * @param projectId
     * @return 项目内切片还没有分析完成的数量
     */
    int countNotReady(Long projectId);

    /**
     * @param specialId
     * @return 状体内脏器类型
     */
    List<ViscusQueryOut> selectViscusBySpecial(Long specialId);

    /**
     * 查询状体
     *
     * @param specialId
     * @return
     */
    CreateStatusOut selectSpecial(Long specialId);

    /**
     * 修改专题
     *
     * @return
     */
    int updateSpecial(@Param("specialId") Long specialId, @Param("resultDesc") Long resultDesc);


}