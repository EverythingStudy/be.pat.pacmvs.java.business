package cn.staitech.anno.service;

import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.viewAddIn;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.marking.SlideRes;
import cn.staitech.anno.domain.vo.marking.out.MarkingSelectListVo;
import cn.staitech.anno.project.domain.DownTask;

import java.util.List;

public interface MarkingService {

    /**
     * 查看当前切片下所有的切片
     *
     * @param slideId 标注信息
     * @return List<Slide>
     */
    List<MarkingSelectListVo> selectList(Long slideId) throws Exception;


    List<Features> selectListBy(Long slideId) throws Exception;


    /**
     * 根据专题查看当前专题下所有的切片
     *
     * @param specialId 标注信息
     * @return List<Slide>
     */
    List<SlideRes> selectSlideList(Long specialId);

    /**
     * 根据切片id查询当前切片下当前标签的总数
     *
     * @param marking 标注信息
     * @return PointCount
     */
    PointCount selectCategoryCount(Marking marking);

    /**
     * 根据切片id查询当前切片下的标签总数
     *
     * @param slideId 切片id
     * @return List<PointCount>
     */
    List<PointCount> selectCategoryCountList(Long slideId);

    /**
     * 根据主键查询详情信息
     *
     * @param markingId 标注id
     * @return true || false
     */
    Marking selectById(Long markingId);

    /**
     * 添加标注
     *
     * @param req 标注数据
     * @return true || false
     */
    Long insert(viewAddIn req) throws Exception;

    /**
     * 删除标注
     *
     * @param marking 标注数据
     * @return true || false
     */
    Long update(MarkingUpdateIn marking) throws Exception;

    /**
     * 更新标注点数
     *
     * @param marking 标注数据
     * @return true || false
     */
    int updatePointCount(Marking marking);

    /**
     * 删除标注
     *
     * @param markingId 标注id
     * @return true || false
     */
    int delete(Long markingId) throws Exception;

    /**
     * 导出json数据
     *
     * @param slideId
     * @return
     */
    String slideJsonExport(Long slideId) throws Exception;

    /**
     * 导入zip压缩包
     *
     * @param zipUrl
     * @param specialId
     * @return
     * @throws Exception
     */
    boolean zipExport(String zipUrl, Long specialId) throws Exception;

    /**
     * 导出execl
     */
    void execlExport(Long slideId) throws Exception;

    DownTask projectJsonExport(Long projectId, List<Long> slideIds) throws Exception;

    void downTaskByCode(String code) throws Exception;

    /**
     * 删除页面所有标注
     *
     * @param slideId 切片id
     * @return
     */
    void batchDelete(Long slideId);
}
