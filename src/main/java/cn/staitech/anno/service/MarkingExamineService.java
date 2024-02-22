package cn.staitech.anno.service;

import cn.staitech.anno.domain.MarkingExamine;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.in.UpdateOperationIn;
import cn.staitech.anno.vo.geojson.out.BatchResult;
import cn.staitech.anno.vo.history.HistoryDTO;
import cn.staitech.anno.vo.marking.MarkingExamineInsertVO;
import cn.staitech.common.core.domain.R;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author gjt
 * @since 2023-09-25
 */
public interface MarkingExamineService extends IService<MarkingExamine> {

    /**
     * 查询文件中的json内容
     */
    JSONArray selectQuestionMarkingList(Long questionId) throws Exception;

    List<Features> selectLists(Long questionProjectId, Long createBy) throws Exception;

    /**
     * 添加标注
     *
     * @param req 标注数据
     * @return true || false
     */
    Long insert(MarkingExamineInsertVO req) throws Exception;

    /**
     * 删除标注
     *
     * @param marking 标注数据
     * @return true || false
     */
    Long update(MarkingExamineInsertVO marking) throws Exception;

    /**
     * 合并、裁剪轮廓
     */
    JSONObject updateOperation(UpdateOperationIn req) throws Exception;


    double operationCheck(UpdateOperationIn req) throws Exception;

    /**
     * 删除标注
     *
     * @param markingExamineId 标注id
     * @return true || false
     */
    int delete(Long markingExamineId) throws Exception;

    /**
     * 批量处理
     *
     * @param list
     * @return
     */
    List<BatchResult> batch(List<MarkingExamineInsertVO> list);

    void undo(HistoryDTO dto);

    void redo(HistoryDTO dto);
}
