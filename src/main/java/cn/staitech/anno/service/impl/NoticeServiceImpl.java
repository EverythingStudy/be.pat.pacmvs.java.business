package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.notice.Notice;
import cn.staitech.anno.domain.notice.in.NoticeChangeStatusIn;
import cn.staitech.anno.domain.notice.out.NoticeListQueryOut;
import cn.staitech.anno.domain.notice.out.NoticeQueryOut;
import cn.staitech.anno.domain.notice.out.data.NoticeQueryOutData;
import cn.staitech.anno.mapper.NoticeMapper;
import cn.staitech.anno.mapper.SystemDictMapper;
import cn.staitech.anno.service.NoticeService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author wudi
 * @Date 2023/6/6 10:11
 * @desc 消息通知业务层
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    private static final Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Resource
    private SystemDictMapper systemDictMapper;
    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 消息获取
     *
     * @return
     */
    @Override
    public List<NoticeQueryOut> getNotice() {
        log.info("消息查询接口开始：");
        Long userId = SecurityUtils.getUserId();
        //Long userId = 1L;
        //创建响应
        List<NoticeQueryOut> resp = new ArrayList<>();

        List<NoticeQueryOut> resps = systemDictMapper.selectList(userId);
        if (!CollectionUtils.isEmpty(resps)) {
            Map<String, NoticeQueryOut> collect = resps.parallelStream().collect(Collectors.toMap(NoticeQueryOut::getSpecialNumber, Function.identity(), (c1, c2) -> c1.getExpireTime().after(c2.getExpireTime()) ? c1 : c2));
            resp = new ArrayList<>(collect.values());
        }
        return resp;

    }

    @Override
    public NoticeListQueryOut getNoticeList() {
        log.info("消息查询接口开始：");
        //创建响应
        NoticeListQueryOut ret = new NoticeListQueryOut();
        //当前登陆人
        Long userId = SecurityUtils.getUserId();
        //Long userId = 11L;
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<Notice>();
        //未读消息
        wrapper.eq(Notice::getRecipient, userId);
        wrapper.eq(Notice::getStatus, "0");
        wrapper.eq(Notice::getNoticeType, "1");
        List<Notice> notices = noticeMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(notices)) {
            List<NoticeQueryOutData> collect = notices.stream().map(e -> {
                NoticeQueryOutData noticeRet = new NoticeQueryOutData();
                BeanUtils.copyBeanProp(noticeRet, e);
                return noticeRet;
            }).collect(Collectors.toList());
            ret.setUnreadList(collect);
        }
        //已读消息
        List<Notice> strings = noticeMapper.selectReadList(userId);
        List<NoticeQueryOutData> readList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(strings)) {
            Map<String, List<Notice>> collect1 = strings.stream().collect(Collectors.groupingBy(Notice::getNoticeContent));
            collect1.forEach((k, v) -> {
                NoticeQueryOutData noticeQueryOutData = new NoticeQueryOutData();
                noticeQueryOutData.setNoticeId(v.get(0).getNoticeId());
                noticeQueryOutData.setNoticeContent(v.get(0).getNoticeContent());
                readList.add(noticeQueryOutData);
            });
            ret.setReadList(readList);

        }
        //公告消息

        return ret;
    }

    @Override
    public R<NoticeListQueryOut> changeStatus(NoticeChangeStatusIn req) {
        log.info("消息状态置已读接口开始：");
        List<Long> noticeList = req.getNoticeList();
        if (!CollectionUtils.isEmpty(noticeList)) {
            noticeList.forEach(e -> {
                noticeMapper.batchUpdate(e);

            });
        }
        return R.ok();
    }
}
