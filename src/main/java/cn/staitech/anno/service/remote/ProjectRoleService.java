package cn.staitech.anno.service.remote;

import cn.staitech.anno.vo.project.ProjectRoleInsertVO;
import cn.staitech.common.core.web.page.TableDataInfo;
import cn.staitech.system.api.domain.SysProjectRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 项目级角色管理
 *
 * @author 王峰
 */
@FeignClient(value = "staitech-system", contextId = "projectRoleService")
public interface ProjectRoleService {
    /**
     * 通过roleId获取角色信息
     */
    @GetMapping("/projectRole/selectById")
    TableDataInfo selectById(@RequestParam("user") Long roleId);

    @PostMapping("/projectRole/add")
    SysProjectRole add(@RequestParam("projectRoleInsertVO") ProjectRoleInsertVO projectRoleInsertVO);
}
