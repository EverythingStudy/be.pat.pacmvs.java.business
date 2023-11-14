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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.annotation.Resource;

import java.io.File;
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
            //AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败 4：部分分析成功
            int aiAnalyzed = req.getAiAnalyzed();
            if(aiAnalyzed == 2 || aiAnalyzed == 4){
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
            		PredictionInfoVO predictionInfoVO = new PredictionInfoVO();
            		predictionInfoVO.setSlideId(slideId);
            		predictionInfoVO.setChunkTotal(1);
            		predictionInfoVO.setAiAnalyzed(aiAnalyzed);
            		predictionInfoVO.setAlgorithmImageUrl(req.getMergeImagePath());
            		predictionInfoVO.setUserId(req.getUserId());
            		predictionInfoVO.setOrganizationId(req.getOrganizationId());
            		File file = new File(req.getMergeImagePath());
            		predictionInfoVO.setImageName(file.getName());

            		//			{"chunkTotal":1,"imageName":"test","algorithmImageUrl":"C:/Users/86153/Desktop/dc/image/s1-168.ndpi","userId":10,"organizationId":95,"size":"447"}
            		slideImageService.uploadImage(predictionInfoVO, SecurityConstants.INNER);
            	}
            }else if(aiAnalyzed == 3){
            	//失败处理
            	//查询当前slideId对应的所有小切片id,全部修改为失败
            	 QueryWrapper<SlidePrediction> queryWrapper = new QueryWrapper<>();
                 queryWrapper.eq("slide_id", slideId).eq("del_flag", "0");
                 List<SlidePrediction> list = slidePredictionService.list(queryWrapper);
                 if(CollectionUtils.isNotEmpty(list)){
                	 List<SlidePrediction> spList = new ArrayList<>();
                	 for(SlidePrediction slidePrediction:list){
                		 SlidePrediction slideP = new SlidePrediction();
                		 slideP.setSlidePredictionId(slidePrediction.getSlidePredictionId());
                		 ////AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败 4：部分分析成功
                		 slideP.setAiAnalyzed(3);
                		 spList.add(slideP);
                	 }
                	 slidePredictionService.updateBatchById(spList);
                 }
            }
        }
        return R.ok();
    }


}

