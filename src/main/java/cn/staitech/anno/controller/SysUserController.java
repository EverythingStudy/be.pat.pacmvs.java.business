package cn.staitech.anno.controller;

import cn.staitech.anno.domain.user.in.SelectUserIdListIn;
import cn.staitech.anno.project.mapper.SysUserMapperV1;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "用户配置", tags = "用户配置")
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Resource
    private SysUserMapperV1 userMapper;


    @ApiOperation(value = "根据用户id查询用户列表")
    @PostMapping("/selectUserIdList")
    public R<List<cn.staitech.anno.project.domain.SysUser>> selectUserIdList(@RequestBody SelectUserIdListIn req) {
        QueryWrapper<cn.staitech.anno.project.domain.SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.in("user_id", req.getUserIdList()).eq("del_flag", "0");
        return R.ok(userMapper.selectList(sysUserQueryWrapper));
    }
}
