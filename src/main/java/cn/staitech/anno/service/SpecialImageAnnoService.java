package cn.staitech.anno.service;

import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.specialAnnotation.SpecialAnnotation;
import cn.staitech.anno.domain.specilaImage.SpecialImage;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageSelectVO;
import cn.staitech.anno.domain.vo.specialImage.WaitSpecialImageVO;
import cn.staitech.anno.domain.vo.specialImageAnno.AnnoMarkGeojson;
import cn.staitech.anno.domain.vo.specialImageAnno.AnnoProperties;
import cn.staitech.anno.domain.vo.specialImageAnno.SpecialAnnoAddVO;
import cn.staitech.anno.domain.vo.specialImageAnno.in.AlgorithmAnnIn;
import cn.staitech.anno.domain.vo.specialImageAnno.in.CallBackAnnAddIn;
import cn.staitech.anno.domain.vo.specialImageAnno.in.SpecialAnnoProperties;
import cn.staitech.anno.domain.vo.specialSliceImage.AuditSpecialImageVO;
import cn.staitech.anno.domain.vo.specialSliceImage.OrganDict;
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


    /**
     * @param @param  specialImageList
     * @param @return
     * @return int
     * @throws
     * @Title: insertSpecialImageList
     * @Description: 批量插入专题选片列表
     */
//	R<List<Long>>  insertSpecialAnnotationList(List<SpecialAnnotation> list);


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


    /**
     * 根据原图生成需要的缩略图，保存到tb_sub_image表中
     *
     * @param inFile
     * @param id
     * @return
     */
    String generateThumbnails(Long specialId, Long specialImageId);

    AnnoMarkGeojson getMarkGeojsonByList(List<SpecialAnnotation> list);

    AnnoProperties getPropertiesBy(SpecialAnnotation req);

    R callBackSlideViscer(List<CallBackAnnAddIn> list);

    void callBackAnnoResult(AlgorithmAnnIn algorithmAnnIn);


}
