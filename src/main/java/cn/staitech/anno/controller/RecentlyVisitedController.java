package cn.staitech.anno.controller;

import cn.staitech.anno.service.RecentlyVisitedService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.recentlyvisited.RecentlyVisitedSelectVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author gjt.
 * @data 2023/5/25 13:44
 */
@Slf4j
@Api(value = "最近访问", tags = "最近访问")
@RestController
@Validated
@RestControllerAdvice
@RequestMapping("/recentlyVisited")

public class RecentlyVisitedController {
    @Resource
    private RecentlyVisitedService recentlyVisitedService;

    @ApiOperation(value = "查询用户最近访问信息")
    @GetMapping("/selectList")
    public R<List<RecentlyVisitedSelectVO>> selectList(@RequestParam("projectType") @ApiParam(name = "projectType", value = "项目类型(1标注2评审3标准训练集)", required = true) Long projectType) {
        return R.ok(recentlyVisitedService.selectList(projectType));
    }

    @ApiOperation(value = "访问viewer记录接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "slideId", value = "切片id", required = true, dataType = "Long", paramType = "query"),
    @ApiImplicitParam(name = "tab", value = "标记默认为0，医学评审为1（区分医学评审和智能评审）", dataType = "String", paramType = "query")})
    @GetMapping("/visited")
    public R<String> add(Long slideId,String tab) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail(MessageSource.M("ARGUMENT_INVALID"));
        }
        // 判断当前角色是否为admin或者超级管理员
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            recentlyVisitedService.selectBy(slideId,tab);
        }
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }
}
