package cn.staitech.anno.controller;

import cn.staitech.anno.domain.Airepost;
import cn.staitech.anno.service.AirepostService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.airepost.AirepostList;
import cn.staitech.anno.vo.slide.SlideIdVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Controller：AI预测-算法项目-眼科拼接Viewer
 *
 * @author wangfeng
 * @date 2023-11-10
 */
@Slf4j
@Api(value = "AI预测-算法项目-眼科拼接Viewer", tags = "AI预测-算法项目-眼科拼接Viewer")
@RestController
@RequestMapping("/airepost")
public class AirepostController {
    @Resource
    private AirepostService airepostService;

    /**
     * 列表查询
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "AI预测-算法项目-眼科拼接Viewer-列表", notes = "AI预测-算法项目-眼科拼接Viewer-列表")
    @Log(title = "眼科拼接Viewer-列表", menu = "AI预测", subMenu = "算法项目", businessType = BusinessType.QUERY)
    @PostMapping("/list")
    public R<List<Airepost>> list(@RequestBody SlideIdVO request) {
        Airepost airepost = new Airepost();
        airepost.setSlideId(request.getSlideId());
        List<Airepost> list = airepostService.selectAirepostList(airepost);
        return R.ok(list);
    }

    /**
     * 批量修改
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "AI预测-算法项目-眼科拼接Viewer-批量修改", notes = "AI预测-算法项目-眼科拼接Viewer-列表")
    @Log(title = "眼科拼接Viewer-批量修改", menu = "AI预测", subMenu = "算法项目", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R<String> edit(@RequestBody AirepostList request) {
        List<Airepost> list = request.getAirepostLists();
        if (CollectionUtils.isNotEmpty(list) && airepostService.updateBatchById(list)) {
            return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
        }
        return R.fail(MessageSource.M("OPERATE_ERROR"));
    }

    /**
     * 重置 - 恢复至初始状态：将所有图片的位置、旋转角度、透明度、显示与隐藏，均恢复至未调整之前的状态。
     */
    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "AI预测-算法项目-眼科拼接Viewer-重置", notes = "AI预测-算法项目-眼科拼接Viewer-列表")
    @Log(title = "眼科拼接Viewer-重置", menu = "AI预测", subMenu = "算法项目", businessType = BusinessType.UPDATE)
    @PostMapping("/reset")
    public R<List<Airepost>> reset(@RequestBody SlideIdVO request) {
        Airepost airepost = new Airepost();
        airepost.setSlideId(request.getSlideId());
        if (airepostService.reset(airepost)) {
            List<Airepost> list = airepostService.selectAirepostList(airepost);
            return R.ok(list, MessageSource.M("OPERATE_SUCCEED"));
        }
        return R.fail(MessageSource.M("OPERATE_ERROR"));
    }
}
