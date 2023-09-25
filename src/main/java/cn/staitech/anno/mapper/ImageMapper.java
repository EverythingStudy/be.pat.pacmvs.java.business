package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.image.in.ImageBatchIdsVO;
import cn.staitech.anno.domain.image.in.ImageListVO;
import cn.staitech.anno.domain.image.in.ImageTopicBatchIdsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 切片表（原图像表）数据层
 *
 * @author staitech
 */
public interface ImageMapper extends BaseMapper<Image> {

    /**
     * 查询切片列表
     *
     * @param image
     * @return
     */
    List<Image> selectListSlfe(Image image);

    /**
     * 查询单个切片信息
     *
     * @param imageId
     * @return
     */
    Image selectById(Long imageId);


    /**
     * 查询图像列表 - 通过 projectId 查询
     *
     * @param projectId
     * @return
     */
    List<ImageListVO> selectImageListByPorjectId(Long projectId);


    /**
     * 通过图片ID查询切片
     *
     * @param imageId
     * @return
     */
    Integer selectSlideCountByImageId(Long imageId);


    /**
     * 标注组选片入口预览图像列表
     *
     * @param image
     * @return
     */
    List<Image> selectImageChooseList(Image image);

    /**
     * 标注组选片入口传输图像
     *
     * @param imageIdList
     * @return
     */
    int updateBatchIds(ImageTopicBatchIdsVO imageIdList);

    /**
     * 标注组图像列表
     *
     * @param image
     * @return
     */
    List<Image> selectImageAnnotationList(Image image);

    /**
     * 运维图像删除
     *
     * @param imageId
     * @return
     */
    int deleteById(Long imageId);

    /**
     * 标注图像逻辑删除
     *
     * @param imageId
     * @return
     */
    int updateDeleteFlagById(Long imageId);

    /**
     * 标注图像批量逻辑删除
     *
     * @param imageIdList
     * @return
     */
    int updateDeleteFlagBatchIds(ImageBatchIdsVO imageIdList);

    /**
     * 更改图像上传状态
     *
     * @param imageIdList 图像ID列表
     * @return
     */
    void updateProcessFlagByIdList(@Param("imageIdList") List imageIdList);
}