package cn.staitech.anno.service.impl;

import cn.staitech.anno.mapper.ExaminationMapper;
import cn.staitech.anno.service.ExaminationService;
import cn.staitech.anno.vo.examination.ExaminationListVO;
import cn.staitech.anno.vo.examination.ExaminationSelectVO;
import cn.staitech.anno.vo.examination.ExaminationStateVO;
import cn.staitech.anno.vo.examination.ExaminationSubmitVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 复核 服务层实现
 *
 * @author staitech
 */
@Service
public class ExaminationServiceImpl implements ExaminationService {
    @Resource
    private ExaminationMapper examinationMapper;

    /**
     * 切片提交复核
     *
     * @param examinationSubmitVo
     * @return
     */
    @Override
    public int updateExaminationSubmit(ExaminationSubmitVO examinationSubmitVo) {
        return examinationMapper.updateExaminationSubmit(examinationSubmitVo);
    }

    /**
     * 查询复核图像列表
     *
     * @param examinationSelectVo
     * @return
     */
    @Override
    public List<ExaminationListVO> selectExaminationList(ExaminationSelectVO examinationSelectVo) {
        return examinationMapper.selectExaminationList(examinationSelectVo);
    }

    /**
     * 修改复核图像状态
     *
     * @param examinationStateVo
     * @return
     */
    public int updateExaminationState(ExaminationStateVO examinationStateVo) {
        return examinationMapper.updateExaminationState(examinationStateVo);
    }

}
