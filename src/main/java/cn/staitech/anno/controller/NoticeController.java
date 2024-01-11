package cn.staitech.anno.controller;

import cn.staitech.anno.service.NoticeService;
import cn.staitech.anno.vo.notice.in.NoticeChangeStatusIn;
import cn.staitech.anno.vo.notice.out.NoticeListQueryOut;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @ApiOperation(value = "消息列表")
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
