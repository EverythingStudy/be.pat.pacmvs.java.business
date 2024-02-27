package cn.staitech.anno.service;

import cn.staitech.anno.domain.MarkingExamine;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.in.UpdateOperationIn;
import cn.staitech.anno.vo.geojson.out.BatchResult;
import cn.staitech.anno.vo.history.HistoryDTO;
import cn.staitech.anno.vo.marking.MarkingExamineInsertVO;
import cn.staitech.anno.vo.marking.MarkingMerge;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vividsolutions.jts.io.ParseException;

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
     * @param req
     * @param traceId
     * @param isBatch
     * @param isUndo
     * @return
     */
    Long insertByHistory(MarkingExamine markingExamine, String traceId, Boolean isBatch, Boolean isUndo) throws Exception;

    /**
     * 删除标注
     *
     * @param marking 标注数据
     * @return true || false
     */
    Long update(MarkingExamineInsertVO marking) throws Exception;

    Long updateByHistory(MarkingExamine markingExamine, String traceId, Boolean isBatch, Boolean isUndo) throws Exception;

    /**
     * 填充标注
     *
     * @param markingId 标注id
     * @return true || false
     */
    int padding(Long markingId) throws Exception;

    /**
     * 填充标注
     *
     * @param markingId 标注id
     * @return true || false
     */
    int stickup(String markingId);

    /**
     * 多个轮廓合并
     *
     * @param markingId 标注id
     * @return true || false
     */
    JSONObject markingMerge(MarkingMerge req) throws ParseException;

    /**
     * 合并、裁剪轮廓
     */
    JSONObject updateOperation(UpdateOperationIn req, String traceId, Boolean isBatch) throws Exception;

    JSONObject updateOperationByHistory(MarkingExamine req, String traceId, Boolean isBatch, Boolean isUndo) throws Exception;

    double operationCheck(UpdateOperationIn req) throws Exception;

    /**
     * 删除标注
     *
     * @param markingExamineId 标注id
     * @return true || false
     */
    int delete(Long markingExamineId, String traceId, Boolean isBatch) throws Exception;

    int deleteByHistory(Long markingExamineId, String traceId, Boolean isBatch, Boolean isUndo) throws Exception;

    /**
     * 批量处理
     *
     * @param list
     * @return
     */
    List<BatchResult> batch(List<MarkingExamineInsertVO> list);

    Boolean process(HistoryDTO dto);

    Boolean undo(HistoryDTO dto);

    Boolean redo(HistoryDTO dto);
}
