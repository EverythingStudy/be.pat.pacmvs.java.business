package cn.staitech.anno.controller;

import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.vo.special.SpecialReclaimResVo;
import cn.staitech.anno.domain.vo.special.SpecialReclaimSelectVo;
import cn.staitech.anno.domain.vo.special.SpecialResVo;
import cn.staitech.anno.enums.SpecialEnum;
import cn.staitech.anno.service.SpecialReclaimService;
import cn.staitech.anno.service.SpecialService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author gjt.
 * @data 2023/5/29 9:27
 */


@Slf4j
@Validated
@RestController
@RestControllerAdvice
@Api(value = "专题回收", tags = "专题回收")
@RequestMapping("/specialReclaim")
public class SpecialReclaimController {

    @Resource
    private SpecialReclaimService specialReclaimService;

    @Resource
    private SpecialService specialService;


    @ApiOperationSupport(author = "gjt")
    @RequiresPermissions("special:recover:query")
    @Log(menu = "专题管理", subMenu = "专题回收站",title = "查询", businessType = BusinessType.QUERY)
    @ApiOperation(value = "查询专题回收信息")
    @PostMapping("/select")
    public R<PageMaster<SpecialReclaimResVo>> select(@RequestBody SpecialReclaimSelectVo req) {
        Special special = new Special();
        special.setSpecialNumber(req.getSpecialNumber());
        special.setSpecialName(req.getSpecialName());
        special.setCreateTimeParams(req.getCreateTimeParams());
        special.setDelFlag(SpecialEnum.del_flag_1.value());
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            special.setCreateBy(SecurityUtils.getUserId());
        }
        List<SpecialResVo> specialList = specialService.selectList(special);
        List<SpecialReclaimResVo> specialReclaimResVos = specialReclaimService.selectList(specialList, req);
        // 按照时间进行倒排
//        specialReclaimResVos.sort((t1, t2) -> t2.getReclaimTime().compareTo(t1.getReclaimTime()));
        // 按照时间进行倒排
        specialReclaimResVos.sort(Comparator.comparing(SpecialReclaimResVo::getReclaimTime));
        // 进行分页操作
        List<SpecialReclaimResVo> subList = specialReclaimResVos.stream().skip((long) (req.getPageNum() - 1) * req.getPageSize()).limit(req.getPageSize()).collect(Collectors.toList());
        // 定义分页出参
        PageMaster<SpecialReclaimResVo> pageMaster = new PageMaster<>(subList);
        pageMaster.setTotal(specialReclaimResVos.size());
        return R.ok(pageMaster);
    }


}
