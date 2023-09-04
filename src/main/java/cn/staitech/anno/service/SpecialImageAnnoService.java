package cn.staitech.anno.service;

import java.io.File;
import java.util.List;

import cn.staitech.anno.domain.geojson.MarkGeojson;
import cn.staitech.anno.domain.geojson.Properties;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.project.out.SystemDictOut;
import cn.staitech.anno.domain.specialAnnotation.SpecialAnnotation;
import cn.staitech.anno.domain.specilaImage.SpecialImage;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageSelectVO;
import cn.staitech.anno.domain.vo.specialImage.WaitSpecialImageVO;
import cn.staitech.anno.domain.vo.specialImageAnno.AnnoMarkGeojson;
import cn.staitech.anno.domain.vo.specialImageAnno.AnnoProperties;
import cn.staitech.anno.domain.vo.specialImageAnno.SpecialAnnoAddVO;
import cn.staitech.anno.domain.vo.specialImageAnno.in.AlgorithmAnnIn;
import cn.staitech.anno.domain.vo.specialImageAnno.in.CallBackAnnAddIn;
import cn.staitech.anno.domain.vo.specialImageAnno.in.SpecialAnnAddIn;
import cn.staitech.anno.domain.vo.specialImageAnno.in.SpecialAnnoProperties;
import cn.staitech.anno.domain.vo.specialSliceImage.AuditSpecialImageVO;
import cn.staitech.anno.domain.vo.specialSliceImage.OrganDict;
import cn.staitech.anno.exception.AnnoException;
import cn.staitech.common.core.domain.R;

/**
 * 
* @ClassName: SpecialImageAnnoService
* @Description:专题选片标注
* @author wanglibei
* @date 2023年6月12日
* @version V1.0
 */
public interface SpecialImageAnnoService  {
	
	
	/**
	 * 
	 * @Title: insertSpecialImageList
	 * @Description: 批量插入专题选片列表
	 * @param @param specialImageList
	 * @param @return
	 * @return int
	 * @throws
	 */
//	R<List<Long>>  insertSpecialAnnotationList(List<SpecialAnnotation> list);
	
	
	
	R<List<SpecialAnnoAddVO>>  annotationSave(List<SpecialAnnoAddVO> list,SpecialImage sImage) throws Exception;
	
	/**
     * 添加标注
     * @param marking 标注数据
     * @return true || false
     */
	
	int  insertSpecialAnnotation(SpecialAnnotation anno);
	
	 /**
     * 更新标注点数
     * @param marking 标注数据
     * @return true || false
     */
    int updatePointCount(SpecialAnnotation specialAnnotation);
	
	 /**
     * 根据切片id查询当前切片下当前标签的总数
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
    String getGeojsonUrls(String geojsonUrls,Long slideId);
	
    /**
     * 修改标注
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
	 * 
	 * @Title: insertSpecialImageList
	 * @Description: 批量审核专题选片列表
	 * @param @param specialImageList
	 * @param @return
	 * @return int
	 * @throws
	 */
	R<String> updateSpecialImageList(AuditSpecialImageVO vo);
	
	
	/**
	 * 
	 * @Title: insertSpecialImageList
	 * @Description: 切片数据交付
	 * @param @param specialImageList
	 * @param @return
	 * @return int
	 * @throws
	 */
	R<String> updateDeliveryBySpecialId(AuditSpecialImageVO vo);
	
	
	/**
	 * 
	 * @Title: selectWaitSpecialImage
	 * @Description: 查询全部待选的专题列表（根据切片编号/上传时间/添加状态查询)
	 * @param @param specialImageSelectVO
	 * @param @return
	 * @return List<WaitSpecialImageVO>
	 * @throws
	 */
	List<WaitSpecialImageVO> selectWaitSpecialImage(SpecialImageSelectVO specialImageSelectVO);


	/**
	 * 
	 * @Title: selectWaitSpecialImage
	 * @Description: 查询全部专题选片（根据切片编号/添加时间）
	 * @param @param specialImageSelectVO
	 * @param @return
	 * @return List<WaitSpecialImageVO>
	 * @throws
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
    String generateThumbnails(Long specialId,Long specialImageId);

    AnnoMarkGeojson getMarkGeojsonByList(List<SpecialAnnotation> list);

	AnnoProperties getPropertiesBy(SpecialAnnotation req);
	
	R callBackSlideViscer(List<CallBackAnnAddIn>  list);
	
	void callBackAnnoResult(AlgorithmAnnIn  algorithmAnnIn);
	

}
