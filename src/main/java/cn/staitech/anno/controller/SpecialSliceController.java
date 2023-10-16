package cn.staitech.anno.controller;

import cn.staitech.anno.domain.vo.special.SpecialResVo;
import cn.staitech.anno.domain.vo.specialSliceImage.AuditSpecialImageVO;
import cn.staitech.anno.domain.vo.specialSliceImage.SpecialSliceSelectVO;
import cn.staitech.anno.domain.vo.specialSliceImage.SpecialSliceVo;
import cn.staitech.anno.service.SpecialImageService;
import cn.staitech.anno.service.SpecialService;
import cn.staitech.anno.service.SubImageService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.SpecialPageMaster;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.RequiresSpecialPermissions;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SpecialSliceController
 * @Description:专题切片配置
 * @date 2023年6月6日
 */
@RestController
@RequestMapping("/specialSlice")
@Api(value = "切片配置", tags = "切片配置")
@Slf4j
public class SpecialSliceController {
    @Resource
    private SpecialImageService specialImageService;
    @Resource
    private SubImageService subImageService;
    @Resource
    private SpecialService specialService;


    @ApiOperationSupport(author = "wanglibei")
    @ApiOperation(value = "查询全部切片数据")
    @Log(title = "切片配置列表查询", menu = "专题管理", subMenu = "切片配置列表查询", businessType = BusinessType.OTHER)
    @PostMapping("/selectSliceImage")
    public R<SpecialPageMaster<SpecialSliceVo>> selectSliceImage(@RequestBody SpecialSliceSelectVO req) {
        //处理下token过期的数据
        subImageService.selectExpireSpecialSlice(req);
        PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);
        //切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中
//		req.setSliceImageStatus("2");
        List<SpecialSliceVo> wsivList = subImageService.selectSpecialSliceVo(req);
        //查询专题是否已经全部交付
        SpecialResVo specialVo = specialService.selectSpecialId(req.getBelongSpecialId());
        SpecialPageMaster<SpecialSliceVo> pageMaster = new SpecialPageMaster<>(wsivList, specialVo.getDeliveryStatus(), specialVo);
        return R.ok(pageMaster);
    }


    @ApiOperation(value = "审核切片数据")
    @RequiresSpecialPermissions("specialConfig:slice:pass")
    @ApiOperationSupport(author = "wanglibei")
    @Log(title = "审核切片", menu = "专题管理", subMenu = "审核切片", businessType = BusinessType.UPDATE)
    @PostMapping("/auditImage")
    public R<String> auditImage(@RequestBody AuditSpecialImageVO vo) {
        return specialImageService.updateSpecialImageList(vo);
    }

    @ApiOperation(value = "交付切片数据")
    @RequiresSpecialPermissions("specialConfig:slice:deliver")
    @ApiOperationSupport(author = "wanglibei")
    @Log(title = "交付切片", menu = "专题管理", subMenu = "交付切片", businessType = BusinessType.UPDATE)
    @PostMapping("/deliveryImage")
    public R<String> deliveryImage(@RequestBody AuditSpecialImageVO vo) {
        return specialImageService.updateDeliveryBySpecialId(vo);
    }


    @ApiOperation(value = "根据用户名称处理退出信息")
    @GetMapping("/logOut")
    public R logOutSpecial(String userName) {
        subImageService.logOutSpecial(userName);
        return R.ok(MessageSource.M("OPERATE_SUCCEED"));
    }
}
