package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.MarkMeasure;
import cn.staitech.anno.domain.MarkingStatistic;
import cn.staitech.anno.project.vo.ImageAnnoStatisticsVO;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.JsonExport;
import cn.staitech.anno.vo.geojson.Properties;
import cn.staitech.anno.vo.geojson.in.RoiIn;
import cn.staitech.anno.vo.labelprojectstatistics.ProjectLabelOut;
import cn.staitech.anno.vo.marking.Marking;
import cn.staitech.anno.vo.marking.MarkingSelectListVO;
import cn.staitech.anno.vo.marking.MarkingStatisticSelectVO;
import cn.staitech.anno.vo.marking.PointCount;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * viewer 标注页面 .
 *
 * @author gjt
 */
@DS("sharding")
public interface MarkingMapper extends BaseMapper<Marking> {

    /**
     * 查看当前切片下所有的切片
     *
     * @param map
     * @return
     */
    List<MarkingSelectListVO> selectList(Map<String, Object> map);

    int selectListCount(Map<String, Object> map);

    /**
     * json文件导出的列表
     *
     * @param slideId
     * @return
     */
    List<Features> selectLists(Long slideId);

    List<Features> selectFilterCategoryLists(Map<String, Object> map);


    /**
     * 与前端交互使用的列表
     *
     * @param slideId
     * @return
     */
    List<Features> selectListBy(Long slideId);

    List<Features> selectListMarking(Map<String, Object> map);
    
    List<cn.staitech.anno.domain.PathologicalIndicatorCategory> getCategoryByMap(Map<String, Object> map);


    List<MarkingSelectListVO> selectUnionList(Map<String, Object> map);


    List<MarkingSelectListVO> selectPointCountList(Map<String, Object> map);

    Properties selectBy(String markingId);

    Marking selectByIds(String markingIds);

    List<Properties> selectMeasureList(Long slideId);

    JsonExport jsonExportSelect(Long slide);

    JsonExport reviewJsonExportSelect(Long slideId);

    List<cn.staitech.anno.domain.PathologicalIndicatorCategory> selectCategory(Long slideId);

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
    int delete(String markingId);

    /**
     * 标签统计
     *
     * @param selectVO
     * @return
     */
    long selectMarkingStatisticTotal(MarkingStatisticSelectVO selectVO);

    /**
     * 标签统计
     *
     * @param selectVO
     * @return
     */
    List<MarkingStatistic> selectMarkingStatistic(MarkingStatisticSelectVO selectVO);

    /**
     * 标签统计 - count
     *
     * @param marking
     * @return
     */
    Long selectMarkingNum(cn.staitech.anno.vo.marking.Marking marking);

    /**
     * ROI查询
     * */
    List<Marking> roiMarking(RoiIn roiIn);



    /**
     * ROI对measure查询
     * */
    List<MarkMeasure>roiMeasure(RoiIn roiIn);

    int delMeasure(List<String> list);

    List<ProjectLabelOut> getProjectCategoryMarkingNum(Long projectId);
    List<ProjectLabelOut> getProjectCategoryImageNum(Long projectId);


    List<ImageAnnoStatisticsVO> getProjectAnnoMarkingNum(Long projectId);
    List<ImageAnnoStatisticsVO> getProjectAnnoImageNum(Long projectId);


    List<MarkingStatistic> getProjectUserLabelMarkingNum(Long projectId);


    List<ProjectLabelOut> getProjectMarkingNum(Long projectId);
    List<ProjectLabelOut> getProjectImageNum(Long projectId);
    
}
