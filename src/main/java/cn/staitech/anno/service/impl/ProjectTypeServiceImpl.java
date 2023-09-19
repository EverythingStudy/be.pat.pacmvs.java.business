package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ProjectType;
import cn.staitech.anno.mapper.ProjectTpyeMapper;
import cn.staitech.anno.service.ProjectTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangf
 * @description 针对表【tb_project_tpye】的数据库操作Service实现
 * @createDate 2023-09-18 16:15:33
 */
@Service
public class ProjectTypeServiceImpl extends ServiceImpl<ProjectTpyeMapper, ProjectType> implements ProjectTypeService {
    @Resource
    private ProjectTpyeMapper projectTpyeMapper;

    @Override
    public Map<String, String> selectMap() {

        List<ProjectType> list = projectTpyeMapper.selectList();
        Map<String, String> map = list.stream()
                .collect(Collectors.toMap(ProjectType::getProjectTypeId, ProjectType::getProjectTypeName));
        return map;
    }
}


