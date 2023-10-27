package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ProjectMenu;
import cn.staitech.anno.mapper.ProjectMenuMapper;
import cn.staitech.anno.service.ProjectMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProjectMenuServiceImpl implements ProjectMenuService {

    @Resource
    private ProjectMenuMapper projectMenuMapper;


    @Override
    public List<ProjectMenu> selectList() {
        return projectMenuMapper.selectList();
    }

}
