package cn.staitech.anno.service;

import java.util.List;

import javax.annotation.Resource;

import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.specilaImage.SpecialImage;
import cn.staitech.anno.domain.vo.image.SubImageVo;
import cn.staitech.anno.domain.vo.specialImage.InsertSpecialImageVO;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageSelectVO;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageVO;
import cn.staitech.anno.domain.vo.specialImage.WaitSpecialImageVO;
import cn.staitech.anno.domain.vo.specialImageAnno.SpecialCutImageVO;
import cn.staitech.anno.domain.vo.specialSliceImage.AuditSpecialImageVO;
import cn.staitech.anno.mapper.SpecialMapper;
import cn.staitech.common.core.domain.R;

/**
 * 
* @ClassName: SpecialImageService
* @Description:专题选片表 服务类
* @author wanglibei
* @date 2023年6月2日
* @version V1.0
 */
public interface SpecialImageService  {


	/**
	 * 根据专题查看所有的图片
	 * @param specialId
	 * @return
	 */
	List<SpecialImage> selectSpecialId(Long specialId);
	
	
	/**
	 * 
	 * @Title: insertSpecialImageList
	 * @Description: 批量插入专题选片列表
	 * @param @param specialImageList
	 * @param @return
	 * @return int
	 * @throws
	 */
	R<String>  insertSpecialImageList(InsertSpecialImageVO vo) throws Exception;
	
	
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
	
	
	int updateByPrimaryKeySelective(SpecialImage record);
	
	
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
	List<SpecialImageVO> selectSpecialImageList(SpecialImageSelectVO specialImageSelectVO);
	
	List<Image> getImageBySpecialId(SpecialImageSelectVO specialImageSelectVO);
	
	
	SpecialImage selectByPrimaryKey(Long specialImageId);
	
	void cutImageNotice(SpecialCutImageVO  resData);
	
	
	
	
	

}
