package cn.staitech.anno.service;

import cn.staitech.anno.domain.ExaminationLog;
import cn.staitech.anno.domain.vo.ExaminationLogVO;

import java.util.List;

/**
 * 复核流水 服务层 .
 *
 * @author staitech
 */
public interface ExaminationLogService {

    /**
     * 更新切片审核日志
     *
     * @param examinationLog
     * @return
     */
    int insertExaminationLog(ExaminationLog examinationLog);


    /**
     * 根据切片id获取数据
     *
     * @param slideId
     * @return
     */
    List<ExaminationLogVO> selectBySlideId(Long slideId);
}