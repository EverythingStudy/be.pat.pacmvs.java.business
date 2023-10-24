package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.notice.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author wudi
 * @Date 2023/6/26 18:03
 * @desc 消息
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    List<Notice> selectReadList(Long recipient);

    int batchUpdate(@Param("noticeId") Long noticeId);

}
