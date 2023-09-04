package cn.staitech.anno.controller;

import cn.staitech.anno.constant.GroupConstant;
import cn.staitech.anno.domain.Group;
import cn.staitech.anno.domain.vo.GroupGetVO;
import cn.staitech.anno.domain.vo.GroupListVO;
import cn.staitech.anno.domain.vo.GroupVO;
import cn.staitech.anno.domain.vo.ProjectGroupAddVO;
import cn.staitech.anno.enums.GroupEnum;
import cn.staitech.anno.enums.ReasonsEnum;
import cn.staitech.anno.service.GroupService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zmj .
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
     * 添加分组
     */
    @ApiOperation(value = "添加分组", notes = "ZMJ")
    @RequiresPermissions("special:group:add")
    @Log(title = "新增分组", menu = "专题管理", subMenu = "分组配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<String> addGroup(@Validated @RequestBody Group group) {
        int res = groupService.insertSelective(group);
        if (res == 0) {
            return R.fail(GroupConstant.ALREADY_EXISTS);
        }
        return R.ok(null, GroupConstant.CREATED_SUCCESSFULLY);
    }

    /**
     * 获取分组列表
     */
    @ApiOperation(value = "获取分组列表", notes = "ZMJ")
    @RequiresPermissions("special:group:list")
    @GetMapping("/list")
    @Log(title = "分组列表", menu = "专题管理", subMenu = "分组配置", businessType = BusinessType.QUERY)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    public R<PageMaster<GroupListVO>> getGroup(
            GroupGetVO groupGetVO) {
        startPage();
        Group group = Group.builder().delFlag(0).specialId(groupGetVO.getSpecialId()).reasons(groupGetVO.getReasons()).build();
        //查询没删除的分组
        List<GroupListVO> groups = groupService.selectAllGroup(group);
        for (GroupListVO groupListVO : groups) {
            ProjectGroupAddVO projectGroupAddVO = ProjectGroupAddVO.builder().groupId(groupListVO.getGroupId()).build();
            List<ProjectGroupAddVO> groupAddVOS = groupService.selectProjectGroup(projectGroupAddVO);
            groupListVO.setTotal(groupAddVOS.size());
            groupListVO.setGenderName(GroupEnum.getEnumLabelByValue(groupListVO.getGender()));
            groupListVO.setCause(ReasonsEnum.getEnumLabelByValue(groupListVO.getReasons()));
        }
        PageMaster<GroupListVO> pageMaster = new PageMaster<>(groups);
        return R.ok(pageMaster);
    }



    /**
     * 编辑分组
     */
    @ApiOperation(value = "编辑分组", notes = "ZMJ")
    @RequiresPermissions("special:group:edit")
    @Log(title = "编辑", menu = "专题管理", subMenu = "分组配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R<String> updateGroup(@Validated @RequestBody GroupVO group) {
        //1.先校验当前分组下是否关联的有切片，有不能修改（页面提示：当前分组已经绑定了切片，请先解除绑定的切片再重试），无可以修改
        int groupAddVOS = groupService.selectSlide(group.getGroupId());
        if (groupAddVOS > 0) {
            return R.fail(GroupConstant.UNBIND_RETRY);
        }
        //2.修改时判断是否和已有的分组是否重复，有重复（则页面提示：当前组别已经存在，请勿重复添加），无重复修改成功
        Group groupVerify = Group.builder().groupName("Group"+group.getGroupName()).gender(group.getGender()).
                groupId(group.getGroupId()).specialId(group.getSpecialId()).reasons(group.getReasons()).dosage(group.getDosage()).build();
        //根据专题、组别和性别、移走原因、剂量查询分组数量
        int num = groupService.selectGroup(groupVerify);
        if (num == 0) {
            group.setUpdateBy(SecurityUtils.getUserId());
            group.setGroupName(groupVerify.getGroupName());
            groupService.updateGroup(group);
            return R.ok(null, GroupConstant.SUCCESSFULLY_MODIFIED);
        }
        return R.fail(GroupConstant.ALREADY_EXISTS);
    }

    /**
     * 获取详情
     */
    @ApiOperation(value = "获取分组详情", notes = "ZMJ")
    @PostMapping("/details")
    public R<GroupListVO> getDetails(@RequestParam @ApiParam(name = "groupId", value = "组别id", required = true) Long groupId) {
        Group group = Group.builder().groupId(groupId).build();
        GroupListVO detail = groupService.selectOneGroup(group);
        if ( detail.getReasons()== ReasonsEnum.reasons_1.value()){
            detail.setCause(ReasonsEnum.reasons_1.label());
        }else{
            detail.setCause(ReasonsEnum.reasons_2.label());
        }
        return R.ok(detail);
    }

    /**
     * 删除分组
     */
    @ApiOperation(value = "删除分组", notes = "ZMJ")
    @RequiresPermissions("special:group:remove")
    @Log(title = "删除", menu = "专题管理", subMenu = "分组配置", businessType = BusinessType.DELETE)
    @PostMapping("/del")
    public R<String> delGroup(@RequestParam @ApiParam(name = "groupId", value = "组别id", required = true) Long groupId) {
        ProjectGroupAddVO projectGroupAddVO = ProjectGroupAddVO.builder().groupId(groupId).build();
        List<ProjectGroupAddVO> projectGroup = groupService.selectProjectGroup(projectGroupAddVO);
        if (projectGroup.size() > 0) {
            return R.fail(GroupConstant.PROHIBIT_DELETION);
        }
        GroupVO groupVO = GroupVO.builder().groupId(groupId).delFlag(1).updateBy(SecurityUtils.getUserId()).build();
        groupService.updateGroup(groupVO);
        return R.ok(null, GroupConstant.SUCCESSFULLY_DELETED);
    }




}
