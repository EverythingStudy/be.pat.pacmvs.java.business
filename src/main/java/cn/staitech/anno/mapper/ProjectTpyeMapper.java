package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.ProjectTpye;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author wangf
* @description 针对表【tb_project_tpye】的数据库操作Mapper
* @createDate 2023-09-18 16:15:33
* @Entity cn.staitech.anno.domain.ProjectTpye
*/
public interface ProjectTpyeMapper extends BaseMapper<ProjectTpye> {
    List<ProjectTpye> selectList();
}

