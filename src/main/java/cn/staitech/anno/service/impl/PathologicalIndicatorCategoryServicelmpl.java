package cn.staitech.anno.service.impl;


import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.project.ProjectExt;
import cn.staitech.anno.domain.vo.LabelListVO;
import cn.staitech.anno.domain.vo.LabelVO;
import cn.staitech.anno.domain.vo.statistic.StatisticCategoryListInVO;
import cn.staitech.anno.domain.vo.statistic.StatisticCategoryListOutVO;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.mapper.ProjectExtMapper;
import cn.staitech.anno.mapper.ProjectMapper;
import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.mapper.ProjectMapperV1;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class PathologicalIndicatorCategoryServicelmpl implements PathologicalIndicatorCategoryService {
    @Resource
    private PathologicalIndicatorCategoryMapper pathologicalIndicatorCategoryMapper;

    @Resource
    private ProjectMapperV1 projectMapperv1;

    /**
     * 添加标签
     */
    @Override
    public int insertSelective(PathologicalIndicatorCategory pathologicalIndicatorCategory) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        pathologicalIndicatorCategory.setCreateBy(sysUser.getUserId());
        pathologicalIndicatorCategory.setUpdateBy(sysUser.getUserId());
        pathologicalIndicatorCategory.setOrganizationId(sysUser.getOrganizationId());
        pathologicalIndicatorCategory.setCreateBy(SecurityUtils.getUserId());
        return pathologicalIndicatorCategoryMapper.insertSelective(pathologicalIndicatorCategory);
    }

    /**
     * 修改标签
     */
    @Override
    public String updateByPrimaryKeySelective(PathologicalIndicatorCategory indicator) {
        if (pathologicalIndicatorCategoryMapper.updateByPrimaryKeySelective(indicator) > 0) {
            return "修改成功";
        } else {
            return "该标签不存在";
        }
    }

    /**
     * 删除标签
     */
    @Override
    public String deleteByPrimaryKey(Long categoryId) {

        // 查询标签是否存在
        if (pathologicalIndicatorCategoryMapper.deleteByPrimaryKey(categoryId) > 0) {
            return "删除成功";
        } else {
            return "此标签不存在";
        }
    }

    /**
     * 查询标签详细信息
     */
    @Override
    public PathologicalIndicatorCategory selectByPrimaryKey(Long categoryId) {
        return pathologicalIndicatorCategoryMapper.selectByPrimaryKey(categoryId);
    }

    /**
     * 根据病理指标id获取全部信息
     *
     * @param indicatorId 病理指标ID
     * @return 标签信息
     */
    @Override
    public List<PathologicalIndicatorCategory> selectIndicatorIdAll(Long indicatorId) {
        return pathologicalIndicatorCategoryMapper.selectIndicatorIdAll(indicatorId);
    }

    /**
     * 根据病理指标id删除信息
     *
     * @param indicatorId 病理指标ID
     * @return 标签信息
     */
    @Override
    public int delIndicatorCategory(Long indicatorId) {
        return pathologicalIndicatorCategoryMapper.delIndicatorCategory(indicatorId);
    }

    @Override
    public PathologicalIndicatorCategory selectCategoryAll(Long CategoryId) {
        return pathologicalIndicatorCategoryMapper.selectCategoryAll(CategoryId);
    }

    /**
     * 获取标注类别统计列表
     *
     * @param indicatorProjectIdList 病例指标id列表、项目id列表
     * @return 结果
     */
    @Override
    public List<StatisticCategoryListOutVO> selectAnnotationCategoryStatisticList(
            StatisticCategoryListInVO indicatorProjectIdList) {

        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            indicatorProjectIdList.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        }
        return pathologicalIndicatorCategoryMapper.selectAnnotationCategoryStatisticList(indicatorProjectIdList);
    }

    /**
     * 条件查询标注类别
     *
     * @param Pathological 病例指标id 或 颜色 或 标注类别名称
     * @return 结果
     */
    @Override
    public List<PathologicalIndicatorCategory> selectIndicatorMessage(PathologicalIndicatorCategory Pathological) {
        return pathologicalIndicatorCategoryMapper.selectIndicatorMessage(Pathological);
    }

    /**
     * 通过项目id统计标注类别
     *
     * @param projectId
     * @return
     */
    @Override
    public List<StatisticCategoryListOutVO> selectByProjectId(Long projectId) {
        return pathologicalIndicatorCategoryMapper.selectByProjectId(projectId);
    }

    /**
     * 通过项目ID查询标注类别
     *
     * @param projectId 项目ID
     * @return 标注类别列表
     */
    @Override
    public List<PathologicalIndicatorCategory> selectCategoryByProjectId(Long projectId) {
        return pathologicalIndicatorCategoryMapper.selectCategoryByProjectId(projectId);
    }

    /**
     * 查询病例指标下的标注类别数量
     */
    @Override
    public Long selectCategoryNumber(Long indicatorId) {
        return pathologicalIndicatorCategoryMapper.selectCategoryNumber(indicatorId);
    }

    /**
     * 根据projectId查询标注类别
     */
    @Override
    public List<PathologicalIndicatorCategory> selectProjectCategory(Long projectId) {
        return pathologicalIndicatorCategoryMapper.selectProjectCategory(projectId);
    }

    /**
     * 根据indicatorId查询标注类别（不包含unLabel）
     */
    @Override
    public List<LabelListVO> selectByIndicator(LabelVO labelVO) {
        return pathologicalIndicatorCategoryMapper.selectByIndicator(labelVO);
    }

    @Override
    public List<PathologicalIndicatorCategory> selectprojectList(Long projectId) {
        Project project = projectMapperv1.selectById(projectId);
        if(project != null){
            QueryWrapper<PathologicalIndicatorCategory> pathologicalIndicatorCategoryQueryWrapper = new QueryWrapper<>();
            pathologicalIndicatorCategoryQueryWrapper.eq("indicator_id", project.getIndicatorId()).orderByDesc("order_number");
            return pathologicalIndicatorCategoryMapper.selectList(pathologicalIndicatorCategoryQueryWrapper);
        }
        return new ArrayList<>();
    }

    /**
     * 查询标签在标注中的使用数量
     */
    @Override
    public Integer selectLabelNum(Long categoryId) {
        return pathologicalIndicatorCategoryMapper.selectLabelNum(categoryId);
    }
}
