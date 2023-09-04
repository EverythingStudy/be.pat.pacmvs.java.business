package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.vo.*;

import java.util.List;

/**
 * 复核 数据层
 *
 * @author staitech
 */
public interface ExaminationMapper {
    /**
     * 切片提交复核 .
     *
     * @param examinationSubmitVo
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