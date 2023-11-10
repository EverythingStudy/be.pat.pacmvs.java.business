package cn.staitech.anno.controller;

import cn.staitech.anno.domain.Airepost;
import cn.staitech.anno.service.AirepostService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.airepost.AirepostQueryVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private AirepostService airepostService;

    /**
     * 查询Airepost列表
     */
    @GetMapping("/list")
    public R<List<Airepost>> list(@RequestBody AirepostQueryVO request) {
        Airepost airepost = new Airepost();
        airepost.setProjectId(request.getProjectId().toString());
        List<Airepost> list = airepostService.selectAirepostList(airepost);
        return R.ok(list);
    }

    /**
     * 获取Airepost详细信息
     */
    @GetMapping(value = "/{reportUuid}")
    public R<Airepost> getInfo(@PathVariable("reportUuid") Long reportUuid) {
        return R.ok(airepostService.selectAirepostByReportUuid(reportUuid));
    }

    /**
     * 修改Airepost
     */
    @Log(title = "Airepost", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R<String> edit(@RequestBody Airepost airepost) {
        airepostService.updateAirepost(airepost);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }


}
