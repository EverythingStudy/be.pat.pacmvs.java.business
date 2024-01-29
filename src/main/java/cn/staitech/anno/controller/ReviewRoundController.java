package cn.staitech.anno.controller;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.ReviewRound;
import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.service.SlideService;
import cn.staitech.anno.service.ReviewRoundService;
import cn.staitech.anno.service.TopicService;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.reviewround.*;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: wangfeng
 * @create: 2023-09-18 17:15:58
 * @Description: 评审轮次
 */
@Api(value = "评审轮次", tags = "评审轮次")
@RestController
@RequestMapping("/reviewRound")
@Slf4j
public class ReviewRoundController {
    public static Map<Long, String> topicMap = new HashMap<>(16);
    @Resource
    private ReviewRoundService reviewRoundService;
    @Resource
    private TopicService topicService;
    @Resource
    private SlideService slideService;

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "查询评审轮次列表")
    @GetMapping("/getById")
    public R<ReviewRoundOutVO> one(@RequestParam("reviewRoundId") Long reviewRoundId) {
        return R.ok(reviewRoundService.getOneById(reviewRoundId));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "查询评审轮次列表")
    @GetMapping("/list")
    public R<PageMaster<ReviewRoundOutVO>> list(@NotNull(message = "{ReviewRoundController.list.isnull}") @RequestParam("pageNum") @ApiParam(name = "pageNum", value = "分页参数", required = true) Integer pageNum,
                                                @NotNull(message = "{ReviewRoundController.list.isnull}") @RequestParam("pageSize") @ApiParam(name = "pageSize", value = "分页参数", required = true) Integer pageSize,
                                                @RequestParam("projectId") Long projectId) {
        return R.ok(reviewRoundService.pageReviewRound(pageNum, pageSize, projectId));
    }

    @ApiOperation(value = "查询评审轮次内容")
    @GetMapping("/query")
    public R<List<ReviewRound>> query(@RequestParam("projectId") Long projectId) {
        QueryWrapper<ReviewRound> queryWrapper = Wrappers.query(ReviewRound.builder().projectId(projectId).build());
        queryWrapper.select("review_content", "content_id");
        queryWrapper.groupBy("review_content", "content_id");
        return R.ok(reviewRoundService.list(queryWrapper));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "添加评审轮次")
    @PostMapping("/add")
    public R add(@RequestBody ReviewRoundBatchInVO reviewRoundBatchInVO) {
        return R.ok(reviewRoundService.saveBatchByList(reviewRoundBatchInVO));
    }

    @ApiOperation(value = "新添加评审轮次")
    @PostMapping("/addNew")
    public R addNew(@RequestBody ReviewRoundBatchInVO reviewRoundBatchInVO) {
        //去重
        QueryWrapper<ReviewRound> queryWrapper = Wrappers.query(ReviewRound.builder().projectId(reviewRoundBatchInVO.getProjectId()).build());
        List<ReviewRound> reviewRoundList = reviewRoundService.list(queryWrapper);
        List<ReviewRoundInsertInVO> reviewRoundInsertInVOS = new ArrayList<>();
        for (ReviewRoundInsertInVO vo : reviewRoundBatchInVO.getInsertList()) {
            Boolean flag = true;
            for (ReviewRound reviewRound : reviewRoundList) {
                if (vo.getRoundId().equals(reviewRound.getRoundId()) && vo.getGroupId().equals(vo.getGroupId()) && vo.getTopicId().equals(reviewRound.getTopicId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                reviewRoundInsertInVOS.add(vo);
            }
        }
        reviewRoundBatchInVO.setInsertList(reviewRoundInsertInVOS);
        return R.ok(reviewRoundService.saveBatchByList(reviewRoundBatchInVO));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "批量删除评审轮次")
    @PostMapping(value = "/remove")
    public R remove(@RequestBody DelReviewRoundIdsVO reviewRoundInVO) {
        List<Long> reviewRoundIds = reviewRoundInVO.getReviewRoundIds();
        QueryWrapper<Slide> queryWrapper = Wrappers.query();
        queryWrapper.select("slide_id", "review_round_id");
        queryWrapper.in("review_round_id", reviewRoundIds);
        List<Slide> slides = slideService.list(queryWrapper);
        boolean flag = false;
        if (slides != null && !slides.isEmpty()) {
            Map<Long, List<Slide>> stringListMap = slides.stream().collect(Collectors.groupingBy(Slide::getReviewRoundId));
            for (Long id : reviewRoundIds) {
                List<Slide> slideList = stringListMap.get(id);
                if (slideList != null && !slideList.isEmpty()) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            return R.fail(MessageSource.M("EXISTS_UNION_DATA"));
        }
        return R.ok(reviewRoundService.removeByIds(reviewRoundInVO.getReviewRoundIds()));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "修改评审轮次")
    @PostMapping("/edit")
    public R edit(@RequestBody ReviewRoundInVO reviewRoundInVO) {
        QueryWrapper<Slide> queryWrapper = Wrappers.query();
        queryWrapper.select("review_round_id");
        queryWrapper.eq("review_round_id", reviewRoundInVO.getReviewRoundId());
        int slides = 0;
        slides = slideService.count(queryWrapper);
        if (slides > 0) {
            return R.fail(MessageSource.M("EXISTS_UNION_DATA"));
        }
        ReviewRound reviewRound = new ReviewRound();
        BeanUtils.copyProperties(reviewRoundInVO, reviewRound);
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        reviewRound.setUpdateBy(sysUser.getUserId());
        reviewRound.setUpdateTime(new Date());
        reviewRound.setOrganizationId(sysUser.getOrganizationId());
        return R.ok(reviewRoundService.updateById(reviewRound));
    }

    @ApiOperation(value = "根据项目id查询轮次树")
    @GetMapping("/reviewRoundTree")
    public R<List<Map<String, Object>>> reviewRoundTree(@RequestParam("projectId") Long projectId) {
        QueryWrapper<ReviewRound> queryWrapper = Wrappers.query();
        queryWrapper.eq("project_id", projectId);
        queryWrapper.select("review_round_id", "review_content", "content_id", "round_id", "topic_id", "group_id");
        List<ReviewRound> reviewRoundList = reviewRoundService.list(queryWrapper);
        List<Map<String, Object>> list = new ArrayList<>();
        if (reviewRoundList != null && !reviewRoundList.isEmpty()) {
            //初始化字典
            init();
            Map<String, List<ReviewRound>> reviewContentGroup = reviewRoundList.stream().collect(Collectors.groupingBy(ReviewRound::getContentId));
            for (String contentId : reviewContentGroup.keySet()) {
                List<ReviewRound> rs = reviewContentGroup.get(contentId);
                if (rs != null && !rs.isEmpty()) {
                    ReviewRound temp = rs.get(0);
                    Map<String, Object> node = new HashMap<>(16);
                    node.put("key", contentId);
                    node.put("label", temp.getReviewContent());
                    //处理轮次信息
                    processRound(rs, node);
                    list.add(node);
                }
            }
        }
        return R.ok(list);
    }

    private void init() {
        List<Topic> topicList = topicService.list();
        for (Topic topic : topicList) {
            topicMap.put(topic.getTopicId(), topic.getTopicName());
        }
    }

    /**
     * 处理轮次信息
     *
     * @param reviewRoundList
     * @param node
     */
    private void processRound(List<ReviewRound> reviewRoundList, Map<String, Object> node) {
        Map<String, List<ReviewRound>> map = new HashMap<>(16);
        for (ReviewRound reviewRound : reviewRoundList) {
            String contentId = reviewRound.getContentId();
            Long roundId = reviewRound.getRoundId();
            String k = contentId + CommonConstant.GLIDE_LINE + roundId + CommonConstant.GLIDE_LINE;
            List<ReviewRound> rounds = map.get(k);
            if (rounds == null) {
                rounds = new ArrayList<>();
            }
            rounds.add(reviewRound);
            map.put(k, rounds);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (String key : map.keySet()) {
            Map<String, Object> roundNode = new HashMap<>(16);
            String[] strings = key.split(CommonConstant.GLIDE_LINE);
            roundNode.put("key", key);
            if (LanguageUtils.isEn()) {
                roundNode.put("label", MapConstant.getRoundNameEn(Long.parseLong(strings[1])));
            } else {
                roundNode.put("label", MapConstant.getRoundName(Long.parseLong(strings[1])));
            }
            //处理下级专题
            processTopic(map.get(key), roundNode);
            list.add(roundNode);
        }
        node.put("children", list);
    }

    /**
     * 处理专题信息
     *
     * @param reviewTopicList
     * @param node
     */
    private void processTopic(List<ReviewRound> reviewTopicList, Map<String, Object> node) {
        Map<String, List<ReviewRound>> map = new HashMap<>(16);
        for (ReviewRound reviewRound : reviewTopicList) {
            String contentId = reviewRound.getContentId();
            Long roundId = reviewRound.getRoundId();
            Long topicId = reviewRound.getTopicId();
            String k = contentId + CommonConstant.GLIDE_LINE + roundId + CommonConstant.GLIDE_LINE + topicId;
            List<ReviewRound> rounds = map.get(k);
            if (rounds == null) {
                rounds = new ArrayList<>();
            }
            rounds.add(reviewRound);
            map.put(k, rounds);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (String key : map.keySet()) {
            Map<String, Object> topicNode = new HashMap<>(16);
            String[] strings = key.split(CommonConstant.GLIDE_LINE);
            topicNode.put("key", key);
            topicNode.put("label", topicMap.get(Long.parseLong(strings[2])));
            processGroup(map.get(key), topicNode);
            list.add(topicNode);
        }
        node.put("children", list);
    }

    /**
     * 处理组信息
     *
     * @param reviewGroupList
     * @param node
     */
    private void processGroup(List<ReviewRound> reviewGroupList, Map<String, Object> node) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReviewRound reviewRound : reviewGroupList) {
            Map<String, Object> groupNode = new HashMap<>(16);
            groupNode.put("key", String.valueOf(reviewRound.getReviewRoundId()));
            groupNode.put("label", MapConstant.getGroupName(reviewRound.getGroupId()));
            list.add(groupNode);
        }
        node.put("children", list);
    }
}
