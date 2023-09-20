package cn.staitech.anno.project.controller;

import cn.staitech.anno.project.domain.Opt;
import cn.staitech.anno.project.service.OptService;
import cn.staitech.anno.project.vo.ProjectIN;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/13 18:28:56
 */
@Slf4j
@Api(value = "切片操作记录", tags = "切片操作记录")
@RestController
@Validated
@RestControllerAdvice
@RequestMapping("/intelligentAnno/opt")
public class OptController {
    @Resource
    private OptService optService;

    @ApiOperation(value = "切片操作记录-分页查询")
    @GetMapping("/page")
    public R<PageMaster<Opt>> page(@NotNull(message = "分页参数为空！") @RequestParam("pageNum") @ApiParam(name = "pageNum", value = "分页参数", required = true) Integer pageNum,
                                   @NotNull(message = "分页参数为空！") @RequestParam("pageSize") @ApiParam(name = "pageSize", value = "分页参数", required = true) Integer pageSize,
                                   ProjectIN in) throws Exception{
        Page page = new Page(pageNum, pageSize);
        optService.page(page);
        PageMaster<Opt> pageMaster = PageMaster.of(page.getRecords());
        pageMaster.setTotal(page.getTotal());
        return R.ok(pageMaster);
    }

    @ApiOperation(value = "列表查询")
    @GetMapping("/query")
    public R<List<Opt>> query(@NotNull(message = "切片id！") @RequestParam("slideId") @ApiParam(name = "slideId", value = "切片id", required = true) Long slideId) throws Exception{
        Opt opt = Opt.builder().slideId(slideId).build();
        return R.ok(optService.list(Wrappers.query(opt)));
    }

    @ApiOperation(value = "添加操作记录")
    @PostMapping("/add")
    public R<Opt> query(Opt in) throws Exception{
        Long userId = SecurityUtils.getUserId();
        in.setCreateBy(userId);
        in.setUpdateBy(userId);
        in.setCreateTime(new Date());
        in.setUpdateTime(new Date());
        optService.save(in);
        return R.ok(in);
    }

}
