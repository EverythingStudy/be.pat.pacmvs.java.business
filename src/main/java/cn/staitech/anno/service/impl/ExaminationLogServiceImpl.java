package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ExaminationLog;
import cn.staitech.anno.mapper.ExaminationLogMapper;
import cn.staitech.anno.service.ExaminationLogService;
import cn.staitech.anno.vo.examination.ExaminationLogVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 复核流水 服务层实现
 *
 * @author staitech
 */
@Service
public class ExaminationLogServiceImpl implements ExaminationLogService {
    @Resource
    private ExaminationLogMapper examinationLogMapper;

    /**
     * 更新切片审核日志
     *
     * @param examinationLog
     * @return
     */
    @Override
    public int insertExaminationLog(ExaminationLog examinationLog) {
        return examinationLogMapper.insertExaminationLog(examinationLog);
    }


    /**
     * 根据切片id获取数据
     *
     * @param slideId
     * @return
     */
    @Override
    public List<ExaminationLogVO> selectBySlideId(Long slideId) {
        return examinationLogMapper.selectBySlideId(slideId);
    }
}
