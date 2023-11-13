package cn.staitech.anno.controller;

import cn.staitech.anno.domain.Airepost;
import cn.staitech.anno.service.AirepostService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.airepost.AirepostList;
import cn.staitech.anno.vo.slide.SlideIdVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * AirepostController
 *
 * @author wangfeng
 * @date 2023-11-10
 */
@RestController
@RequestMapping("/airepost")
public class AirepostController {
    @Resource
    private AirepostService airepostService;

    /**
     * 查询Airepost列表
     */
    @PostMapping("/list")
    public R<List<Airepost>> list(@RequestBody SlideIdVO request) {
        Airepost airepost = new Airepost();
        airepost.setSlideId(request.getSlideId());
        List<Airepost> list = airepostService.selectAirepostList(airepost);
        return R.ok(list);
    }

    /**
     * 批量修改Airepost
     */
    @Log(title = "Airepost", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R<String> edit(@RequestBody AirepostList request) {
        if (!request.getAirepostLists().isEmpty()) {
            airepostService.saveBatch(request.getAirepostLists());
        }
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

}
