package cn.staitech.anno.service;

import cn.staitech.anno.domain.Project;
import cn.staitech.anno.vo.predictionInfo.in.SlidePredictionIn;
import cn.staitech.anno.vo.predictionInfo.in.StartPredictionIn;
import cn.staitech.anno.vo.predictionInfo.out.SlidePredictionOut;
import cn.staitech.common.core.domain.R;

/**
 * 
* @ClassName: AlgorithmPredictionService
* @Description:算法预测
* @author wanglibei
* @date 2023年11月2日
* @version V1.0
 */
public interface AlgorithmPredictionService{

    R startPrediction(StartPredictionIn req,Project project);

    SlidePredictionOut getOriginalSlideList(SlidePredictionIn req);
}
