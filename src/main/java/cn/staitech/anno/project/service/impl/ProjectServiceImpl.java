package cn.staitech.anno.project.service.impl;

import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.mapper.ProjectMapperV1;
import cn.staitech.anno.project.service.ProjectService;
import cn.staitech.anno.project.vo.ProjectIn;
import cn.staitech.anno.project.vo.ProjectVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 86186
 * @description 针对表【tb_project(项目表)】的数据库操作Service实现
 * @createDate 2023-09-13 17:20:12
 */
@Service("ProjectServiceImplV1")
public class ProjectServiceImpl extends ServiceImpl<ProjectMapperV1, Project>
        implements ProjectService {

    @Override
    public List<ProjectVO> queryProject(ProjectIn params) {
        return getBaseMapper().queryProject(params);
    }

    @Override
    public IPage<ProjectVO> pageProject(Page page, ProjectIn params) throws Exception {
        return getBaseMapper().pageProject(page, params);
    }

}




