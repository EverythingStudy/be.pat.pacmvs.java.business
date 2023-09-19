package cn.staitech.anno.service;

import cn.staitech.anno.domain.ProjectType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author wangf
 * @description 针对表【tb_project_tpye】的数据库操作Service
 * @createDate 2023-09-18 16:15:33
 */
public interface ProjectTypeService extends IService<ProjectType> {
    Map<String, String> selectMap();
}
