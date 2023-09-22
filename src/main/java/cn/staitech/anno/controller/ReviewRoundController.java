package cn.staitech.anno.controller;

import cn.staitech.anno.domain.Group;
import cn.staitech.anno.domain.ReviewRound;
import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.domain.reviewround.ReviewRoundBatchInVO;
import cn.staitech.anno.domain.reviewround.ReviewRoundInVO;
import cn.staitech.anno.domain.reviewround.ReviewRoundOutVO;
import cn.staitech.anno.domain.round.Round;
import cn.staitech.anno.service.GroupService;
import cn.staitech.anno.service.ReviewRoundService;
import cn.staitech.anno.service.RoundService;
import cn.staitech.anno.service.TopicService;
import cn.staitech.anno.utils.PageMaster;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Resource
    private ReviewRoundService reviewRoundService;

    @Resource
    private TopicService topicService;
    @Resource
    private GroupService groupService;
    @Resource
    private RoundService roundService;

    public static Map<Long,String> roundMap = new HashMap<>();
    public static Map<Long,String> topicMap = new HashMap<>();
    public static Map<Long,String> groupMap = new HashMap<>();



    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "查询评审轮次列表")
    @GetMapping("/getById")
    public R<ReviewRoundOutVO> one(@RequestParam("reviewRoundId") Long reviewRoundId) {
        return R.ok(reviewRoundService.getOneById(reviewRoundId));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "查询评审轮次列表")
    @GetMapping("/list")
    public R<PageMaster<ReviewRoundOutVO>> list(@NotNull(message = "分页参数为空！") @RequestParam("pageNum") @ApiParam(name = "pageNum", value = "分页参数", required = true) Integer pageNum,
                                                @NotNull(message = "分页参数为空！") @RequestParam("pageSize") @ApiParam(name = "pageSize", value = "分页参数", required = true) Integer pageSize,
                                                @RequestParam("projectId") Long projectId) {
        return R.ok(reviewRoundService.pageReviewRound(pageNum, pageSize, projectId));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "添加评审轮次")
    @PostMapping("/add")
    public R add(@RequestBody ReviewRoundBatchInVO reviewRoundBatchInVO) {
        return R.ok(reviewRoundService.saveBatchByList(reviewRoundBatchInVO));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "批量删除评审轮次")
    @GetMapping(value = "/remove")
    public R remove(@RequestParam("reviewRoundIds") List<Long> reviewRoundIds) {
        return R.ok(reviewRoundService.removeByIds(reviewRoundIds));
    }

    @ApiOperationSupport(author = "wangfeng")
    @ApiOperation(value = "修改评审轮次")
    @PostMapping("/edit")
    public R edit(@RequestBody ReviewRoundInVO reviewRoundInVO) {
        ReviewRound reviewRound = new ReviewRound();
        BeanUtils.copyProperties(reviewRoundInVO, reviewRound);
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        reviewRound.setCreateBy(sysUser.getUserId());
        reviewRound.setOrganizationId(sysUser.getOrganizationId());
        return R.ok(reviewRoundService.updateById(reviewRound));
    }

    @ApiOperation(value = "根据项目id查询轮次树")
    @GetMapping("/reviewRoundTree")
    public R<List<Map<String,Object>>> reviewRoundTree(@RequestParam("projectId") Long projectId) {
        QueryWrapper<ReviewRound> queryWrapper = Wrappers.query();
        queryWrapper.eq("project_id",projectId);
        queryWrapper.select("review_round_id","review_content","content_id","round_id","topic_id","group_id");
        List<ReviewRound> reviewRoundList = reviewRoundService.list(queryWrapper);
        List<Map<String,Object>> list = new ArrayList<>();
        if (reviewRoundList!=null&&!reviewRoundList.isEmpty()){
            //初始化字典
            init();
            Map<String, List<ReviewRound>> reviewContentGroup = reviewRoundList.stream().collect(Collectors.groupingBy(ReviewRound::getContentId));
            for (String contentId:reviewContentGroup.keySet()){
                List<ReviewRound> rs = reviewContentGroup.get(contentId);
                if (rs!=null&&!rs.isEmpty()){
                    ReviewRound temp = rs.get(0);
                    Map<String,Object> node = new HashMap<>();
                    node.put("key",contentId);
                    node.put("label",temp.getReviewContent());
                    //处理轮次信息
                    processRound(rs,node);
                    list.add(node);
                }
            }
        }
        return R.ok(list);
    }

    private void init(){
        List<Round> roundList =roundService.list();
        for (Round round : roundList) {
            roundMap.put(round.getRoundId(),round.getRoundName());
        }
        List<Group> groupList =groupService.list();
        for (Group group : groupList) {
            groupMap.put(group.getGroupId(),group.getGroupName());
        }
        List<Topic> topicList =topicService.list();
        for (Topic topic : topicList) {
            topicMap.put(topic.getTopicId(),topic.getTopicName());
        }
    }
    //处理轮次信息
    private void processRound(List<ReviewRound> reviewRoundList,Map<String,Object> node){
        Map<String, List<ReviewRound>> map = new HashMap<>();
        for (ReviewRound reviewRound : reviewRoundList) {
            String contentId = reviewRound.getContentId();
            Long roundId = reviewRound.getRoundId();
            //Long id = reviewRound.getReviewRoundId();
            String k = contentId+"_"+roundId+"_";
            List<ReviewRound> rounds = map.get(k);
            if (rounds==null){
                rounds = new ArrayList<>();
            }
            rounds.add(reviewRound);
            map.put(k,rounds);
        }
        List<Map<String,Object>> list = new ArrayList<>();
        for (String key:map.keySet()){
            Map<String,Object> roundNode = new HashMap<>();
            String[] strings = key.split("_");
            //roundNode.put("key",strings[1]);
            roundNode.put("key",key);
            roundNode.put("label",roundMap.get(Long.parseLong(strings[1])));
            //处理下级专题
            processTopic(map.get(key),roundNode);
            list.add(roundNode);
        }
        node.put("children",list);
    }

    //处理专题信息
    private void processTopic(List<ReviewRound> reviewTopicList,Map<String,Object> node){
        Map<String, List<ReviewRound>> map = new HashMap<>();
        for (ReviewRound reviewRound : reviewTopicList) {
            String contentId = reviewRound.getContentId();
            Long roundId = reviewRound.getRoundId();
            //Long id = reviewRound.getReviewRoundId();
            Long topicId = reviewRound.getTopicId();
            String k = contentId+"_"+roundId+"_"+topicId;
            List<ReviewRound> rounds = map.get(k);
            if (rounds==null){
                rounds = new ArrayList<>();
            }
            rounds.add(reviewRound);
            map.put(k,rounds);
        }
        List<Map<String,Object>> list = new ArrayList<>();
        for (String key:map.keySet()){
            Map<String,Object> topicNode = new HashMap<>();
            String[] strings = key.split("_");
            //topicNode.put("key",strings[2]);
            topicNode.put("key",key);
            topicNode.put("label",topicMap.get(Long.parseLong(strings[2])));
            processGroup(map.get(key),topicNode);
            list.add(topicNode);
        }
        node.put("children",list);
    }

    //处理组信息
    private void processGroup(List<ReviewRound> reviewGroupList,Map<String,Object> node){
        List<Map<String,Object>> list = new ArrayList<>();
        for (ReviewRound reviewRound : reviewGroupList) {
            Map<String,Object> groupNode = new HashMap<>();
            groupNode.put("key",String.valueOf(reviewRound.getReviewRoundId()));
            groupNode.put("label",groupMap.get(reviewRound.getGroupId()));
            list.add(groupNode);
        }
        node.put("children",list);
    }
}
