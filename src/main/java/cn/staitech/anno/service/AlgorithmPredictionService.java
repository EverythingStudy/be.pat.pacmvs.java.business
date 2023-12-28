package cn.staitech.anno.service;

import cn.staitech.anno.domain.Project;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.imagecsv.ImageCsvListVO;
import cn.staitech.anno.vo.predictionInfo.in.SlideImagePagerVO;
import cn.staitech.anno.vo.predictionInfo.in.SlidePredictionIn;
import cn.staitech.anno.vo.predictionInfo.in.StartPredictionIn;
import cn.staitech.anno.vo.predictionInfo.out.SlidePredictionOut;
import cn.staitech.common.core.domain.R;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: AlgorithmPredictionService
 * @Description:算法预测
 * @date 2023年11月2日
 */
public interface AlgorithmPredictionService {

    R startPrediction(StartPredictionIn req, Project project);

    SlidePredictionOut getOriginalSlideList(SlidePredictionIn req);

    PageMaster<ImageCsvListVO> slidePageList(SlideImagePagerVO request);
}
