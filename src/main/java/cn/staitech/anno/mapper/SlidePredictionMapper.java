package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.SlidePrediction;
import cn.staitech.anno.vo.imagecsv.ImageCsvGetVO;
import cn.staitech.anno.vo.imagecsv.ImageCsvListVO;
import cn.staitech.anno.vo.predictioninfo.in.EyeSlideResult;
import cn.staitech.anno.vo.predictioninfo.in.EyeThumImageQuery;
import cn.staitech.anno.vo.predictioninfo.in.SlidePredictionQuery;
import cn.staitech.anno.vo.predictioninfo.out.SlidePredictionInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 眼科切片预测表 Mapper 接口
 * </p>
 *
 * @author wanglibei
 * @since 2023-11-02
 */
public interface SlidePredictionMapper extends BaseMapper<SlidePrediction> {

    List<SlidePredictionInfo> getOriginalSlideList(SlidePredictionQuery req);

    List<ImageCsvListVO> getImageCsvListVOList(ImageCsvGetVO request);

    List<Image> getMainImageList(EyeThumImageQuery query);

    List<EyeSlideResult> getSlidePredictionList(ImageCsvGetVO request);
}
