package cn.staitech.anno.controller;

import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.vo.special.*;
import cn.staitech.anno.enums.SpecialEnum;
import cn.staitech.anno.service.SpecialService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.utils.hasPerms;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.exception.auth.NotPermissionException;
import cn.staitech.common.core.utils.PageUtils;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.model.LoginUser;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static cn.staitech.common.security.utils.SecurityUtils.getLoginUser;

/**
 * @author gjt.
 */
@SuppressWarnings({"checkstyle:SummaryJavadoc", "checkstyle:SingleLineJavadoc", "checkstyle:EmptyLineSeparator"})
@Slf4j
//@Validated
@RestController
//@RestControllerAdvice
@RequestMapping("/special")
@Api(value = "专题管理", tags = "专题管理")
public class SpecialController extends BaseController {

    @Resource
    private SpecialService specialService;

    @ApiOperationSupport(author = "gjt")
    @Log(menu = "专题管理", subMenu = "专题创建", title = "专题列表", businessType = BusinessType.QUERY)
    @RequiresPermissions("special:subject:query")
    @ApiOperation(value = "查询专题信息")
    @PostMapping("/select")
    public R<PageMaster<SpecialResVo>> select(@Validated @RequestBody SpecialSelectVo req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);
        Special special = new Special();
        BeanUtils.copyProperties(req, special);
        special.setUserName(req.getCreateBy());
        special.setDelFlag(SpecialEnum.del_flag_0.value());
        // 判断当前用户是否是admin,admin 查看所有专题
        LoginUser loginUser = getLoginUser();
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            // 传入当前用户角色
            // 判断当前角色如果为专题管理员,查看当前用户所创建专题
            if (loginUser.getSysUser().getRoleId() == 3L) {
                special.setCreateBy(SecurityUtils.getUserId());
            }
            // 其他角色查看当前用户所创建和参与专题
            else {
                special.setSpecialUserId(SecurityUtils.getUserId());
            }
        }
        List<SpecialResVo> specialList = specialService.selectList(special);
        PageMaster<SpecialResVo> pageMaster = new PageMaster<>(specialList);
        return R.ok(pageMaster);
    }

    /**
     * 专题统计
     *
     * @param role 角色信息
     * @return PageMaster<SpecialStatisticsListVO>
     */
    @ApiOperationSupport(author = "YangLei")
    @ApiOperation(value = "专题统计")
    @Log(menu = "智能阅片", subMenu = "专题统计", title = "专题统计列表", businessType = BusinessType.QUERY)
//    @RequiresPermissions("special:subject:statistics")
    @PostMapping("/statistics")
    public R<PageMaster<SpecialStatisticsListVO>> statistics(@Validated @RequestBody SpecialStatisticsQueryVO role) {
        PageUtils.startPage(role.getPageNum(), role.getPageSize());
        role.setUserId(SecurityUtils.getUserId());
        List<SpecialStatisticsListVO> list = specialService.specialStatistics(role);
        PageMaster<SpecialStatisticsListVO> pageMaster = new PageMaster<>(list);
        return R.ok(pageMaster, MessageSource.M("OPERATE_SUCCEED"));
    }

    @ApiOperationSupport(author = "gjt")
    @Log(menu = "智能阅片", subMenu = "专题阅片", title = "专题阅片列表", businessType = BusinessType.QUERY)
    @ApiOperation(value = "查询专题阅片信息")
    @PostMapping("/selectSpecialReadFilm")
    public R<PageMaster<SpecialResVo>> selectSpecialReadFilm(@Validated @RequestBody SpecialSelectVo req) {
        List<SpecialResVo> specialList = specialService.selectSpecialReadFilm(req);
        PageMaster<SpecialResVo> pageMaster = new PageMaster<>(specialList);
        return R.ok(pageMaster);
    }

    @ApiOperation(value = "查询专题详情")
    @ApiOperationSupport(author = "gjt")
    @ApiImplicitParams({@ApiImplicitParam(name = "specialId", value = "专题id", required = true, dataType = "Long", paramType = "query")})
    @GetMapping("/selectById")
    public R<SpecialResVo> selectById(Long specialId) {
        return R.ok(specialService.selectSpecialId(specialId));
    }

    @Log(menu = "专题管理", subMenu = "专题创建", title = "新增专题", businessType = BusinessType.INSERT)
    @RequiresPermissions("special:subject:add")
    @ApiOperation(value = "专题创建")
    @ApiOperationSupport(author = "gjt")
    @PostMapping("/insert")
    public R<String> insert(@Validated @RequestBody SpecialInsertVo req) {
        specialService.insert(req);
        return R.ok(null, MessageSource.M("OPERATE_ERROR"));
    }

    @Log(menu = "专题管理", subMenu = "专题创建", title = "编辑", businessType = BusinessType.UPDATE)
    @RequiresPermissions("special:subject:edit")
    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "专题修改")
    @PostMapping("/update")
    public R<String> update(@Validated @RequestBody SpecialUpdateVo req) {
        specialService.update(req);
        return R.ok(null, MessageSource.M("OPERATE_ERROR"));
    }

    @Log(menu = "专题管理", subMenu = "专题创建", title = "删除/恢复/彻底删除", businessType = BusinessType.DELETE)
    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "专题删除")
    @PostMapping("/updateDelFlag")
    public R<String> updateDelFlag(@Validated @RequestBody SpecialDeleteVo req) {
        // 校验是否有权限
        if (!hasPerms.permsVerify(req.getPerms())) {
            throw new NotPermissionException(req.getPerms());
        }
        specialService.updateDelFlag(req);
        return R.ok(null, MessageSource.M("OPERATE_ERROR"));
    }

    @Log(menu = "专题管理", subMenu = "专题创建", title = "启动/暂停/锁定/解锁/完成", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "更新专题状态")
    @PostMapping("/updateStatus")
    public R<String> updateStatus(@Validated @RequestBody SpecialStatusVo req) {
        // 校验是否有权限
        if (!hasPerms.permsVerify(req.getPerms())) {
            throw new NotPermissionException(req.getPerms());
        }
        specialService.updateStatus(req);
        return R.ok(null, MessageSource.M("OPERATE_ERROR"));
    }


}
