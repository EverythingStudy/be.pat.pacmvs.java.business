package cn.staitech.anno.project.mapper;

import cn.staitech.anno.project.domain.Project;
import cn.staitech.anno.project.vo.ProjectIn;
import cn.staitech.anno.project.vo.ProjectVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 86186
 * @description 针对表【tb_project(项目表)】的数据库操作Mapper
 * @createDate 2023-09-13 17:20:12
 * @Entity cn.staitech.anno.project.domain.Project
 */
public interface ProjectMapperV1 extends BaseMapper<Project> {

    List<ProjectVO> queryProject(@Param("params") ProjectIn params);

    IPage<ProjectVO> pageProject(@Param("page") Page page, @Param("params") ProjectIn params) throws Exception;
}




