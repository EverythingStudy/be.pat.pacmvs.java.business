package cn.staitech.anno.controller;

import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.domain.topic.in.TopicInsert;
import cn.staitech.anno.domain.topic.in.TopicQueryIn;
import cn.staitech.anno.service.TopicService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.utils.date.DateUtils;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author: wangfeng
 * @create: 2023-06-02 14:19:22
 * @Description: 切片（原图像）专题
 */
@Api(value = "切片列表（原图像管理）", tags = "切片列表（原图像管理）")
@ApiSupport(author = "wangfeng")
@RestController
@RequestMapping("/topic")
@Slf4j
public class TopicController {

    @Autowired
    private TopicService topicService;

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "切片专题列表 - 无分页版", notes = "切片专题目列表 - 王峰")
    @GetMapping("/list")
    public R<List<Topic>> list() {
        QueryWrapper<Topic> qw = new QueryWrapper();
        qw.eq("del_flag", 1).orderByDesc("topic_id");
        List<Topic> list = topicService.list(qw);
        return R.ok(list);
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "切片专题列表 - 有分页版", notes = "切片专题目列表 - 王峰")
    @PostMapping("/pagelist")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    public R<PageMaster<Topic>> pagelist(@Validated @RequestBody TopicQueryIn req) {
        PageMaster<Topic> pageMaster = topicService.pagelist(req);
        return R.ok(pageMaster);
    }


    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "添加切片（原图片）专题", notes = "添加切片（原图片）专题目 - 王峰")
    @PostMapping("/add")
    public R add(@Validated @RequestBody TopicInsert req) throws Exception {

        Long uid = SecurityUtils.getUserId();
        String time = DateUtils.getCurrentHHmmssString("yyyy-MM-dd HH:mm:ss");
        Topic topic = Topic.builder()
                .topicName(req.getTopicName().trim().toString())
                .createBy(uid)
                .updateBy(uid)
                .delFlag(1)
                .createTime(time)
                .updateTime(time)
                .build();

        topicService.save(topic);
        return R.ok();
    }

}
