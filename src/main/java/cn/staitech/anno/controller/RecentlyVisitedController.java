package cn.staitech.anno.controller;

import cn.staitech.anno.domain.ImageVisited;
import cn.staitech.anno.domain.RecentlyVisited;
import cn.staitech.anno.domain.vo.RecentlyVisitedVO.RecentlyVisitedSelectVO;
import cn.staitech.anno.mapper.SubImageMapper;
import cn.staitech.anno.service.*;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import cn.staitech.system.api.model.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cn.staitech.common.security.utils.SecurityUtils.getLoginUser;

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
    public R<List<RecentlyVisitedSelectVO>> selectList() {
        return R.ok(recentlyVisitedService.selectList());
    }

    @ApiOperation(value = "访问viewer记录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "slideId", value = "切片id", required = true, dataType = "Long", paramType = "query")})
    @GetMapping("/visited")
    public R<String> add(Long slideId) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail("参数异常");
        }
        // 判断当前角色是否为admin或者超级管理员
        if(!SysUser.isAdmin(SecurityUtils.getUserId())){
            recentlyVisitedService.selectBy(slideId);
        }
        return R.ok("操作成功");
    }


}
