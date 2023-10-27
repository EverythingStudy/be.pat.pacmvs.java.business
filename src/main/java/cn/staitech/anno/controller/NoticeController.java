package cn.staitech.anno.controller;

import cn.staitech.anno.domain.notice.in.NoticeChangeStatusIn;
import cn.staitech.anno.domain.notice.out.NoticeListQueryOut;
import cn.staitech.anno.domain.notice.out.NoticeQueryOut;
import cn.staitech.anno.service.NoticeService;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author wudi
 * @Date 2023/6/6 10:02
 * @desc 消息提醒接口
 */
@Slf4j
@Api(tags = "消息提醒模块")
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;

    @GetMapping("/getNotice")
    public R<List<NoticeQueryOut>> getNotice() {
        List<NoticeQueryOut> resp = noticeService.getNotice();
        return R.ok(resp);
    }

    @ApiOperation(value = "消息列表", notes = "新版")
    @GetMapping("/getNoticeList")
    public R<NoticeListQueryOut> getNoticeList() {
        NoticeListQueryOut resp = noticeService.getNoticeList();
        return R.ok(resp);
    }

    @ApiOperation(value = "消息状态置为已读")
    @PostMapping("/changeStatus")
    public R changeStatus(@RequestBody NoticeChangeStatusIn req) {
        return noticeService.changeStatus(req);
    }
}
