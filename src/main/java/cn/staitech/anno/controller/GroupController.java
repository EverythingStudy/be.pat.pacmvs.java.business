package cn.staitech.anno.controller;

import cn.staitech.anno.domain.Group;
import cn.staitech.anno.service.GroupService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author wangfeng
 * @Date 2023/5/30 16:42
 * @desc 分组配置
 */
@Api(tags = "分组")
@RestController
@RequestMapping("/group")
public class GroupController extends BaseController {
    @Resource
    private GroupService groupService;

    /**
     * 获取分组列表
     */
    @ApiOperation(value = "获取分组列表", notes = "ZMJ")
    @Log(title = "分组列表", menu = "专题管理", subMenu = "分组配置", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public R<List<Group>> list() throws ExecutionException, InterruptedException {
        List<Group> list = groupService.list();
        return R.ok(list);
    }

}
