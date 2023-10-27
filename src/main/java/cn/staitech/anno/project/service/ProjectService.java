package cn.staitech.anno.project.service;

import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.vo.ProjectIN;
import cn.staitech.anno.project.vo.ProjectVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 86186
 * @description 针对表【tb_project(项目表)】的数据库操作Service
 * @createDate 2023-09-13 17:20:12
 */
public interface ProjectService extends IService<Project> {

    List<ProjectVO> queryProject(ProjectIN params) throws Exception;

    IPage<ProjectVO> pageProject(Page page, ProjectIN params) throws Exception;

}
