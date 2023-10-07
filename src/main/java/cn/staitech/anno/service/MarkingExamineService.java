package cn.staitech.anno.service;

import cn.staitech.anno.domain.MarkingExamine;
import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.viewAddIn;
import cn.staitech.anno.domain.markingExamine.MarkingExamineInsertVO;
import cn.staitech.anno.domain.markingExamine.MarkingExamineUpdateVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gjt
 * @since 2023-09-25
 */
public interface MarkingExamineService extends IService<MarkingExamine> {

    List<Features> selectLists(Long questionProjectId) throws Exception;

    /**
     * 添加标注
     * @param req 标注数据
     * @return true || false
     */
    Long insert(MarkingExamineInsertVO req) throws Exception;

    /**
     * 删除标注
     * @param marking 标注数据
     * @return true || false
     */
    Long update(MarkingExamineUpdateVO marking) throws Exception;


    /**
     * 删除标注
     * @param markingExamineId 标注id
     * @return true || false
     */
    int delete(Long markingExamineId) throws Exception;

}
