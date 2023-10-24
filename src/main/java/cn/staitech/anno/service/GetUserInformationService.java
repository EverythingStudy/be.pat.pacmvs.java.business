package cn.staitech.anno.service;

import cn.staitech.common.core.web.page.TableDataInfo;
import cn.staitech.system.api.domain.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "staitech-system", contextId = "saleserviceGetUsername")
public interface GetUserInformationService {
    /**
     * 获取用户信息
     */
    @GetMapping("/user/selectById")
    SysUser selectById(@RequestParam("userId") Long userId);

    @GetMapping("/user/list")
    TableDataInfo list(@RequestParam("user") SysUser user);


}
