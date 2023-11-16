package cn.staitech.anno.service;

import cn.staitech.anno.domain.Image;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.image.in.*;
import cn.staitech.anno.vo.image.out.ImageListOutVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 图像 服务层
 *
 * @author wangfeng
 * @date 2023/06/01
 */
public interface ImageService extends IService<Image> {

    /**
     * 运维组图像列表
     *
     * @param image
     * @return
     */
    PageMaster<ImageListOutVO> selectList(ImageListVO image) throws ExecutionException, InterruptedException;

    /**
     * 项目管理-图像列表
     *
     * @param image
     * @return
     */
    PageMaster<ImageListOutVO> choiceList(ImageTopicVO image) throws ExecutionException, InterruptedException;

    /**
     * 查询单个切片信息
     *
     * @param imageId
     * @return
     */
    Image selectById(Long imageId);

    /**
     * 通过图片ID查询切片
     *
     * @param imageId
     * @return
     */
    Integer selectSlideCountByImageId(Long imageId);

    /**
     * 标注组选片入口传输图像
     *
     * @param ids
     * @return
     */
    int updateBatchIds(ImageTopicBatchIdsVO ids);

    /**
     * 标注组图像列表
     *
     * @param image
     * @return
     */
    List<Image> selectImageAnnotationList(Image image);

    List<Long> deleteBatchIds(ImageBatchIdsVO ids) throws InterruptedException;

    int updateById(ImageUpdateVO vo) throws Exception;

}