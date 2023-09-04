package cn.staitech.anno.domain.project.out;

import cn.staitech.anno.domain.project.ProjectExt;
import cn.staitech.anno.domain.projectgroup.ProjectGroup;
import lombok.Data;

import java.util.List;

/**
 * @author mugw
 * @version 1.0
 * @description 项目信息（包含分组列表）
 * @date 2023/6/13 14:57:44
 */
@Data
public class ProjectWithGroupsVo extends ProjectExt {

    private List<ProjectGroup> children;
}
