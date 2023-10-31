package cn.staitech.anno.service;

import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.special.SpecialAnnotation;
import cn.staitech.anno.domain.special.SpecialImage;
import cn.staitech.anno.domain.specialimage.SpecialImageSelectVO;
import cn.staitech.anno.domain.specialimage.WaitSpecialImageVO;
import cn.staitech.anno.domain.specialimageanno.AnnoMarkGeojson;
import cn.staitech.anno.domain.specialimageanno.AnnoProperties;
import cn.staitech.anno.domain.specialimageanno.SpecialAnnoAddVO;
import cn.staitech.anno.domain.specialimageanno.in.AlgorithmAnnIn;
import cn.staitech.anno.domain.specialimageanno.in.CallBackAnnAddIn;
import cn.staitech.anno.domain.specialimageanno.in.SpecialAnnoProperties;
import cn.staitech.anno.domain.specialsliceimage.AuditSpecialImageVO;
import cn.staitech.anno.domain.specialsliceimage.OrganDict;
import cn.staitech.common.core.domain.R;

import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SpecialImageAnnoService
 * @Description:专题选片标注
 * @date 2023年6月12日
 */
public interface SpecialImageAnnoService {

    R<List<SpecialAnnoAddVO>> annotationSave(List<SpecialAnnoAddVO> list, SpecialImage sImage) throws Exception;

    /**
     * 添加标注
     *
     * @param marking 标注数据
     * @return true || false
     */

    int insertSpecialAnnotation(SpecialAnnotation anno);

    /**
     * 更新标注点数
     *
     * @param marking 标注数据
     * @return true || false
     */
    int updatePointCount(SpecialAnnotation specialAnnotation);

    /**
     * 根据切片id查询当前切片下当前标签的总数
     *
     * @param marking 标注信息
     * @return PointCount
     */
    PointCount selectCategoryCount(SpecialAnnotation anno);

    /**
     * 获取geojsonUrl
     *
     * @param geojsonUrls 文件地址,slideId 切片id
     * @return geojsonUrl
     */
    String getGeojsonUrls(String geojsonUrls, Long slideId);

    /**
     * 修改标注
     *
     * @param marking 标注数据
     * @return true || false
     */
    int update(SpecialAnnotation ann);


    /**
     * 修改标注 .
     *
     * @param annotation 修改标注
     * @return 结果
     * @throws Exception 添加事务
     */
    R<String> updateAnnotation(SpecialAnnotation anno);


    /**
     * @param @param  specialImageList
     * @param @return
     * @return int
     * @throws
     * @Title: insertSpecialImageList
     * @Description: 批量审核专题选片列表
     */
    R<String> updateSpecialImageList(AuditSpecialImageVO vo);


    /**
     * @param @param  specialImageList
     * @param @return
     * @return int
     * @throws
     * @Title: insertSpecialImageList
     * @Description: 切片数据交付
     */
    R<String> updateDeliveryBySpecialId(AuditSpecialImageVO vo);


    /**
     * @param @param  specialImageSelectVO
     * @param @return
     * @return List<WaitSpecialImageVO>
     * @throws
     * @Title: selectWaitSpecialImage
     * @Description: 查询全部待选的专题列表（根据切片编号/上传时间/添加状态查询)
     */
    List<WaitSpecialImageVO> selectWaitSpecialImage(SpecialImageSelectVO specialImageSelectVO);


    /**
     * @param @param  specialImageSelectVO
     * @param @return
     * @return List<WaitSpecialImageVO>
     * @throws
     * @Title: selectWaitSpecialImage
     * @Description: 查询全部专题选片（根据切片编号/添加时间）
     */
    List<SpecialAnnotation> selectSpecialAnnotationList(SpecialAnnotation annotation);


    List<SpecialAnnoProperties> selectSpecialPropertiesList(SpecialAnnotation annotation);


    SpecialAnnotation selectByPrimaryKey(Long specialAnnotationId);


    int deleteAnnotationById(Long annotationId);


    List<OrganDict> getSystemDict(OrganDict dict);

    AnnoMarkGeojson getMarkGeojsonByList(List<SpecialAnnotation> list);

    AnnoProperties getPropertiesBy(SpecialAnnotation req);

    R callBackSlideViscer(List<CallBackAnnAddIn> list);

    void callBackAnnoResult(AlgorithmAnnIn algorithmAnnIn);

}
