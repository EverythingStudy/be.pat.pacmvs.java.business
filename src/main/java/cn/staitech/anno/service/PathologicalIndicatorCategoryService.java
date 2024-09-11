package cn.staitech.anno.service;

import java.text.ParseException;
import java.util.List;

import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.vo.annotation.CategoryVO;
import cn.staitech.anno.vo.annotation.LabelListVO;
import cn.staitech.anno.vo.annotation.LabelVO;
import cn.staitech.anno.vo.indicator.PathologicalIndicatorCategoryVO;
import cn.staitech.common.core.domain.R;


public interface PathologicalIndicatorCategoryService {
    /**
     * 添加标签信息
     *
     * @param Pathological 标签ID
     * @return 标签信息
     */
    int insertSelective(PathologicalIndicatorCategory Pathological);

    /**
     * 修改标签信息
     *
     * @param Pathological 标签ID
     * @return 标签信息
     */
    String updateByPrimaryKeySelective(PathologicalIndicatorCategory Pathological);

    /**
     * 修改标签信息(结构、标注、考核同步)
     *
     * @param Pathological 标签ID
     * @return 标签信息
     */
    String updateByPrimaryKeySelective2(PathologicalIndicatorCategory Pathological);

    /**
     * 删除标签信息
     *
     * @param categoryId 标签ID
     * @return 标签信息
     */
    String deleteByPrimaryKey(Long categoryId);

    /**
     * 查询标签详细信息
     *
     * @param categoryId 标签ID
     * @return 标签信息
     */
    PathologicalIndicatorCategory selectByPrimaryKey(Long categoryId);

    /**
     * 根据病理指标id获取全部信息
     *
     * @param indicatorId 病理指标ID
     * @return 标签信息
     */
    List<PathologicalIndicatorCategory> selectIndicatorIdAll(Long indicatorId);

    /**
     * 根据病理指标id删除信息
     *
     * @param indicatorId 病理指标ID
     * @return 标签信息
     */
    int delIndicatorCategory(Long indicatorId);

    PathologicalIndicatorCategory selectCategoryAll(Long CategoryId);


    /**
     * 条件查询标注类别
     *
     * @param Pathological 病例指标id 或 颜色 或 标注类别名称
     * @return 结果
     */
    List<PathologicalIndicatorCategory> selectIndicatorMessage(PathologicalIndicatorCategory Pathological);


    /**
     * 条件查询标注类别-用于修改
     *
     * @param Pathological 病例指标id 或 颜色 或 标注类别名称
     * @return 结果
     */
    List<PathologicalIndicatorCategory> selectIndicatorMessageForUpdate(PathologicalIndicatorCategory Pathological);



    /**
     * 查询病例指标下的标注类别数量
     */
    Long selectCategoryNumber(Long indicatorId);

    /**
     * 根据projectId查询标注类别
     */
    List<PathologicalIndicatorCategory> selectProjectCategory(Long projectId);
    
    
    /**
     * 根据indicatorId查询标注类别（不包含unLabel）
     */
    List<LabelListVO> selectByIndicator(LabelVO labelVO);




    /**
     * 查询标签所属脏器系统内已有标签数量
     */
    Integer selectLabelNumByStructureId(String structureId);

    void handlerCouponsUserStatusTimeOutToExpired(List<Long> list) throws ParseException;
    
    R<String> add(PathologicalIndicatorCategoryVO vo);
    R<String> edit(PathologicalIndicatorCategory vo);
    R<String> del(CategoryVO categoryVO);
    R<PathologicalIndicatorCategory> getInfo(Long categoryId);
}
