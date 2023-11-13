package cn.staitech.anno.controller;

import cn.hutool.json.JSONUtil;
import cn.staitech.anno.domain.SlidePrediction;
import cn.staitech.anno.service.SlidePredictionService;
import cn.staitech.anno.service.remote.SlideImageService;
import cn.staitech.anno.vo.predictionInfo.in.EyeAlgorithmCallBackIn;
import cn.staitech.anno.vo.predictionInfo.in.EyeAnalyzedResult;
import cn.staitech.anno.vo.predictionInfo.in.PredictionInfoVO;
import cn.staitech.anno.vo.predictionInfo.out.SlidePredictionOut;
import cn.staitech.common.core.constant.SecurityConstants;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SlidePredictionCallBackController
 * @Description:算法预测回调
 * @date 2023年11月8日
 */
@Api(value = "算法预测回调", tags = "算法预测回调")
@Slf4j
@RestController
@RequestMapping("/slidePredictionCallBack")
public class SlidePredictionCallBackController {

    @Resource
    private SlidePredictionService slidePredictionService;

    @Resource
    private SlideImageService slideImageService;

    @ApiOperation(value = "眼科回调")
    @PostMapping("/eyeAlgorithm")
    public R<SlidePredictionOut> eyeAlgorithm(@Validated @RequestBody EyeAlgorithmCallBackIn req) {
        if (null != req) {
        	log.info("算法预测:{}",JSONUtil.toJsonStr(req));
            Long slideId = req.getSlideId();
            int aiAnalyzed = req.getAiAnalyzed();
            String mergeImagePath = req.getMergeImagePath();
            List<EyeAnalyzedResult> aiAnalyResult = req.getAiAnalyResult();
            if (CollectionUtils.isNotEmpty(aiAnalyResult)) {
                List<SlidePrediction> batchList = new ArrayList<SlidePrediction>();
                for (EyeAnalyzedResult result : aiAnalyResult) {
                    SlidePrediction sp = new SlidePrediction();
                    sp.setSlidePredictionId(result.getSlidePredictionId());
                    sp.setAiAnalyzed(result.getAiAnalyzed());
                    batchList.add(sp);
                }
                slidePredictionService.updateBatchById(batchList);
            }
            PredictionInfoVO predictionInfoVO = new PredictionInfoVO();
            predictionInfoVO.setSlideId(slideId);
            predictionInfoVO.setChunkTotal(1);
            predictionInfoVO.setAiAnalyzed(aiAnalyzed);
            predictionInfoVO.setAlgorithmImageUrl(mergeImagePath);
            predictionInfoVO.setUserId(req.getUserId());
            predictionInfoVO.setOrganizationId(req.getOrganizationId());

//			{"chunkTotal":1,"imageName":"test","algorithmImageUrl":"C:/Users/86153/Desktop/dc/image/s1-168.ndpi","userId":10,"organizationId":95,"size":"447"}
            slideImageService.uploadImage(predictionInfoVO, SecurityConstants.INNER);
        }
        return R.ok();
    }


}

