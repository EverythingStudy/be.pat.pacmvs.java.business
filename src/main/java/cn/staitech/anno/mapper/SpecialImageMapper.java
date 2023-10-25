package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.image.in.ImageBatchIdsVO;
import cn.staitech.anno.domain.special.SpecialImage;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageSelectVO;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageVO;
import cn.staitech.anno.domain.vo.specialImage.WaitSpecialImageVO;
import cn.staitech.anno.domain.vo.specialSliceImage.SpecialSliceSelectVO;
import cn.staitech.anno.domain.vo.specialSliceImage.SpecialSliceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SpecialImageMapper
 * @Description:专题选片表 Mapper 接口
 * @date 2023年6月2日
 */
public interface SpecialImageMapper {


    int deleteByPrimaryKey(Long specialImageId);

    int insert(SpecialImage record);

    int insertSelective(SpecialImage record);

    SpecialImage selectByPrimaryKey(Long specialImageId);

    int updateByPrimaryKeySelective(SpecialImage record);

    int updateByPrimaryKey(SpecialImage record);

    /**
     * @param @param  specialImageList
     * @param @return
     * @return int
     * @throws
     * @Title: insertSpecialImageList
     * @Description: 批量插入专题选片列表
     */
    int insertSpecialImageList(@Param("specialImageList") List<SpecialImage> specialImageList);


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
    List<Image> getImageBySpecialId(SpecialImageSelectVO specialImageSelectVO);

    /**
     * @param @param  specialImageSelectVO
     * @param @return
     * @return List<SpecialSliceVo>
     * @throws
     * @Title: selectSpecialSliceVoList
     * @Description: 查询全部切片配置
     */
    List<SpecialSliceVo> selectSpecialSliceVoList(SpecialSliceSelectVO specialSliceSelectVO);


    List<SpecialImage> selectSpecialImageListByParm(@Param("params") Map params);

    List<SpecialImage> selectEditIdListByParm(@Param("params") Map params);

    /***
     *  通过切片ID（imageId）查询SpecialImageList
     * @param imageId
     * @return
     */
    List<SpecialImage> selectListByImageId(Long imageId);

    /**
     * 通过切片ID（imageId）批量查询SpecialImageList
     *
     * @param imageIds
     * @return
     */
    List<SpecialImage> selectListByImageIds(ImageBatchIdsVO imageIds);

    /**
     * 通过切片ID（imageId）批量查询tb_special_image 表中imageId列表
     *
     * @param imageIds
     * @return
     */
    List<Long> selectImageIdsListByImageIds(ImageBatchIdsVO imageIds);

    /**
     * 根据专题查看所有的图片
     *
     * @param specialId
     * @return
     */
    List<SpecialImage> selectSpecialId(Long specialId);


    List<SpecialImageVO> selectSpecialImageList(SpecialImageSelectVO specialImageSelectVO);

}
