package cn.staitech.anno.service;

import cn.staitech.anno.domain.MarkMeasure;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.vo.geojson.in.UpdateOperationIn;
import cn.staitech.anno.vo.geojson.in.ViewAddIn;
import cn.staitech.anno.vo.marking.MarkingSelectListVO;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 标注测量表 服务类
 * </p>
 *
 * @author wanglibei
 * @since 2023-12-05
 */
public interface MarkMeasureService extends IService<MarkMeasure> {

    /**
     * 查看当前切片下所有的数据
     */
    PageResponse<MarkingSelectListVO> list(Long slideId, Integer pageNum, Integer pageSize, String measureFullName) throws Exception;


    List<Features> selectListBy(Long slideId) throws Exception;

    double operationCheck(UpdateOperationIn req) throws Exception;


    /**
     * 添加标注
     *
     * @param req 标注数据
     * @return true || false
     */
    String insert(ViewAddIn req) throws Exception;

    /**
     * 删除标注
     *
     * @param marking 标注数据
     * @return true || false
     */
    String update(MarkingUpdateIn marking) throws Exception;

    JSONObject updateOperation(UpdateOperationIn req) throws Exception;


    /**
     * 删除标注
     *
     * @param markingId 标注id
     * @return true || false
     */
    int delete(String markingId) throws Exception;


    /**
     * 导出json数据
     *
     * @param slideId
     * @return
     */
    String slideJsonExport(Long slideId, SysUser sysUser) throws Exception;


    /**
     * 导出execl
     */
    void execlExport(Long slideId, HttpServletResponse response) throws Exception;


}
