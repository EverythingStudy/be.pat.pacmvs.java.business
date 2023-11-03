package cn.staitech.anno.service;

import cn.staitech.anno.vo.examination.ExaminationListVO;
import cn.staitech.anno.vo.examination.ExaminationSelectVO;
import cn.staitech.anno.vo.examination.ExaminationStateVO;
import cn.staitech.anno.vo.examination.ExaminationSubmitVO;

import java.util.List;

/**
 * 复核 服务层 .
 *
 * @author staitech
 */
public interface ExaminationService {

    /**
     * 切片提交复核 .
     *
     * @param
     * @return
     */
    int updateExaminationSubmit(ExaminationSubmitVO examinationSubmitVo);

    /**
     * 查询复核图像列表 .
     *
     * @param examinationSelectVo
     * @return
     */
    List<ExaminationListVO> selectExaminationList(ExaminationSelectVO examinationSelectVo);

    /**
     * 修改复核图像状态 .
     *
     * @param examinationStateVo
     * @return
     */
    int updateExaminationState(ExaminationStateVO examinationStateVo);
}