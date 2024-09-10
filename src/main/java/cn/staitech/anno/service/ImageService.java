package cn.staitech.anno.service;

import cn.staitech.anno.domain.image.Image;
import cn.staitech.anno.domain.image.in.ImageBatchIdsVO;
import cn.staitech.anno.domain.image.in.ImageListFindIn;
import cn.staitech.anno.domain.image.out.ImageListFindOut;
import cn.staitech.common.core.domain.PageResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.List;

/**
* @author 94024
* @description 针对表【tb_image】的数据库操作Service
* @createDate 2024-09-10 10:21:48
*/
public interface ImageService extends IService<Image> {

    PageResponse<ImageListFindOut> findImageList(ImageListFindIn findIn) throws ParseException;

    List<Long> deleteBatchIds(ImageBatchIdsVO ids);

}
