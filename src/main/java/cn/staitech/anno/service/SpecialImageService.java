package cn.staitech.anno.service;

import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.special.SpecialImage;
import cn.staitech.anno.domain.specialimage.InsertSpecialImageVO;
import cn.staitech.anno.domain.specialimage.SpecialImageSelectVO;
import cn.staitech.anno.domain.specialimage.SpecialImageVO;
import cn.staitech.anno.domain.specialimage.WaitSpecialImageVO;
import cn.staitech.anno.domain.specialimageanno.SpecialCutImageVO;
import cn.staitech.anno.domain.specialsliceimage.AuditSpecialImageVO;
import cn.staitech.common.core.domain.R;

import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SpecialImageService
 * @Description:专题选片表 服务类
 * @date 2023年6月2日
 */
public interface SpecialImageService {


    /**
     * 根据专题查看所有的图片
     *
     * @param specialId
     * @return
     */
    List<SpecialImage> selectSpecialId(Long specialId);


    /**
     * @param @param  specialImageList
     * @param @return
     * @return int
     * @throws
     * @Title: insertSpecialImageList
     * @Description: 批量插入专题选片列表
     */
    R<String> insertSpecialImageList(InsertSpecialImageVO vo) throws Exception;


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


    int updateByPrimaryKeySelective(SpecialImage record);


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
    List<SpecialImageVO> selectSpecialImageList(SpecialImageSelectVO specialImageSelectVO);

    List<Image> getImageBySpecialId(SpecialImageSelectVO specialImageSelectVO);


    SpecialImage selectByPrimaryKey(Long specialImageId);

    void cutImageNotice(SpecialCutImageVO resData);


}
